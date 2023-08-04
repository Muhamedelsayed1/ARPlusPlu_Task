package com.example.arplusplu_task.ui.api

import com.example.arplusplu_task.ui.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//this singleton class to make requests from everywhere in our code
class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            // this to log responses from retrofit that will help us in debugging

            val logging =
                HttpLoggingInterceptor() // this http request will help us to which request we made and what requests are
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }
val api by lazy {
    retrofit.create(NewsApi::class.java)
}
    }
}