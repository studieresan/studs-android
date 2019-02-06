package se.studieresan.studs.di

import dagger.Module
import dagger.Provides
import se.studieresan.studs.StudsApplication
import javax.inject.Singleton

@Module
class AppModule(private val application: StudsApplication) {

  @Provides
  @Singleton
  fun provideApplication(): StudsApplication = application
}
