package com.studieresan.studs.happenings.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HappeningsViewModel : ViewModel(){

    val happenings = MutableLiveData<List<HappeningsQuery.Happening>>()

    fun setHappenings(h: List<HappeningsQuery.Happening>?){
        happenings.value = h
    }
}