package com.app.flobiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.flobiz.model.dataclass.StackOverflow
import com.app.flobiz.model.repository.QuestionRepository

class BookViewModel(private val bookRepository: QuestionRepository) : ViewModel() {

    private val _bookResponse = MutableLiveData<StackOverflow>()
    val bookResponse: LiveData<StackOverflow>
        get() = _bookResponse


    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String>
        get() = _errorResponse

    fun getQuestions() {
        bookRepository.getQuestions(
            {
                _bookResponse.postValue(it)
            }, {
                _errorResponse.postValue(it)
            })
    }
}

class BookViewModelFactory(private val bookRepository: QuestionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(bookRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}