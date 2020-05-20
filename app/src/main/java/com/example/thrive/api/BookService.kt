package com.example.thrive.api

import com.example.thrive.model.Book
import com.example.thrive.model.BookCreateRequest
import com.example.thrive.model.BookUpdateRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface BookService  {

    @GET("/books")
    fun getBooks():Observable<Response<List<Book>>>

    @GET("/books/{id}")
    fun getBook(@Path("id") id: String) : Observable<Response<Book>>

    @PUT("/books/{id}")
    fun putBook(@Path("id") id: String, @Body body: BookUpdateRequest): Single<Response<Book>>

    @POST("/books")
    fun createBook(@Body body: BookCreateRequest): Completable
}