package com.app.flobiz.utility

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtility {

    companion object{

        private val TAG = "DateTimeUtility"
        const val QUESTION_POSTED_DATE_PATTERN = "dd-MM-yyyy"

        @SuppressLint("SimpleDateFormat")
        fun getDateFromTime(pattern: String, time: Long): String {
            val formatter = SimpleDateFormat(pattern)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(Date(time))
        }
    }
}