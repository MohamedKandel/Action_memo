package com.mkandeel.actionmemo.ui

import android.graphics.Color
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

        var priority:Int = -1

        binding.layoutPriority0.setOnClickListener {
            priority = 0
            binding.btnPriority0.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }
        binding.btnPriority0.setOnClickListener {
            priority = 0
            binding.btnPriority0.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }

        binding.layoutPriority1.setOnClickListener {
            priority = 1
            binding.btnPriority1.setImageResource(R.drawable.check)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }
        binding.btnPriority1.setOnClickListener {
            priority = 1
            binding.btnPriority1.setImageResource(R.drawable.check)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }

        binding.layoutPriority2.setOnClickListener {
            priority = 2
            binding.btnPriority2.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }
        binding.btnPriority2.setOnClickListener {
            priority = 2
            binding.btnPriority2.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
            binding.btnPriority3.setImageResource(R.drawable.check_box_3)
        }

        binding.layoutPriority3.setOnClickListener {
            priority = 3
            binding.btnPriority3.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
        }
        binding.btnPriority3.setOnClickListener {
            priority = 3
            binding.btnPriority3.setImageResource(R.drawable.check)
            binding.btnPriority1.setImageResource(R.drawable.check_box_1)
            binding.btnPriority2.setImageResource(R.drawable.check_box_2)
            binding.btnPriority0.setImageResource(R.drawable.check_box_0)
        }

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
                    when(priority) {
                        0-> binding.btnPriority0.setImageResource(R.drawable.check_box_0)
                        1-> binding.btnPriority1.setImageResource(R.drawable.check_box_1)
                        2-> binding.btnPriority2.setImageResource(R.drawable.check_box_2)
                        3-> binding.btnPriority3.setImageResource(R.drawable.check_box_3)
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
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

    private fun pickPriority(): Int {
        var priority: Int = -1

        return priority
    }
}