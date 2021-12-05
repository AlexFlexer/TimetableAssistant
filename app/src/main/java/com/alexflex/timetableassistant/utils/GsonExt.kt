package com.alexflex.timetableassistant.utils

import com.google.gson.Gson

inline fun <reified T> String.parseType(gson: Gson): T? {
    return try {
        gson.fromJson(this, T::class.java)!!
    } catch (e: Exception) {
        null
    }
}