package com.studieresan.studs.di

import dagger.Module
import dagger.Provides
import com.studieresan.studs.StudsApplication
import javax.inject.Singleton

@Module
class AppModule(private val application: StudsApplication) {

    @Provides
    @Singleton
    fun provideApplication(): StudsApplication = application
}
