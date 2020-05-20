package com.example.thrive.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thrive.repository.Repository
import com.example.thrive.api.ApiEmptyResponse
import com.example.thrive.api.ApiErrorResponse
import com.example.thrive.api.ApiResponse
import com.example.thrive.api.ApiSuccessResponse
import com.example.thrive.model.Book
import com.example.thrive.model.BookCreateRequest
import com.example.thrive.api.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val mutableBooksResponse = MutableLiveData<Resource<List<Book>>>()

    val booksResponse: LiveData<Resource<List<Book>>>
        get() = mutableBooksResponse

    init {
        mutableBooksResponse.postValue(Resource.loading(null))
    }

    fun getBooks() {
        getBooksHelper()
    }

    fun createBook(body: BookCreateRequest) {
        createBookHelper(body)
    }

    @SuppressLint("CheckResult")
    private fun createBookHelper(body: BookCreateRequest) {
        repository.createBook(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { getBooks() },
                { mutableBooksResponse.postValue(
                        Resource.error("${this.javaClass.name} could not create book!"))}
            )

    }

    @SuppressLint("CheckResult")
    private fun getBooksHelper() {
        mutableBooksResponse.postValue(Resource.loading(arrayListOf()))

        var resource: Resource<List<Book>>

        repository.getBooks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    resource = when (val apiResponse = ApiResponse.create(response)) {
                        is ApiSuccessResponse -> {
                            Resource.success(apiResponse.body)
                        }
                        is ApiEmptyResponse -> {
                            Resource.error("${this.javaClass.name} Empty response from network!")
                        }
                        is ApiErrorResponse -> {
                            Resource.error("${this.javaClass.name} API Error: ${apiResponse.errorMessage}")
                        }
                    }
                    Log.d("API_RESPONSE", "Status: ${resource.status} Message: ${resource.message}")
                    mutableBooksResponse.postValue(resource)
                },
                {error -> Log.d("ERROR", "${this.javaClass.name} Error: ${error.message}")}
            )
    }
}