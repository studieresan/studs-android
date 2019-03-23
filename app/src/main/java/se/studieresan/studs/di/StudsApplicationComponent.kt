package se.studieresan.studs.di

import dagger.Component
import se.studieresan.studs.events.views.EventDetailActivity
import se.studieresan.studs.events.views.EventsFragment
import se.studieresan.studs.events.views.PostEventFormActivity
import se.studieresan.studs.events.views.PreEventFormActivity
import se.studieresan.studs.login.views.ForgotPasswordActivity
import se.studieresan.studs.login.views.LoginActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, ServiceModule::class])
interface StudsApplicationComponent {
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventDetailActivity: EventDetailActivity)
    fun inject(preEventFormActivity: PreEventFormActivity)
    fun inject(postEventFormActivity: PostEventFormActivity)
}
