package se.studieresan.studs

import io.reactivex.Scheduler

interface BaseView {
    val mainScheduler: Scheduler
}