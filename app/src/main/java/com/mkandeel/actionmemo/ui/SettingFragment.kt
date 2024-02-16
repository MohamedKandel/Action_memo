package com.mkandeel.actionmemo.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mkandeel.actionmemo.Adapter.SettingAdapter
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.MainActivity
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.data.SettingModel
import com.mkandeel.actionmemo.databinding.FragmentSettingBinding
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class SettingFragment : Fragment(), ClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentSettingBinding
    private lateinit var helper: HelperClass
    private lateinit var adapter: SettingAdapter
    private lateinit var list: MutableList<SettingModel>
    private lateinit var notesDB: NotesDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        helper = HelperClass(this)
        notesDB = NotesDB.getDBInstace(requireContext())

        list = ArrayList<SettingModel>()
        adapter = SettingAdapter(requireContext(), list, this)
        binding.settingRecyclerView.adapter = adapter

        onBackPressed()

        fillList()

        binding.btnSave.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            requireActivity().finish()
            startActivity(intent)
        }

        return binding.root
    }

    private fun fillList() {
        list.add(
            SettingModel(
                R.drawable.lang, resources.getString(R.string.app_lang), R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.night, resources.getString(R.string.mode_change), R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.apps, resources.getString(R.string.apps), R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.contact_us, resources.getString(R.string.contact_us),
                R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.share, resources.getString(R.string.share),
                R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.lock, resources.getString(R.string.change_password),
                R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.delete, resources.getString(R.string.delete_notes),
                R.drawable.arrow
            )
        )
        list.add(
            SettingModel(
                R.drawable.remove, resources.getString(R.string.delete_account),
                R.drawable.arrow
            )
        )

        adapter.updateAdapter(list)

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

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        when (position) {
            0 -> {
                var selectedItem = 0
                val arr = resources.getStringArray(R.array.languages)
                lifecycleScope.launch {
                    val currentState = helper.getLangFromSP()
                    helper.showToast(currentState, 0)
                    val builder = AlertDialog.Builder(requireContext())
                    if (currentState.contains("en")) {
                        builder.setSingleChoiceItems(
                            arr,
                            0,
                            DialogInterface.OnClickListener { dialog, which ->
                                selectedItem = which
                            })
                    } else {
                        builder.setSingleChoiceItems(
                            arr,
                            1,
                            DialogInterface.OnClickListener { dialog, which ->
                                selectedItem = which
                            })
                    }
                    builder.setPositiveButton(resources.getString(R.string.ok),
                        DialogInterface.OnClickListener { dialog, which ->

                            if ((selectedItem == 1 && currentState.contains("en"))
                                || (selectedItem == 0 && currentState.contains("ar"))
                            ) {
                                binding.btnSave.visibility = View.VISIBLE
                            }

                            var lang = ""
                            if (selectedItem == 0) {
                                lang = "en"
                            } else {
                                lang = "ar"
                            }
                            helper.setLangToSP(lang)
                            dialog.apply {
                                dismiss()
                                cancel()
                            }

                        })

                    builder.setNegativeButton(resources.getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.apply {
                                dismiss()
                                cancel()
                            }
                        })
                    builder.show()
                }

            }

            1 -> {
                var selectedItem = 0
                val arr = resources.getStringArray(R.array.mode)
                lifecycleScope.launch {
                    val currentState = helper.getTheme()
                    helper.showToast("$currentState", 0)
                    val builder = AlertDialog.Builder(requireContext())
                    if (!currentState) {
                        builder.setSingleChoiceItems(
                            arr,
                            0,
                            DialogInterface.OnClickListener { dialog, which ->
                                selectedItem = which
                            })
                    } else {
                        builder.setSingleChoiceItems(
                            arr,
                            1,
                            DialogInterface.OnClickListener { dialog, which ->
                                selectedItem = which
                            })
                    }
                    builder.setPositiveButton(resources.getString(R.string.ok),
                        DialogInterface.OnClickListener { dialog, which ->
                            if ((selectedItem == 1 && !currentState) ||
                                (selectedItem == 0 && currentState)
                            ) {
                                binding.btnSave.visibility = View.VISIBLE
                            }

                            lifecycleScope.launch {
                                if (selectedItem == 0) {
                                    helper.setThemeToSP(false)
                                } else {
                                    helper.setThemeToSP(true)
                                }
                                dialog.apply {
                                    dismiss()
                                    cancel()
                                }
                            }
                        })

                    builder.setNegativeButton(resources.getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.apply {
                                dismiss()
                                cancel()
                            }
                        })
                    builder.show()
                }
            }

            2 -> {
                helper.navigateToFragment(OurAppsFragment())
            }

            3 -> {
                helper.navigateToFragment(ContactFragment())
            }

            4 -> {
                helper.navigateToFragment(ShareFragment())
            }

            5 -> {
                helper.navigateToFragment(ChangePasswordFragment())
            }

            6 -> {
                val dialog = helper.showDialog(
                    requireContext(),
                    R.layout.delete_dialog,
                    Gravity.CENTER,
                    false
                )
                val img = dialog.findViewById<ImageView>(R.id.img_icon)
                val txt_title = dialog.findViewById<TextView>(R.id.txt_dialog_title)
                val txt_msg = dialog.findViewById<TextView>(R.id.txt_dialog_msg)
                val btn_del = dialog.findViewById<Button>(R.id.btn_delete)
                val btn_cancel = dialog.findViewById<Button>(R.id.btn_cancel)

                img.setColorFilter(resources.getColor(R.color.red, context?.theme))

                txt_title.setText(resources.getString(R.string.delete_all_notes_title))
                txt_msg.setText(resources.getString(R.string.delete_all_notes_msg))

                btn_del.setOnClickListener {
                    lifecycleScope.launch {
                        helper.getUserID()?.let { it1 ->
                            val password = notesDB.userDAO().getUserPassword(it1)
                            val dialog_confirm = helper.showDialog(
                                requireContext(),
                                R.layout.confirmation_dialog,
                                Gravity.CENTER,
                                false
                            )
                            val txt = dialog_confirm.findViewById<TextView>(R.id.txt_message)
                            txt.setText(resources.getString(R.string.enter_password_notes))
                            val editText = dialog_confirm.findViewById<EditText>(R.id.txt_pass)
                            val btn_ok = dialog_confirm.findViewById<Button>(R.id.btn_ok)
                            val btn_cancel = dialog_confirm.findViewById<Button>(R.id.btn_cancel)

                            btn_ok.setOnClickListener {
                                val entered_password = editText.text.toString()
                                if (entered_password.equals(password)) {
                                    lifecycleScope.launch {
                                        notesDB.noteDAO().deleteAllNotes(it1)
                                        dialog_confirm.apply {
                                            dismiss()
                                            cancel()
                                        }
                                        helper.showToast(
                                            resources.getString(R.string.notes_deleted),
                                            0
                                        )
                                    }
                                } else {
                                    helper.showToast(resources.getString(R.string.mis_match), 0)
                                    dialog_confirm.apply {
                                        dismiss()
                                        cancel()
                                    }
                                    dialog.apply {
                                        dismiss()
                                        cancel()
                                    }
                                }
                            }

                            btn_cancel.setOnClickListener {
                                dialog_confirm.apply {
                                    dismiss()
                                    cancel()
                                }
                            }

                        }
                        dialog.apply {
                            dismiss()
                            cancel()
                        }
                    }
                }

                btn_cancel.setOnClickListener {
                    dialog.apply {
                        dismiss()
                        cancel()
                    }
                }
            }

            7 -> {
                val dialog = helper.showDialog(
                    requireContext(),
                    R.layout.delete_dialog,
                    Gravity.CENTER,
                    false
                )
                val img = dialog.findViewById<ImageView>(R.id.img_icon)
                val txt_title = dialog.findViewById<TextView>(R.id.txt_dialog_title)
                val txt_msg = dialog.findViewById<TextView>(R.id.txt_dialog_msg)
                val btn_del = dialog.findViewById<Button>(R.id.btn_delete)
                val btn_cancel = dialog.findViewById<Button>(R.id.btn_cancel)

                img.setImageResource(R.drawable.remove_dialog)
                img.setColorFilter(resources.getColor(R.color.red, context?.theme))

                txt_title.setText(resources.getString(R.string.delete_account_title))
                txt_msg.setText(resources.getString(R.string.delete_account_msg))

                btn_del.setOnClickListener {
                    lifecycleScope.launch {
                        helper.getUserID()?.let { it1 ->
                            val password = notesDB.userDAO().getUserPassword(it1)
                            Log.d("Password", password)
                            val dialog_confirm = helper.showDialog(
                                requireContext(),
                                R.layout.confirmation_dialog,
                                Gravity.CENTER,
                                false
                            )
                            val editText = dialog_confirm.findViewById<EditText>(R.id.txt_pass)
                            val btn_ok = dialog_confirm.findViewById<Button>(R.id.btn_ok)
                            val btn_cancel = dialog_confirm.findViewById<Button>(R.id.btn_cancel)

                            btn_ok.setOnClickListener {
                                val entered_password = editText.text.toString()
                                if (entered_password.equals(password)) {
                                    lifecycleScope.launch {
                                        notesDB.noteDAO().deleteAllNotes(it1)
                                        notesDB.userDAO().deleteById(it1)
                                        dialog_confirm.apply {
                                            dismiss()
                                            cancel()
                                        }
                                        helper.showToast(
                                            resources.getString(R.string.user_deleted),
                                            0
                                        )
                                        helper.navigateToFragment(LoginFragment())
                                    }
                                } else {
                                    helper.showToast(resources.getString(R.string.mis_match), 0)
                                    dialog_confirm.apply {
                                        dismiss()
                                        cancel()
                                    }
                                    dialog.apply {
                                        dismiss()
                                        cancel()
                                    }
                                }
                            }

                            btn_cancel.setOnClickListener {
                                dialog_confirm.apply {
                                    dismiss()
                                    cancel()
                                }
                            }

                        }
                        dialog.apply {
                            dismiss()
                            cancel()
                        }
                    }
                }

                btn_cancel.setOnClickListener {
                    dialog.apply {
                        dismiss()
                        cancel()
                    }
                }
            }
        }
    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {

    }
}