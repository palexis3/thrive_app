package com.example.thrive.di

import com.example.thrive.api.BookService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://ivy-ios-challenge.herokuapp.com/"

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providerBookService() : BookService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BookService::class.java)
    }
}