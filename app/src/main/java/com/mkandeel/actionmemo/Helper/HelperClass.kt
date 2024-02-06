package com.mkandeel.actionmemo.Helper

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mkandeel.actionmemo.R
import kotlin.random.Random

class HelperClass {
    private lateinit var fragment: Fragment
    private lateinit var activity: Activity
    private lateinit var context: Context


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
        val view: View = activity.findViewById(R.id.drawerLayout)
        view.visibility = View.GONE
    }

    // use in other fragments
    fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
        val view: View = activity.findViewById(R.id.drawerLayout)
        view.visibility = View.VISIBLE
    }

    fun generateID(list: List<String>, length: Int): String {
        var id = ""
        val sequence = "0123456789"
        for (i in 0..<length) {
            val char = sequence[Random.nextInt(0, sequence.length)]
            id += char
        }
        if (list.contains(id)) {
            generateID(list, length)
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
        transaction.commit()
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
}