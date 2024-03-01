package com.mkandeel.actionmemo.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.mkandeel.actionmemo.Adapter.NoteAdapter
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants.CLICKED
import com.mkandeel.actionmemo.Helper.Constants.DELETE
import com.mkandeel.actionmemo.Helper.Constants.EDIT
import com.mkandeel.actionmemo.Helper.Constants.ID
import com.mkandeel.actionmemo.Helper.Constants.NOTE
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.Room.notes.Note
import com.mkandeel.actionmemo.databinding.FragmentHomeBinding

import kotlinx.coroutines.launch


class HomeFragment : Fragment(), ClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var helper: HelperClass
    private lateinit var noteDB: NotesDB
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        noteDB = NotesDB.getDBInstace(requireContext())

        // check if keyboard opened or not
        /*        var isKeyboardShowing = false;
                container?.viewTreeObserver?.addOnGlobalLayoutListener(
                    ViewTreeObserver.OnGlobalLayoutListener {
                        val r = Rect()
                        container.getWindowVisibleDisplayFrame(r)
                        val screenHeight: Int = container.rootView.height

                        val keypadHeight = screenHeight - r.bottom

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = false

                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = true
                            }
                        }
                    }
                )*/
        if (view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) != null) {
            val imm =
                view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        helper.showActionBar()
        noteList = ArrayList<Note>()
        adapter = NoteAdapter(requireContext(), noteList, this, helper)
        binding.noteRecyclerView.adapter = adapter
        val NavView =
            requireActivity().findViewById<NavigationView>(com.mkandeel.actionmemo.R.id.navView)
        val img = NavView.getHeaderView(0).findViewById<ImageView>(com.mkandeel.actionmemo.R.id.img)
        val txt = NavView.getHeaderView(0)
            .findViewById<TextView>(com.mkandeel.actionmemo.R.id.txt_username)

        onBackPressed()
        val id = if (arguments != null) {
            arguments?.getString(ID)
        } else {
            helper.getUserID()
        }

        lifecycleScope.launch {
            if (id != null) {
                val userData = noteDB.userDAO().getUserById(id)
                helper.display(userData.img, img)
                txt.text = resources.getString(com.mkandeel.actionmemo.R.string.hello)
                txt.append(", " + userData.username.split(" ")[0])
                img.setOnClickListener {
                    val drawerLayout =
                        requireActivity().findViewById<DrawerLayout>(com.mkandeel.actionmemo.R.id.drawerLayout)
                    helper.navigateToFragment(ProfileFragment())
                    drawerLayout.close()
                }

                noteList = noteDB.noteDAO().getNotes(id).toMutableList()
                adapter.updateAdapter(noteList)
            }
        }

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedItem = noteList[position]

                Log.v("Swipe", "Swiped item id is ${swipedItem.title}")
                // delete
                if (direction == ItemTouchHelper.RIGHT) {
                    deleteDialog(position)
                }
                // edit
                else if (direction == ItemTouchHelper.LEFT) {
                    val extras = Bundle()
                    extras.putParcelable(NOTE, swipedItem)
                    helper.navigateToFragment(AddNoteFragment(), extras)
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.noteRecyclerView)

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
        var noteObj: Note? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            noteObj = extras?.getParcelable(NOTE, Note::class.java)
        } else {
            noteObj = extras?.getParcelable(NOTE)
        }
        val dialog =
            helper.showTransparentDialog(
                requireContext(),
                com.mkandeel.actionmemo.R.layout.opened_note,
                Gravity.CENTER,
                false
            )
        val title = dialog.findViewById<TextView>(com.mkandeel.actionmemo.R.id.txt_title)
        val body = dialog.findViewById<TextView>(com.mkandeel.actionmemo.R.id.txt_note)
        val card = dialog.findViewById<CardView>(com.mkandeel.actionmemo.R.id.layout_card)
        title.text = noteObj?.title
        body.text = noteObj?.body
        when (noteObj?.priority) {
            0 -> {
                card.setCardBackgroundColor(
                    resources.getColor(
                        com.mkandeel.actionmemo.R.color.priority_0,
                        context?.theme
                    )
                )
            }

            1 -> {
                card.setCardBackgroundColor(
                    resources.getColor(
                        com.mkandeel.actionmemo.R.color.priority_1,
                        context?.theme
                    )
                )
            }

            2 -> {
                card.setCardBackgroundColor(
                    resources.getColor(
                        com.mkandeel.actionmemo.R.color.priority_2,
                        context?.theme
                    )
                )
            }

            3 -> {
                card.setCardBackgroundColor(
                    resources.getColor(
                        com.mkandeel.actionmemo.R.color.priority_3,
                        context?.theme
                    )
                )
            }
        }
        title.setOnLongClickListener {
            helper.copyTextToClipboard("Note ${noteObj?.id} title", title.text.toString())
            helper.showToast(resources.getString(com.mkandeel.actionmemo.R.string.text_copied), 0)
            true
        }

        body.setOnLongClickListener {
            helper.copyTextToClipboard("Note ${noteObj?.id} body", body.text.toString())
            helper.showToast(resources.getString(com.mkandeel.actionmemo.R.string.text_copied), 0)
            true
        }

    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {
        val request = extras?.getString(CLICKED)
        when (request) {
            DELETE -> {
                deleteDialog(position)
            }

            EDIT -> {
                helper.navigateToFragment(AddNoteFragment(), extras)
            }
        }
    }

    private fun deleteDialog(position: Int) {
        val dialog = helper.showDialog(
            requireContext(), R.layout.confirmation_dialog,
            Gravity.CENTER, false
        )
        val layout =
            dialog.findViewById<TextInputLayout>(R.id.layout_pass)
        layout.visibility = View.GONE
        val txt = dialog.findViewById<TextView>(R.id.txt_message)
        txt.text = resources.getString(com.mkandeel.actionmemo.R.string.delete_msg)
        val btn_ok = dialog.findViewById<Button>(R.id.btn_ok)
        val btn_cancel =
            dialog.findViewById<Button>(R.id.btn_cancel)

        btn_ok.setOnClickListener {
            lifecycleScope.launch {
                noteDB.noteDAO().deleteNote(noteList[position])
                noteList.removeAt(position)
                println(noteList[0].title)
                adapter.updateAdapter(noteList)
                dialog.dismiss()
                dialog.cancel()
            }
        }

        btn_cancel.setOnClickListener {
            adapter.notifyItemChanged(position)
            dialog.dismiss()
            dialog.cancel()
        }
    }
}