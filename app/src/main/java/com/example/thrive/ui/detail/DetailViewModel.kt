package com.example.thrive.ui.detail

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
import com.example.thrive.model.BookUpdateRequest
import com.example.thrive.api.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val mutableBookResponse = MutableLiveData<Resource<Book>>()

    val bookResponse: LiveData<Resource<Book>>
        get() = mutableBookResponse

    init {
        mutableBookResponse.postValue(Resource.loading(null))
    }

    fun getBook(id: String) {
        getBookHelper(id)
    }

    fun updateBook(id: String, body: BookUpdateRequest) {
        updateBookHelper(id, body)
    }

    @SuppressLint("CheckResult")
    private fun updateBookHelper(id: String, body: BookUpdateRequest) {
        repository.updateBook(id, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    when (val apiResponse = ApiResponse.create(response)) {
                        is ApiSuccessResponse -> {
                            getBook(apiResponse.body.id)
                        }
                        is ApiEmptyResponse -> {
                            mutableBookResponse.postValue(
                                Resource.error("${this.javaClass.name} Empty response from network getting single book!")
                            )
                        }
                        is ApiErrorResponse -> {
                            mutableBookResponse.postValue(
                                Resource.error("${this.javaClass.name} API Error: ${apiResponse.errorMessage}")
                            )
                        }
                    }
                },
                {error -> Log.d("ERROR", "${this.javaClass.name} Error: ${error.message}")}
            )
    }

    @SuppressLint("CheckResult")
    private fun getBookHelper(id: String) {
        mutableBookResponse.postValue(Resource.loading(null))

        var resource: Resource<Book>

        repository.getBook(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    resource = when (val apiResponse = ApiResponse.create(response)) {
                        is ApiSuccessResponse -> {
                            Resource.success(apiResponse.body)
                        }
                        is ApiEmptyResponse -> {
                            Resource.error("${this.javaClass.name} Empty response from network getting books!")
                        }
                        is ApiErrorResponse -> {
                            Resource.error("${this.javaClass.name} API Error: ${apiResponse.errorMessage}")
                        }
                    }
                    mutableBookResponse.postValue(resource)
                },
                {error -> Log.d("ERROR", "${this.javaClass.name} Error: ${error.message}")}
            )
    }
}