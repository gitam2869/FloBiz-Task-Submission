package com.app.flobiz.webservices.retrofit

import com.app.flobiz.common.Constants
import com.app.flobiz.model.dataclass.StackOverflow
import retrofit2.Call
import retrofit2.http.GET

interface APIServices {

    @GET(Constants.API_GET_STACKOVERFLOW_QUESTIONS)
    fun getQuestions(): Call<StackOverflow>
}