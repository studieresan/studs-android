package com.studieresan.studs.di

import dagger.Component
import com.studieresan.studs.events.views.EventDetailActivity
import com.studieresan.studs.events.views.EventsFragment
import com.studieresan.studs.login.views.ForgotPasswordActivity
import com.studieresan.studs.login.views.LoginActivity
import com.studieresan.studs.StudsWidget
import com.studieresan.studs.happenings.HappeningsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, ServiceModule::class])
interface StudsApplicationComponent {
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventDetailActivity: EventDetailActivity)
    fun inject(studsWidget: StudsWidget)
    fun inject(happeningsFragment: HappeningsFragment)
}
