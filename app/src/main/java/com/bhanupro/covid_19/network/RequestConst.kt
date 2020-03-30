package com.bhanupro.covid_19.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */
object RequestConst{
    val okHttpAuthClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .build()
}