package com.alexflex.timetableassistant.ui.main.addtimetable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexflex.timetableassistant.database.TimetableEntity

class AddTimetableViewModel : ViewModel() {
    val item = MutableLiveData<TimetableEntity>()
}