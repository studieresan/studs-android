package se.studieresan.studs.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import se.studieresan.studs.data.StudsService
import javax.inject.Singleton

@Module
class ServiceModule {

  @Provides
  @Singleton
  fun provideStudsService(retrofit: Retrofit): StudsService = retrofit.create(StudsService::class.java)
}
