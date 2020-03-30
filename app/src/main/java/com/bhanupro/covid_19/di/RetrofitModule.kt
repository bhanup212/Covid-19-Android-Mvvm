package com.bhanupro.covid_19.di

import com.bhanupro.covid_19.constants.ApiUrl
import com.bhanupro.covid_19.network.ApiClient
import com.bhanupro.covid_19.network.RequestConst
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */

val retrofitModule = module {
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    fun provideHttpClient(): OkHttpClient = RequestConst.okHttpAuthClient

    single { provideRetrofit(get()) }
    single { provideHttpClient() }

    fun provideCovidApi(retrofit: Retrofit): ApiClient = retrofit.create(ApiClient::class.java)
    single { provideCovidApi(get()) }
}
