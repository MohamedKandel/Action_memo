package com.mkandeel.actionmemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.databinding.ActivityMainBinding
import com.mkandeel.actionmemo.ui.AboutFragment
import com.mkandeel.actionmemo.ui.AddNoteFragment
import com.mkandeel.actionmemo.ui.HomeFragment
import com.mkandeel.actionmemo.ui.LoginFragment
import com.mkandeel.actionmemo.ui.ProfileFragment
import com.mkandeel.actionmemo.ui.RegisterFragment
import com.mkandeel.actionmemo.ui.SettingFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = HelperClass(this)
        helper.setTheme(helper.getTheme())
        helper.setLocale(helper.getLangFromSP())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notesDB = NotesDB.getDBInstace(this)
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)

        binding.drawerLayout.addDrawerListener(toggle)
//        if (helper.getTheme()) {
//            toggle.drawerArrowDrawable.color = resources.getColor(R.color.black, baseContext.theme)
//        } else {
//
//        }
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white, baseContext.theme)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                // navigate to fragments
                R.id.add -> helper.navigateToFragment(AddNoteFragment())
                R.id.about -> helper.navigateToFragment(AboutFragment())
                R.id.home -> helper.navigateToFragment(HomeFragment())
                R.id.profile -> helper.navigateToFragment(ProfileFragment())
                R.id.setting -> helper.navigateToFragment(SettingFragment())
                //R.id.logout -> logout()
            }
            binding.drawerLayout.close()
            true
        }

        binding.txtLogout.setOnClickListener {
            logout()
            binding.drawerLayout.close()
        }
    }

    private fun logout() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.delete_dialog,null,false)
        //val dialog = helper.showDialog(this,R.layout.delete_dialog, Gravity.BOTTOM,true)
        val img = view.findViewById<ImageView>(R.id.img_icon)
        val txt_title = view.findViewById<TextView>(R.id.txt_dialog_title)
        val txt_msg = view.findViewById<TextView>(R.id.txt_dialog_msg)
        val btn_del = view.findViewById<Button>(R.id.btn_delete)
        val btn_cancel = view.findViewById<Button>(R.id.btn_cancel)

        txt_title.setText(resources.getString(R.string.logout))
        txt_msg.setText(resources.getString(R.string.logout_msg))
        btn_del.setText(resources.getString(R.string.logout))
        img.setImageResource(R.drawable.wave)

        btn_del.setOnClickListener {
            dialog.apply {
                dismiss()
                cancel()
            }
            helper.navigateToFragment(LoginFragment())
        }

        btn_cancel.setOnClickListener {
            dialog.apply {
                dismiss()
                cancel()
            }
        }
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        //helper.clearSP()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}