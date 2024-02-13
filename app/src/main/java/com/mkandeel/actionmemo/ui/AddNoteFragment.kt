package com.mkandeel.actionmemo.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Helper.Constants
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.Room.notes.Note
import com.mkandeel.actionmemo.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        notesDB = NotesDB.getDBInstace(requireContext())

        var priority: Int = -1

        binding.layoutPriority0.setOnClickListener {
            priority = 0
            setPriority(priority)
        }
        binding.btnPriority0.setOnClickListener {
            priority = 0
            setPriority(priority)
        }

        binding.layoutPriority1.setOnClickListener {
            priority = 1
            setPriority(priority)
        }
        binding.btnPriority1.setOnClickListener {
            priority = 1
            setPriority(priority)
        }

        binding.layoutPriority2.setOnClickListener {
            priority = 2
            setPriority(priority)
        }
        binding.btnPriority2.setOnClickListener {
            priority = 2
            setPriority(priority)
        }

        binding.layoutPriority3.setOnClickListener {
            priority = 3
            setPriority(priority)
        }
        binding.btnPriority3.setOnClickListener {
            priority = 3
            setPriority(priority)
        }

        // edit request
        if (arguments != null) {
            var clickedNote: Note? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                clickedNote = arguments?.getParcelable(Constants.NOTE, Note::class.java)
            } else {
                clickedNote = arguments?.getParcelable(Constants.NOTE)
            }
            binding.txtTitle.setText(clickedNote?.title)
            binding.txtBody.setText(clickedNote?.body)
            setPriority(clickedNote?.priority)

            binding.btnSave.setOnClickListener {
                val id = clickedNote?.id
                val title = binding.txtTitle.text.toString()
                val body = binding.txtBody.text.toString()

                lifecycleScope.launch {
                    if (id != null) {
                        notesDB.noteDAO().updateNoteByID(title, body, priority, id)
                        binding.txtTitle.text?.clear()
                        binding.txtBody.text?.clear()
                        when (priority) {
                            0 -> binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                            1 -> binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                            2 -> binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                            3 -> binding.btnPriority3.setImageResource(R.drawable.check_box_3)
                        }
                    }
                }
            }
        }
        // add new note
        else {
            binding.btnSave.setOnClickListener {
                val user_id = helper.getUserID()
                val title = binding.txtTitle.text.toString()
                val body = binding.txtBody.text.toString()
                val note = user_id?.let { it1 ->
                    Note(userId = it1, title = title, body = body, priority = priority)
                }
                lifecycleScope.launch {
                    if (note != null) {
                        notesDB.noteDAO().addNote(note)
                        binding.txtTitle.text?.clear()
                        binding.txtBody.text?.clear()
                        when (priority) {
                            0 -> binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                            1 -> binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                            2 -> binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                            3 -> binding.btnPriority3.setImageResource(R.drawable.check_box_3)
                        }
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            helper.navigateToFragment(HomeFragment())
        }


        return binding.root
    }

    private fun setPriority(priority: Int?) {
        when (priority) {
            0 -> {
                binding.btnPriority0.setImageResource(R.drawable.check)
                binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                binding.btnPriority3.setImageResource(R.drawable.check_box_3)
            }

            1 -> {
                binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                binding.btnPriority1.setImageResource(R.drawable.check)
                binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                binding.btnPriority3.setImageResource(R.drawable.check_box_3)
            }

            2 -> {
                binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                binding.btnPriority2.setImageResource(R.drawable.check)
                binding.btnPriority3.setImageResource(R.drawable.check_box_3)
            }

            3 -> {
                binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                binding.btnPriority3.setImageResource(R.drawable.check)
            }
        }
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

    private fun pickPriority(): Int {
        var priority: Int = -1

        return priority
    }
}