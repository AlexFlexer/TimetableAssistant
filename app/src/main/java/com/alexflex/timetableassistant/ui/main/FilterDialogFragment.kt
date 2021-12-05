package com.alexflex.timetableassistant.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexflex.timetableassistant.databinding.DialogFilterBinding
import com.alexflex.timetableassistant.utils.autoDestroyViewComponent
import com.alexflex.timetableassistant.utils.makeBackgroundTransparent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterDialogFragment : BottomSheetDialogFragment() {
    val binding: DialogFilterBinding by autoDestroyViewComponent({
        DialogFilterBinding.inflate(layoutInflater)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeBackgroundTransparent()
    }
}