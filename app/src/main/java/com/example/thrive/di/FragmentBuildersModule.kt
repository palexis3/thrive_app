package com.example.thrive.di

import com.example.thrive.ui.main.CreateBookFragment
import com.example.thrive.ui.detail.DetailFragment
import com.example.thrive.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateBookFragment(): CreateBookFragment
}