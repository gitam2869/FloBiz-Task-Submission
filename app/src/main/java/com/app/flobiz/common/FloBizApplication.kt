package com.app.flobiz.common

import android.app.Application
import android.content.Context
import com.app.flobiz.model.repository.QuestionRepository
import com.app.flobiz.webservices.retrofit.RetrofitClientBuilder

class FloBizApplication : Application() {

    private val TAG = "FloBizApplication"

    val bookRepository by lazy { QuestionRepository(RetrofitClientBuilder.buildAPIService()) }

    init {
        instance = this
    }

    companion object {
        private var instance: FloBizApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = FloBizApplication.applicationContext()
    }

}