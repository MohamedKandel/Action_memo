package com.mkandeel.actionmemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
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
        if (helper.getTheme()) {
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.black, baseContext.theme)
        } else {
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.white, baseContext.theme)
        }
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
                R.id.logout -> helper.navigateToFragment(LoginFragment())
            }
            binding.drawerLayout.close()
            true
        }
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