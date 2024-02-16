package com.mkandeel.actionmemo.ui

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Helper.RealPathUtil
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.databinding.FragmentContactBinding
import kotlinx.coroutines.launch
import java.io.File


class ContactFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentContactBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var util: RealPathUtil

    private val arl: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback<Uri?> {
            if (it != null) {
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

        lifecycleScope.launch {
            val mail = helper.getUserID()?.let { notesDB.userDAO().getUserMail(it) }
            binding.txtMail.setText(mail)
            binding.txtTitle.requestFocus()
        }

        onBackPressed()

        binding.apply {
            txtTitle.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.txtTitle.setHint(resources.getString(R.string.title_hint))
                } else {
                    binding.txtTitle.setHint("")
                }
            }

            btnSend.setOnClickListener {
                if (txtMail.text?.isEmpty() == true || txtTitle.text?.isEmpty() == true || txtBody.text?.isEmpty() == true) {
                    helper.showToast(resources.getString(R.string.complete_required), 0)
                } else {
                    // send to back-end
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