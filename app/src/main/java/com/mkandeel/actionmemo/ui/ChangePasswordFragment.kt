package com.mkandeel.actionmemo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.databinding.FragmentChangePasswordBinding
import kotlinx.coroutines.launch


class ChangePasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        notesDB = NotesDB.getDBInstace(requireContext())

        onBackPressed()

        binding.apply {
            btnSave.setOnClickListener {
                if (txtPassword.text?.isEmpty() == true ||
                    txtNewPassword.text?.isEmpty() == true ||
                    txtConfirmPassword.text?.isEmpty() == true
                ) {
                    helper.showToast(resources.getString(R.string.complete_required), 0)
                    txtPassword.text?.clear()
                    txtConfirmPassword.text?.clear()
                    txtNewPassword.text?.clear()
                } else {
                    lifecycleScope.launch {
                        val password = helper.getUserID()
                            ?.let { it1 -> notesDB.userDAO().getUserPassword(it1) }
                        if (password?.equals(txtPassword.text.toString()) == false) {
                            helper.showToast(resources.getString(R.string.mis_match), 0)
                            txtPassword.text?.clear()
                            txtConfirmPassword.text?.clear()
                            txtNewPassword.text?.clear()
                        } else if (txtConfirmPassword.text?.toString()
                                ?.equals(txtNewPassword.text?.toString()) == false
                        ) {
                            helper.showToast(resources.getString(R.string.incorrect_password), 0)
                        } else {
                            val new_password = txtNewPassword.text?.toString()
                            if (new_password != null) {
                                notesDB.userDAO()
                                    .updateUserPassword(new_password, helper.getUserID())
                                helper.showToast(
                                    resources.getString(R.string.successfully_change),
                                    0
                                )
                            }
                        }
                    }
                }
            }

            btnCancel.setOnClickListener{
                helper.navigateToFragment(SettingFragment())
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