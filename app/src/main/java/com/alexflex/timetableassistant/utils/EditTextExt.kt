package com.alexflex.timetableassistant.utils

import android.widget.EditText

/**
 * Gets the text the user has entered in [this] EditText.
 */
fun EditText.getContent(): String {
    return this.editableText.toString()
}