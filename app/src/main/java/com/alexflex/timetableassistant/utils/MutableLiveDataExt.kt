package com.alexflex.timetableassistant.utils

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifySelf() {
    value = value
}