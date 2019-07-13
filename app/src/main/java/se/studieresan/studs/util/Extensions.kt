package se.studieresan.studs.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

val <T> T.exhaustive
    get() = this

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

