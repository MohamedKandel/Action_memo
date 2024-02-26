package com.mkandeel.actionmemo.Retrofit

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mkandeel.actionmemo.data.IssueBody
import com.mkandeel.actionmemo.data.NotificationBody
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: Repository
    private val TAG = "VIEW_MODEL TAG"
    private var responseMessage: MutableLiveData<ResponseBody> = MutableLiveData()

    init {
        repo = Repository(application)
    }

    fun sendMessage(messageBody: NotificationBody) {
        repo.sendMessageFCM(messageBody, object : RepositoryCallback<ResponseBody> {
            override fun onSuccess(response: ResponseBody) {
                responseMessage.postValue(response)
            }

            override fun onFailure(message: String) {
                Log.e(TAG, "onFailure: $message")
            }

        })
    }



    fun getSendingResponse(): LiveData<ResponseBody> {
        return responseMessage
    }
}