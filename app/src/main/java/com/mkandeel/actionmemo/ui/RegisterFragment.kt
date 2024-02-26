package com.mkandeel.actionmemo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Helper.RealPathUtil
import com.mkandeel.actionmemo.MainActivity
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.Room.users.User
import com.mkandeel.actionmemo.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Calendar


class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var helper: HelperClass
    private lateinit var util: RealPathUtil
    private lateinit var img_array: ByteArray
    private lateinit var notesDB: NotesDB

    private fun isImageArrayInitialized() = ::img_array.isInitialized


    private val arl: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback<Uri?> {
            if (it != null) {
                val file = util.getRealPath(requireContext(), it)
                img_array = helper.imageFileToByteArray(file)
                helper.display(img_array, binding.profileLayout.profileImg)
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        helper = HelperClass(this)
        util = RealPathUtil()
        notesDB = NotesDB.getDBInstace(requireContext())

        helper.hideActionBar()

        onBackPressed()

        binding.txtCalendar.inputType = android.text.InputType.TYPE_NULL

        binding.profileLayout.profileImg.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.btnReturn.setOnClickListener {
            helper.navigateToFragment(LoginFragment())
        }

        binding.birthLayout.setStartIconOnClickListener {
            displayDatePicker()
        }

        binding.txtCalendar.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm =
                    view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                displayDatePicker()
            }
        }

        binding.txtCalendar.setOnClickListener {
            val imm =
                view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            displayDatePicker()
        }


        binding.btnRegister.setOnClickListener {
            lifecycleScope.launch {
                val list = notesDB.userDAO().getAllIDs()
                val ID = helper.generateID(list, 8)
                val username = binding.txtUsername.text.toString()
                val password = binding.txtPass.text.toString()
                val birthDate = binding.txtCalendar.text.toString()
                val mail = binding.txtMail.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty()
                    && birthDate.isNotEmpty() && mail.isNotEmpty()
                ) {

                    if (!isImageArrayInitialized()) {
                        val drawable =
                            resources.getDrawable(R.drawable.default_avatar, context?.theme)
                        val bitmap = (drawable as BitmapDrawable).bitmap
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        img_array = stream.toByteArray()
                    }
                    val user = User(ID, username, password, mail, birthDate, img_array)
                    if (username.equals("Google") || password.equals("Google@123") || mail.equals("Google@gmail.com")) {
                        helper.showToast(resources.getString(R.string.incorrect),0)
                    } else {
                        notesDB.userDAO().registerUser(user)
                        helper.navigateToFragment(LoginFragment())
                    }
                } else {
                    helper.showToast(resources.getString(R.string.complete_required), 0)
                }
            }
        }

        return binding.root
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
                binding.txtCalendar.setText("$dayOfMonth/${month + 1}/${year}")
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun onBackPressed() {
        val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity() /* lifecycle owner */,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    fragmentManager.popBackStack()
                }
            })
    }
}