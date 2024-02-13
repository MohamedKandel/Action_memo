package com.mkandeel.actionmemo.ui

import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAboutBinding
    private lateinit var helper: HelperClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        helper = HelperClass(this)

        binding.apply {
            val pInfo = context?.packageManager?.getPackageInfo(
                requireContext().packageName,
                0
            )
            val versionName = pInfo?.versionName
            txtVersion.append(" $versionName")
        }

        return binding.root
    }

}