package com.app.flobiz.model.repository

import com.app.flobiz.model.dataclass.StackOverflow
import com.app.flobiz.webservices.retrofit.APIServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionRepository(private val apiServices: APIServices) {

    private val TAG = "QuestionRepository"

    fun getQuestions(
        onSuccess: (StackOverflow) -> Unit,
        onError: (String) -> Unit
    ) {
        apiServices.getQuestions().enqueue(object : Callback<StackOverflow> {
            override fun onResponse(call: Call<StackOverflow>, response: Response<StackOverflow>) {
                if (response.isSuccessful && response.body() != null) {
                    Thread {
                        onSuccess(response.body()!!)
                    }.start()
                } else {
                    onError(response.message())
                }
            }

            override fun onFailure(call: Call<StackOverflow>, t: Throwable) {
                onError("Something went wrong")
            }
        })
    }
}