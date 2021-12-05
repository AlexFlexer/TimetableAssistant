package com.alexflex.timetableassistant.ui.main.addtimetable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alexflex.timetableassistant.R
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityAddTimetableBinding
import com.alexflex.timetableassistant.utils.createBundleAndPut
import com.alexflex.timetableassistant.utils.extra

class AddTimetableActivity : BaseBindingActivity<ActivityAddTimetableBinding>() {

    companion object {
        fun startForNamedTimetable(context: Context, timetableName: String) {
            context.startActivity(Intent(context, AddTimetableActivity::class.java).also {
                createBundleAndPut(AddTimetableActivity::mTimetableName to timetableName)
            })
        }
    }

    val mTimetableName: String by extra()

    override fun createBinding(): ActivityAddTimetableBinding {
        return ActivityAddTimetableBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.text = getString(R.string.main_add_timetable_title, mTimetableName)
    }
}