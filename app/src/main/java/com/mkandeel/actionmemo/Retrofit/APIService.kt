package com.mkandeel.actionmemo.Retrofit

import com.mkandeel.actionmemo.Helper.Constants.CONTENT_TYPE
import com.mkandeel.actionmemo.Helper.Constants.SERVER_KEY
import com.mkandeel.actionmemo.data.NotificationBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type: ${CONTENT_TYPE}",
        "Authorization: ${SERVER_KEY}"
    )
    @POST("fcm/send")
    fun sendMessage(@Body body:NotificationBody) : Call<ResponseBody>
}