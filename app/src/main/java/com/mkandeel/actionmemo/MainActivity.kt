package com.mkandeel.actionmemo

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.mkandeel.actionmemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white,baseContext.theme)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                // navigate to fragments
                R.id.add -> Toast.makeText(baseContext,"Add",Toast.LENGTH_SHORT).show()
                R.id.about -> Toast.makeText(baseContext,"About",Toast.LENGTH_SHORT).show()
                R.id.home -> Toast.makeText(baseContext,"Home",Toast.LENGTH_SHORT).show()
                R.id.profile -> Toast.makeText(baseContext,"Profile",Toast.LENGTH_SHORT).show()
                R.id.setting -> Toast.makeText(baseContext,"Settings",Toast.LENGTH_SHORT).show()
                R.id.logout -> Toast.makeText(baseContext,"Logout",Toast.LENGTH_SHORT).show()
            }
            binding.drawerLayout.close()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}