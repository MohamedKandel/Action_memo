package com.mkandeel.actionmemo.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mkandeel.actionmemo.Helper.DataStoreManager
import com.mkandeel.actionmemo.Helper.HelperClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    val dataStoreManager = DataStoreManager(application)

    fun getTheme():LiveData<Boolean> {
        return dataStoreManager.getTheme().asLiveData(Dispatchers.IO)
    }
    fun setTheme(uiMode: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setTheme(uiMode)
        }
    }
}