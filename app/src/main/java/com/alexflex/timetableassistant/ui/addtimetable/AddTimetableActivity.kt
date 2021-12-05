package com.alexflex.timetableassistant.ui.addtimetable

import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityAddTimetableBinding

class AddTimetableActivity : BaseBindingActivity<ActivityAddTimetableBinding>() {
    override fun createBinding(): ActivityAddTimetableBinding {
        return ActivityAddTimetableBinding.inflate(layoutInflater)
    }
}