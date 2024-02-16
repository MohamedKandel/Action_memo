package com.mkandeel.actionmemo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Helper.RealPathUtil
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Calendar

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var util: RealPathUtil
    private lateinit var img_array: ByteArray

    private fun isImageArrayInitialized() = ::img_array.isInitialized

    private val arl: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback<Uri?> {
            if (it != null) {
                val file = util.getRealPath(requireContext(), it)
                img_array = helper.imageFileToByteArray(file)
                helper.display(img_array, binding.pictureLayout.profileImg)
            }
        }
    )

    private val permissions: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback<Boolean> {
            if (it) {
                arl.launch("image/*")
            } else {
                helper.showToast(resources.getString(R.string.permission_required), 0)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        util = RealPathUtil()
        notesDB = NotesDB.getDBInstace(requireContext())

        onBackPressed()

        lifecycleScope.launch {
            val user = helper.getUserID()?.let {
                notesDB.userDAO().getUserById(it)
            }
            user?.img?.let {
                img_array = it
                helper.display(it, binding.pictureLayout.profileImg)
            }
            binding.txtBirth.setText(user?.birthDate)
            binding.txtName.setText(user?.username)
            binding.txtMail.setText(user?.email)
        }

        binding.apply {
            txtBirth.inputType = android.text.InputType.TYPE_NULL
            birthLayout.setStartIconOnClickListener {
                displayDatePicker()
            }
            txtBirth.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    val imm =
                        view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    displayDatePicker()
                }
            }

            txtBirth.setOnClickListener {
                val imm =
                    view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                displayDatePicker()
            }

            pictureLayout.profileImg.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    permissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            pictureLayout.imgCamera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    permissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            btnSave.setOnClickListener {
                lifecycleScope.launch {
                    val name = txtName.text?.toString()
                    val mail = txtMail.text?.toString()
                    val birthDate = txtBirth.text?.toString()
                    if (birthDate?.isNotEmpty() == true && name?.isNotEmpty() == true &&
                        mail?.isNotEmpty() == true
                    ) {
                        helper.getUserID()?.let { it1 ->
                            notesDB.userDAO().updateUserByID(
                                username = name, email = mail,
                                birthDate = birthDate, img = img_array, it1
                            )
                            helper.showToast(resources.getString(R.string.update_msg), 0)
                            helper.navigateToFragment(HomeFragment())
                        }
                    }
                }
            }

            btnCancel.setOnClickListener {
                helper.navigateToFragment(HomeFragment())
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
                    helper.navigateToFragment(HomeFragment())
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun displayDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.txtBirth.setText("$dayOfMonth/${month + 1}/${year}")
            }, year, month, day
        )
        datePickerDialog.show()
    }

}