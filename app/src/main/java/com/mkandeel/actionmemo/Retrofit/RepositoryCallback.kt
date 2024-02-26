package com.mkandeel.actionmemo.Retrofit

import okhttp3.ResponseBody
import retrofit2.Callback

interface RepositoryCallback<T>{
    fun onSuccess(response: T)
    fun onFailure(message: String)
}