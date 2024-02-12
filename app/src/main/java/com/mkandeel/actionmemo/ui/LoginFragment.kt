package com.mkandeel.actionmemo.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Adapter.SavedUserAdapter
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants.ID
import com.mkandeel.actionmemo.Helper.Constants.PASSWORD
import com.mkandeel.actionmemo.Helper.Constants.USERNAME
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.Room.users.User
import com.mkandeel.actionmemo.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), ClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var users: List<User>
    private lateinit var adapter: SavedUserAdapter
    private lateinit var dialog: Dialog

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        notesDB = NotesDB.getDBInstace(requireContext())
        helper.hideActionBar()
        onBackPressed()

        users = ArrayList<User>()

        adapter = SavedUserAdapter(requireContext(), users, this)


        binding.txtUsername.setOnClickListener {
            // display dialog with users
            lifecycleScope.launch {
                users = notesDB.userDAO().getUsers()
                if (users.isNotEmpty()) {
                    context?.let { it1 ->
                        dialog = helper.showDialog(it1, R.layout.dialog_users, Gravity.BOTTOM)
                        val rv = dialog.findViewById<RecyclerView>(R.id.users_recyclerView)
                        rv.adapter = adapter
                        adapter.update(users)

                        println(notesDB.userDAO().getAllIDs())
                    }
                } else {
                    Log.d("Saved Users", "empty")
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPass.text.toString()
            lifecycleScope.launch {
                val id = notesDB.userDAO().login(username, password)
                if (id != null) {
                    // user found
                    helper.showToast(resources.getString(R.string.logged_in), 0)
                    val extras = Bundle()
                    extras.putString(ID, id)
                    helper.setUserID(id)
                    helper.navigateToFragment(HomeFragment(), extras)
                } else {
                    helper.showToast(resources.getString(R.string.incorrect), 0)
                    binding.txtUsername.text?.clear()
                    binding.txtPass.text?.clear()
                    binding.txtUsername.requestFocus()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            helper.navigateToFragment(RegisterFragment())
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun onBackPressed() {
        (activity as AppCompatActivity).supportFragmentManager
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity() /* lifecycle owner */,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    requireActivity().finish()
                }
            })
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        dialog.dismiss()
        dialog.cancel()
        val username = extras?.getString(USERNAME)
        val password = extras?.getString(PASSWORD)
        val id = extras?.getString(ID)
        // call navigate to home method
        binding.txtUsername.setText(username)
        binding.txtPass.setText(password)
        if (id != null) {
            helper.setUserID(id)
        }

        helper.navigateToFragment(HomeFragment(), extras)

    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {
        helper.showToast(resources.getString(R.string.delete), 0)
        val password = extras?.getString(PASSWORD)
        val userID = extras?.getString(ID)
        val confirmDialg =
            helper.showDialog(requireContext(), R.layout.confirmation_dialog, Gravity.CENTER)
        val btn_ok = confirmDialg.findViewById<Button>(R.id.btn_ok)
        val btn_cancel = confirmDialg.findViewById<Button>(R.id.btn_cancel)
        val txt = confirmDialg.findViewById<EditText>(R.id.txt_pass)
        btn_ok.setOnClickListener {
            val pass = txt.text.toString()
            if (pass.equals(password)) {
                lifecycleScope.launch {
                    if (userID != null) {
                        notesDB.userDAO().deleteById(userID)
                        users.drop(position)
                        adapter.update(users)
                        confirmDialg.dismiss()
                        confirmDialg.cancel()
                    }
                }
            }
        }

        btn_cancel.setOnClickListener {
            confirmDialg.dismiss()
            confirmDialg.cancel()
        }
    }
}