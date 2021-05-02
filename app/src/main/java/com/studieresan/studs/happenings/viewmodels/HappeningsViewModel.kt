package com.studieresan.studs.happenings.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studieresan.studs.data.models.Happenings

class HappeningsViewModel : ViewModel(){

    val happenings = MutableLiveData<Happenings>()

    fun setHappenings(h: Happenings){
        happenings.value = h
    }
}