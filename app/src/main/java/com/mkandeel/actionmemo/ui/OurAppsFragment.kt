package com.mkandeel.actionmemo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.mkandeel.actionmemo.Adapter.AppAdapter
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants.GOOGLE_PLAY_URL
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.data.Apps
import com.mkandeel.actionmemo.databinding.FragmentOurAppsBinding


class OurAppsFragment : Fragment(), ClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentOurAppsBinding
    private lateinit var adapter: AppAdapter
    private lateinit var list: MutableList<Apps>
    private lateinit var helper:HelperClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOurAppsBinding.inflate(inflater,container,false)
        helper = HelperClass(this)
        list = ArrayList<Apps>()
        adapter = AppAdapter(requireContext(),list,this)
        binding.appsRv.adapter = adapter

        onBackPressed()

        fillList()

        return binding.root
    }

    private fun fillList() {
        list.add(Apps(R.drawable.eg_1,resources.getString(R.string.eg)))
        list.add(Apps(R.drawable.pm_1,resources.getString(R.string.pm)))
        list.add(Apps(R.drawable.qr_1,resources.getString(R.string.qr)))
        list.add(Apps(R.drawable.icon,resources.getString(R.string.app_name)))

        adapter.updateAdapter(list)
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

    override fun onItemClickListener(position: Int, extras: Bundle?) {
        when(position) {
            0-> {
                helper.openGooglePlay("${GOOGLE_PLAY_URL}com.mkandeel.dalelelemanupdate")
            }
            1-> {
                helper.openGooglePlay("${GOOGLE_PLAY_URL}com.Mkandeel.passwordmanager")
            }
            2-> {
                helper.openGooglePlay("${GOOGLE_PLAY_URL}com.mkandeel.QRreader")
            }
            3-> {
                helper.openGooglePlay("${GOOGLE_PLAY_URL}com.mkandeel.actionmemo")
            }
        }
    }

    override fun onLongItemClickListener(position: Int, extras: Bundle?) {

    }

}