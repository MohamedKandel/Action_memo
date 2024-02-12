package com.mkandeel.actionmemo.ui

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.mkandeel.actionmemo.Adapter.NoteAdapter
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants.CLICKED
import com.mkandeel.actionmemo.Helper.Constants.DELETE
import com.mkandeel.actionmemo.Helper.Constants.EDIT
import com.mkandeel.actionmemo.Helper.Constants.ID
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.Room.notes.Note
import com.mkandeel.actionmemo.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), ClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var helper: HelperClass
    private lateinit var noteDB: NotesDB
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: List<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        noteDB = NotesDB.getDBInstace(requireContext())

        helper.showActionBar()
        noteList = ArrayList<Note>()
        adapter = NoteAdapter(requireContext(), noteList, this)
        binding.noteRecyclerView.adapter = adapter
        val NavView = requireActivity().findViewById<NavigationView>(R.id.navView)
        val img = NavView.getHeaderView(0).findViewById<ImageView>(R.id.img)
        val txt = NavView.getHeaderView(0).findViewById<TextView>(R.id.txt_username)

        onBackPressed()
        val id = if (arguments != null) {
            arguments?.getString(ID)
        } else {
            helper.getUserID()
        }

        lifecycleScope.launch {
            if (id != null) {
                val userData = noteDB.userDAO().getUserById(id)
                helper.setLocale(userData.lang)

                helper.display(userData.img, img)
                txt.text = resources.getString(R.string.hello)
                txt.append(", " + userData.username.split(" ")[0])

                noteList = noteDB.noteDAO().getNotes(id)
                adapter.updateAdapter(noteList)
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
                    requireActivity().finish()
                }
            })
    }

    override fun onItemClickListener(position: Int, extras: Bundle?) {

    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {
        val request = extras?.getString(CLICKED)
        when (request) {
            DELETE -> {
                val dialog = helper.showDialog(
                    requireContext(),
                    R.layout.confirmation_dialog,
                    Gravity.CENTER
                )
                val layout = dialog.findViewById<TextInputLayout>(R.id.layout_pass)
                layout.visibility = View.GONE
                val txt = dialog.findViewById<TextView>(R.id.txt_message)
                txt.text = resources.getString(R.string.delete_msg)
                val btn_ok = dialog.findViewById<Button>(R.id.btn_ok)
                val btn_cancel = dialog.findViewById<Button>(R.id.btn_cancel)

                btn_ok.setOnClickListener {
                    lifecycleScope.launch {
                        noteDB.noteDAO().deleteNote(noteList[position])
                        noteList.drop(position)
                        adapter.updateAdapter(noteList)
                        dialog.dismiss()
                        dialog.cancel()
                    }
                }

                btn_cancel.setOnClickListener {
                    dialog.dismiss()
                    dialog.cancel()
                }
            }

            EDIT -> {

            }
        }
    }
}