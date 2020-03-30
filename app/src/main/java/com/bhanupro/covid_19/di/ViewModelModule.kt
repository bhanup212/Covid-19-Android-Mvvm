package com.bhanupro.covid_19.di

import com.bhanupro.covid_19.viewmodel.MainViewModel
import org.koin.dsl.module


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */

val viewModelModule = module {
    single { MainViewModel(get()) }
}