package com.mkandeel.actionmemo

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.Room.NotesDB
import com.mkandeel.actionmemo.databinding.ActivityMainBinding
import com.mkandeel.actionmemo.ui.AboutFragment
import com.mkandeel.actionmemo.ui.AddNoteFragment
import com.mkandeel.actionmemo.ui.HomeFragment
import com.mkandeel.actionmemo.ui.LoginFragment
import com.mkandeel.actionmemo.ui.ProfileFragment
import com.mkandeel.actionmemo.ui.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var helper: HelperClass
    private lateinit var notesDB: NotesDB
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = HelperClass(this)
        helper.setTheme(helper.getTheme())
        helper.setLocale(helper.getLangFromSP())

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdate()

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
        val view = layoutInflater.inflate(R.layout.delete_dialog, null, false)
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

    private val arl = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        val resultCode = result.resultCode
        when {
            resultCode == Activity.RESULT_OK -> {
                Log.v("MyActivity", "Update flow completed!")
            }
            resultCode == Activity.RESULT_CANCELED -> {
                Log.v("MyActivity", "User cancelled Update flow!")
            }
            else -> {
                Log.v("MyActivity", "Update flow failed with resultCode:$resultCode")
            }
        }
    }

    /*private val installStateUodateListener = InstallStateUpdatedListener {state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            helper.showToast(resources.getString(R.string.successful_update),1)
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }*/

    private fun checkForAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val updateAllowed = when(updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && updateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    arl,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }
}