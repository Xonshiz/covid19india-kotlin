package com.xonshiz.covid_19tracker.interfaces

import com.xonshiz.covid_19tracker.models.MainDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidRestService {
    @GET("/data.json")
    fun getMainData(): Call<MainDataModel>
}