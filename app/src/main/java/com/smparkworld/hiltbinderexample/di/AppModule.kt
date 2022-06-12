package com.smparkworld.hiltbinderexample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
class AppModule {

    @Provides
    @Singleton
    fun provideTestString(): String {
        return "This is test string."
    }
}