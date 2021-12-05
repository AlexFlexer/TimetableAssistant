package com.alexflex.timetableassistant.ui.main.addtimetable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexflex.timetableassistant.database.TimetableDao
import com.alexflex.timetableassistant.database.TimetableEntity
import kotlinx.coroutines.launch

class AddTimetableViewModel(
    private val mTimetableDao: TimetableDao
) : ViewModel() {
    val item = MutableLiveData<TimetableEntity>()

    fun createNewEntity(name: String) {
        item.value = TimetableEntity(name, emptyList())
    }

    fun fetchEntityById(id: Int) {
        viewModelScope.launch {
            item.value = mTimetableDao.selectById(id) ?: return@launch
        }
    }
}