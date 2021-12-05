package com.alexflex.timetableassistant.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alexflex.timetableassistant.utils.autoDestroyLifecycleComponent

abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity() {

    val binding: VB by autoDestroyLifecycleComponent({
        createBinding()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    abstract fun createBinding(): VB
}