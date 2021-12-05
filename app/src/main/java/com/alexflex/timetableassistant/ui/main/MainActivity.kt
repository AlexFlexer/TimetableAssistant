package com.alexflex.timetableassistant.ui.main

import android.content.Intent
import android.os.Bundle
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityMainBinding
import com.alexflex.timetableassistant.ui.main.addtimetable.AddTimetableActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    companion object {
        @JvmStatic
        val TAG = MainActivity::class.java.name
    }

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddTimetableActivity::class.java))
            }
            btnFilter.setOnClickListener {
                FilterDialogFragment().show(supportFragmentManager, TAG)
            }
        }
    }
}