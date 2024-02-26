package com.mkandeel.actionmemo.ui

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Helper.RealPathUtil
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Retrofit.ViewModel
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.data.IssueBody
import com.mkandeel.actionmemo.data.NotificationBody
import com.mkandeel.actionmemo.data.NotificationData
import com.mkandeel.actionmemo.databinding.FragmentContactBinding
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File


class ContactFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentContactBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var util: RealPathUtil
    private lateinit var viewModel: ViewModel
    private var isImageChoosen = false
    private lateinit var imgUri: Uri

    private val arl: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback<Uri?> {
            if (it != null) {
                isImageChoosen = true
                imgUri = it
                val file = it.path?.let { it1 -> File(it1) }
                binding.txtFileName.setText(file?.name)
            }
        }
    )

    private val permissions: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback<Boolean> {
            if (it) {
                arl.launch("image/*")
            } else {
                helper.showToast(resources.getString(R.string.permission_required_2), 0)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        util = RealPathUtil()
        notesDB = NotesDB.getDBInstace(requireContext())
        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        lifecycleScope.launch {
            val mail = helper.getUserID()?.let { notesDB.userDAO().getUserMail(it) }
            binding.txtMail.setText(mail)
            binding.txtTitle.requestFocus()
        }

        onBackPressed()

        binding.apply {
            txtTitle.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.txtTitle.hint = resources.getString(R.string.title_hint)
                } else {
                    binding.txtTitle.hint = ""
                }
            }

            btnSend.setOnClickListener {
                if (txtMail.text?.isEmpty() == true || txtTitle.text?.isEmpty() == true || txtBody.text?.isEmpty() == true) {
                    helper.showToast(resources.getString(R.string.complete_required), 0)
                } else {
                    // send to back-end
                    val title = txtTitle.text.toString()
                    val body = txtBody.text.toString()
                    val email = txtMail.text.toString()
                    //sendFCM(title, body)

                    if (isImageChoosen) {
                        val id = helper.getUserID()
                        lifecycleScope.launch {
                            if (id != null) {
                                val username = notesDB.userDAO().getUsername(id)
                                context?.let { it1 ->
                                    uploadIssueWithImage(
                                        imgUri,
                                        it1, username, email, title, body
                                    )
                                }
                            }
                        }
                    } else {
                        lifecycleScope.launch {
                            val id = helper.getUserID()
                            if (id != null) {
                                val username = notesDB.userDAO().getUsername(id)
                                context?.let { it1 ->
                                    val issue = IssueBody(
                                        helper.generateID(),
                                        username,
                                        email,
                                        title,
                                        body,
                                        ""
                                    )
                                    uploadIssueWithoutImage(issue)
                                }
                            }
                        }
                    }
                }
            }

            btnCancel.setOnClickListener {
                helper.navigateToFragment(SettingFragment())
            }

            btnBrowse.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    permissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }


        return binding.root
    }

    private fun uploadIssueWithImage(
        uri: Uri, context: Context, name: String, email: String, title: String, body: String
    ) {
        val sRef = FirebaseStorage.getInstance().getReference("Images")
        val mRef = sRef.child(
            "${System.currentTimeMillis()}." +
                    "${helper.getFileExtn(uri)}"
        )
        mRef.putFile(uri).addOnSuccessListener {
            mRef.downloadUrl.addOnSuccessListener {
                val url = it.toString()
                val issueBody = IssueBody(helper.generateID(), name, email, title, body, url)
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("Database")
                ref.push().setValue(issueBody).addOnSuccessListener {
                    sendFCM(resources.getString(R.string.new_message), title)

                }
            }
        }
    }

    private fun uploadIssueWithoutImage(
        issueBody: IssueBody
    ) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Database")
        ref.child(issueBody.id).setValue(issueBody).addOnSuccessListener {
            sendFCM(resources.getString(R.string.new_message), issueBody.title)

        }
    }

    private fun sendFCM(msgTitle: String, msgBody: String) {
        val body = NotificationBody("/topics/AdminTopic", NotificationData(msgTitle, msgBody))
        viewModel.sendMessage(body)
        viewModel.getSendingResponse().observe(
            requireActivity()
        ) {
            //helper.showToast(resources.getString(R.string.pleasure_msg), 0)
            context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setTitle(resources.getString(R.string.pleasure_title))
                    .setMessage(resources.getString(R.string.pleasure_msg))
                    .setPositiveButton(
                        resources.getString(R.string.ok),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                            dialog.cancel()
                            binding.apply {
                                txtTitle.text?.clear()
                                txtBody.text?.clear()
                                txtFileName.text = ""
                            }
                        })
                    .show()
                    .setOnCancelListener {
                        binding.apply {
                            txtTitle.text?.clear()
                            txtBody.text?.clear()
                            txtFileName.text = ""
                        }
                    }
            }

            println(it.string())
            Log.v("Firebase Sending", "Message Sent")
        }
    }

    private fun onBackPressed() {
        (activity as AppCompatActivity).supportFragmentManager
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity() /* lifecycle owner */,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    helper.navigateToFragment(SettingFragment())
                }
            })
    }
}