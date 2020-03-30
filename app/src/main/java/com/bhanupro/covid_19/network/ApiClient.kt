package com.bhanupro.covid_19.network

import com.bhanupro.covid_19.constants.ApiUrl
import com.bhanupro.covid_19.model.CovidData
import retrofit2.http.GET


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */
interface ApiClient {

    @GET(ApiUrl.URL_CONSTANTS)
    suspend fun getCovidData(): ArrayList<CovidData>

}