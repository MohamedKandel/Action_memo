package com.mkandeel.actionmemo.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {
    private lateinit var apiServices: APIService

    companion object {
        fun getInstance(BASE_URL: String): APIService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}