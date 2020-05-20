package com.example.thrive.repository

import com.example.thrive.api.BookService
import com.example.thrive.model.BookCreateRequest
import com.example.thrive.model.BookUpdateRequest
import javax.inject.Inject

class Repository @Inject constructor(private val client: BookService) {

    fun getBooks() = client.getBooks()

    fun getBook(id: String) = client.getBook(id)

    fun updateBook(id: String, body: BookUpdateRequest) = client.putBook(id, body)

    fun createBook(body: BookCreateRequest) = client.createBook(body)
}