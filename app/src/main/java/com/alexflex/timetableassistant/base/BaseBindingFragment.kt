package com.alexflex.timetableassistant.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alexflex.timetableassistant.utils.autoDestroyViewComponent

abstract class BaseBindingFragment<VB : ViewBinding> : Fragment() {

    val binding by autoDestroyViewComponent({
        createBinding()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    abstract fun createBinding(): VB
}