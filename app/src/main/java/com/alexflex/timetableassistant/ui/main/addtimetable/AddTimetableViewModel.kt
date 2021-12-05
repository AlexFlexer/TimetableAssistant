package com.alexflex.timetableassistant.ui.main.addtimetable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexflex.timetableassistant.database.TimetableDao
import com.alexflex.timetableassistant.database.TimetableEntity
import com.alexflex.timetableassistant.database.TimetableItem
import com.alexflex.timetableassistant.database.WeekDayTimetable
import com.alexflex.timetableassistant.utils.notifySelf
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

    suspend fun putEntityBack() {
        val entity = item.value ?: return
        mTimetableDao.putTimetableEntity(entity)
    }

    fun addNewItemWithDayIndex(dayIndex: Int, timetableItem: TimetableItem) {
        if (item.value == null) return
        var lessons = item.value!!.lessons
        if (lessons.isNullOrEmpty()) {
            val list = mutableListOf<WeekDayTimetable>()
            for (i in 0..6) list.add(WeekDayTimetable(i, mutableListOf()))
            item.value!!.lessons = list
            lessons = list
        }
        timetableItem.index = lessons.size + 1
        lessons[dayIndex].lectures.add(timetableItem)
        item.notifySelf()
    }
}