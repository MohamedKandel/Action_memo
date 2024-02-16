package com.mkandeel.actionmemo.Helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mkandeel.actionmemo.Helper.Constants.ID
import com.mkandeel.actionmemo.Helper.Constants.LANG
import com.mkandeel.actionmemo.Helper.Constants.MODE
import com.mkandeel.actionmemo.R
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.util.Locale
import kotlin.random.Random

class HelperClass {
    private lateinit var fragment: Fragment
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var sp: SharedPreferences
    private lateinit var editor: Editor

    constructor() {}

    constructor(activity: Activity) {
        this.activity = activity
        this.context = activity.baseContext
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment
        this.activity = fragment.requireActivity()
        this.context = fragment.requireContext()
    }

    constructor(context: Context) {
        this.context = context
    }

    private fun initSP() {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sp.edit()
    }

    fun setUserID(userID: String) {
        initSP()
        editor.putString(ID, userID)
        editor.commit()
        editor.apply()
    }

    fun getUserID(): String? {
        initSP()
        return sp.getString(ID, "0")
    }

    fun clearSP() {
        initSP()
        editor.clear()
        editor.apply()
    }

    fun hideView(viewID: Int) {
        val view: View = activity.findViewById(viewID)
        view.visibility = View.GONE
    }

    fun showView(viewID: Int) {
        val view: View = activity.findViewById(viewID)
        view.visibility = View.VISIBLE
    }

    // use in login fragment only
    fun hideActionBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val view: View = activity.findViewById(R.id.toolbar)
        view.visibility = View.GONE
        val navView: View = activity.findViewById(R.id.navView)
        navView.visibility = View.GONE
        val drawer: DrawerLayout = activity.findViewById(R.id.drawerLayout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    // use in other fragments
    fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
        val view: View = activity.findViewById(R.id.toolbar)
        view.visibility = View.VISIBLE
        val navView: View = activity.findViewById(R.id.navView)
        navView.visibility = View.VISIBLE
        val drawer: DrawerLayout = activity.findViewById(R.id.drawerLayout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun generateID(list: List<String>?, length: Int): String {
        var id = ""
        val sequence = "0123456789"
        for (i in 0..<length) {
            val char = sequence[Random.nextInt(0, sequence.length)]
            id += char
        }
        if (list != null) {
            if (list.contains(id)) {
                generateID(list, length)
            }
        }
        return id
    }

    fun String.getAlphaFromStirng(str: String): String {
        val chars = str.toCharArray()
        var alpha = ""
        for (c in chars) {
            if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' ||
                        c == '6' || c == '7' || c == '8' || c == '9' || c == '.')
            ) {
                alpha += c
            }
        }
        return alpha
    }

    fun String.getNumsFromString(str: String): String {
        val chars = str.toCharArray()
        var nums = ""
        for (c in chars) {
            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' ||
                c == '6' || c == '7' || c == '8' || c == '9' || c == '.'
            ) {
                nums += c
            }
        }
        return nums
    }

    fun navigateToFragment(fragment: Fragment) {
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateToFragment(fragment: Fragment, extras: Bundle?) {
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        fragment.arguments = extras
        transaction.replace(R.id.containerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun changeFont(fontName: String, vararg txts: TextView) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        for (txt in txts) {
            txt.typeface = typeFace
        }
    }

    fun changeFont(fontName: String, vararg btns: Button) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        for (btn in btns) {
            btn.typeface = typeFace
        }
    }

    fun changeFont(fontName: String, vararg txts: EditText) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        for (txt in txts) {
            txt.typeface = typeFace
        }
    }

    fun imageFileToByteArray(imageFile: String?): ByteArray {
        val inputStream = FileInputStream(imageFile)
        val byteBuffer = ByteArrayOutputStream()

        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        inputStream.close()

        return byteBuffer.toByteArray()
    }

    fun display(imageByteArray: ByteArray, imageView: ImageView) {
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        imageView.setImageBitmap(bitmap)
    }

    fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        context.createConfigurationContext(configuration)
    }

    fun setTheme(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun showDialog(
        context: Context,
        dialogLayout: Int,
        gravity: Int,
        withAnimation: Boolean
    ): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogLayout)
        dialog.show()
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        if (withAnimation) {
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        }
        dialog.window?.setGravity(gravity)
        return dialog
    }

    fun showToast(resID: String, lenght: Int) {
        if (lenght == 0) {
            Toast.makeText(context, resID, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, resID, Toast.LENGTH_LONG).show()
        }
    }

    fun setLangToSP(lang: String?) {
        initSP()
        editor.putString(LANG, lang)
        editor.commit()
        editor.apply()
    }

    fun getLangFromSP(): String {
        initSP()
        if (sp.getString(LANG, "en")?.equals("en") == true) {
            return "en"
        } else {
            return "ar"
        }
    }

    fun setThemeToSP(mode: Boolean) {
        initSP()
        editor.putBoolean(MODE, mode)
        editor.commit()
        editor.apply()
    }

    fun getTheme(): Boolean {
        initSP()
        return sp.getBoolean(MODE, false)
    }

    fun openGooglePlay(url: String) {
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}