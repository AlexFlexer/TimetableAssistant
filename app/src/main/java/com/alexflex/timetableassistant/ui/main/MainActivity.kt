package com.alexflex.timetableassistant.ui.main

import android.content.Intent
import android.os.Bundle
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityMainBinding
import com.alexflex.timetableassistant.ui.addtimetable.AddTimetableActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}