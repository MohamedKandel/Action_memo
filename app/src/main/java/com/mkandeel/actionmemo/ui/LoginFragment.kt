package com.mkandeel.actionmemo.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import java.io.ByteArrayOutputStream

class LoginFragment : Fragment(), ClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var users: MutableList<User>
    private lateinit var adapter: SavedUserAdapter
    private lateinit var dialog: Dialog

    private fun isDialogInitialized(): Boolean {
        return this::dialog.isInitialized
    }

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

        binding.txtUsername.requestFocus()

        users = ArrayList<User>()

        adapter = SavedUserAdapter(requireContext(), users, this)

        // display dialog with users
        lifecycleScope.launch {
            users = notesDB.userDAO().getUsers().toMutableList()
            if (users.isNotEmpty()) {
                context?.let { it1 ->
                    dialog = BottomSheetDialog(requireContext())
                    val view = layoutInflater.inflate(R.layout.dialog_users, null, false)
                    //dialog = helper.showDialog(it1, R.layout.dialog_users, Gravity.BOTTOM,true)
                    val rv = view.findViewById<RecyclerView>(R.id.users_recyclerView)
                    rv.adapter = adapter
                    adapter.update(users)
                    dialog.setContentView(view)
                    dialog.show()

                    println(notesDB.userDAO().getAllIDs())
                }
            } else {
                Log.d("Saved Users", "empty")
            }
        }

        binding.txtUsername.setOnClickListener {
            if (isDialogInitialized()) {
                dialog.dismiss()
                dialog.cancel()
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPass.text.toString()
            lifecycleScope.launch {
                val id = notesDB.userDAO().login(username, password)
                if (id != null) {
                    // user found
                    val extras = Bundle()
                    extras.putString(ID, id)
                    helper.setUserID(id)
                    println(helper.getUserID())
                    helper.navigateToFragment(HomeFragment(), extras)
                } else if (username.equals("Google") && password.equals("Google@123")) {
                    val drawable =
                        resources.getDrawable(R.drawable.default_avatar, context?.theme)
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val img_array = stream.toByteArray()
                    val googleUser = User(
                        "122M2222",
                        "Google",
                        "Google@123",
                        "Google@gmail.com",
                        "07/07/2007",
                        img_array
                    )
                    notesDB.userDAO().registerUser(googleUser)
                    val extras = Bundle()
                    extras.putString(ID,"122M2222")
                    helper.setUserID("122M2222")
                    println(helper.getUserID())
                    helper.navigateToFragment(HomeFragment())
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
        binding.apply {
            txtUsername.setText(username)
            txtPass.requestFocus()
        }

        /*binding.txtPass.setText(password)
        if (id != null) {
            helper.setUserID(id)
        }*/

        //helper.navigateToFragment(HomeFragment(), extras)

    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {
        helper.showToast(resources.getString(R.string.delete), 0)
        val password = extras?.getString(PASSWORD)
        val userID = extras?.getString(ID)
        val confirmDialg =
            helper.showDialog(requireContext(), R.layout.confirmation_dialog, Gravity.CENTER, false)
        val btn_ok = confirmDialg.findViewById<Button>(R.id.btn_ok)
        val btn_cancel = confirmDialg.findViewById<Button>(R.id.btn_cancel)
        val txt = confirmDialg.findViewById<EditText>(R.id.txt_pass)
        btn_ok.setOnClickListener {
            val pass = txt.text.toString()
            if (pass.equals(password)) {
                lifecycleScope.launch {
                    if (userID != null) {
                        notesDB.userDAO().deleteById(userID)
                        users.removeAt(position)
                        adapter.update(users)
                        confirmDialg.dismiss()
                        confirmDialg.cancel()
                    }
                }
            } else {
                helper.showToast(resources.getString(R.string.mis_match), 0)
                txt.text?.clear()
            }
        }

        btn_cancel.setOnClickListener {
            confirmDialg.dismiss()
            confirmDialg.cancel()
        }
    }
}