package com.mkandeel.actionmemo.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.databinding.FragmentShareBinding

class ShareFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentShareBinding
    private lateinit var helper:HelperClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(inflater, container, false)
        helper = HelperClass(this)

        onBackPressed()

        binding.btnShare.setOnClickListener {
            shareApp()
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
                    helper.navigateToFragment(SettingFragment())
                }
            })
    }

    private fun shareApp() {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
        //shareMessage = resources.getString(R.string.share_with)
        val shareMessage = "https://play.google.com/store/apps/details?id=com.mkandeel.actionmemo"
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        requireActivity()
            .startActivity(Intent.createChooser(intent, resources.getString(R.string.share_with)))
    }
}