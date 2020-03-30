package com.bhanupro.covid_19.extentions

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */

fun Long.toDate():String{
    val date = Date().apply {
        time = this@toDate
    }
    return SimpleDateFormat("dd,MMM-yyyy  HH:mm:ss", Locale.getDefault()).format(date)
}