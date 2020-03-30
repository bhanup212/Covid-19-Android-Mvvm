package com.bhanupro.covid_19.model


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */
data class CovidData(
    val country:String,
    val cases:Int,
    val todayCases:Int,
    val deaths:Int,
    val todayDeaths:Int,
    val recovered:Int,
    val active:Int,
    val critical:Int,
    val casesPerOneMillion:Double,
    val updated:Long,
    val countryInfo:CountryInfo
){
    data class CountryInfo(
        val _id:Int,
        val iso2:String,
        val iso3:String,
        val lat:Double,
        val long:Double,
        val flag:String
    )
}