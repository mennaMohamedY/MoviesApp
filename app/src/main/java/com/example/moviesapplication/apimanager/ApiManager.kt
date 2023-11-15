package com.example.moviesapplication.apimanager

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object{
        private var retrofit : Retrofit?=null

        private fun getInstance():Retrofit{
            if (retrofit == null){

                Log.e("TAG", "Retrofit == null")
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client: OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

                retrofit= Retrofit.Builder().client(client)
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }


            return retrofit!!
        }

        fun getServices():WebServices{
           return getInstance().create(WebServices::class.java)

        }
    }

}