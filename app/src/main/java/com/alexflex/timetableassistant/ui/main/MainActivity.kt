package com.alexflex.timetableassistant.ui.main

import android.os.Bundle
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityMainBinding
import com.alexflex.timetableassistant.databinding.DialogTimetableNameBinding
import com.alexflex.timetableassistant.ui.main.addtimetable.AddTimetableActivity
import com.alexflex.timetableassistant.utils.createAndShowDialogWithCustomView

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnAdd.setOnClickListener {
            createAndShowDialogWithCustomView(
                DialogTimetableNameBinding.inflate(layoutInflater),
                bindingSetup = { binding, dialog ->
                    binding.btnOk.setOnClickListener click@{
                        val name = binding.editTimetableName.text
                        if (name.isNullOrBlank()) return@click
                        AddTimetableActivity.startForNamedTimetable(this, name.toString())
                    }
                }
            )
        }
    }
}