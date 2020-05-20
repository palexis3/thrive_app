package com.example.thrive.model

import com.google.gson.annotations.SerializedName

data class Book(
    @field:SerializedName("author")
    var author: String,
    @field:SerializedName("categories")
    var categories: String,
    @field:SerializedName("publisher")
    var publisher: String,
    @field:SerializedName("title")
    var title: String,
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("lastCheckedOut")
    var lastCheckedOut: String,
    @field:SerializedName("lastCheckedOutBy")
    var lastCheckedOutBy: String
)

data class BookUpdateRequest(
    @field:SerializedName("lastCheckedOutBy")
    var lastCheckedOutBy: String
)

data class BookCreateRequest(
    @field:SerializedName("title")
    var title: String,
    @field:SerializedName("author")
    var author: String,
    @field:SerializedName("categories")
    var categories: String,
    @field:SerializedName("publisher")
    var publisher: String
)