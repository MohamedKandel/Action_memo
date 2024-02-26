package com.mkandeel.actionmemo.Retrofit

import android.app.Application
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mkandeel.actionmemo.Helper.Constants.FCM_URL
import com.mkandeel.actionmemo.data.IssueBody
import com.mkandeel.actionmemo.data.NotificationBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository(application: Application) {

    fun sendMessageFCM(
        notificationBody: NotificationBody,
        callback: RepositoryCallback<ResponseBody>
    ) {
        val client = RetrofitClient.getInstance(FCM_URL)
        client.sendMessage(notificationBody)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { callback.onSuccess(it) }
                    } else {
                        callback.onFailure("Error Code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailure("Error : ${t.message}")
                }

            })
    }


}