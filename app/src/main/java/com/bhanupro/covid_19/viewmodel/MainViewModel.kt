package com.bhanupro.covid_19.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhanupro.covid_19.model.CovidData
import com.bhanupro.covid_19.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.HttpException


/**
 * Created by Bhanu Prakash Pasupula on 30,Mar-2020.
 * Email: pasupula1995@gmail.com
 */
class MainViewModel(private val api:ApiClient):ViewModel() {

    private val _worldData = MutableLiveData<ArrayList<CovidData>>()
    fun getWorldData():LiveData<ArrayList<CovidData>> = _worldData

    fun loadWorldData(){
        viewModelScope.launch {
            try {
                val res = api.getCovidData()
                _worldData.postValue(res)
            }catch (e:HttpException){
                Log.e("HttpException", "msg: ${e.message}")
            }catch (e:Exception){
                Log.e("Exception", "msg: ${e.message}")
            }
        }
    }
}