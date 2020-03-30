package com.bhanupro.covid_19

import android.app.Application
import com.bhanupro.covid_19.di.retrofitModule
import com.bhanupro.covid_19.di.viewModelModule
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: bhanu.prakash@loansimple.in
 */
class CovidApp:Application() {

    private lateinit var mFireAnalytics:FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        mFireAnalytics = FirebaseAnalytics.getInstance(this)
        startKoin()
    }
    private fun startKoin(){
        org.koin.core.context.startKoin {
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            modules (retrofitModule , viewModelModule)
        }
    }
}