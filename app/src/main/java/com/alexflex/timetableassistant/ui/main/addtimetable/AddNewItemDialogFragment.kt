package com.alexflex.timetableassistant.ui.main.addtimetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexflex.timetableassistant.database.TimetableItem
import com.alexflex.timetableassistant.databinding.DialogNewTimetableItemBinding
import com.alexflex.timetableassistant.utils.argument
import com.alexflex.timetableassistant.utils.getContent
import com.alexflex.timetableassistant.utils.makeBackgroundTransparent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddNewItemDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogNewTimetableItemBinding? = null
    val binding: DialogNewTimetableItemBinding
        get() = _binding!!

    private val mViewModel: AddTimetableViewModel by sharedViewModel()
    val mIndex: Int by argument()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNewTimetableItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        makeBackgroundTransparent()
        binding.btnOk.setOnClickListener click@{
            if (binding.editLectureName.getContent().isEmpty()) return@click
            mViewModel.addNewItemWithDayIndex(
                mIndex, TimetableItem(
                    binding.editLectureName.getContent(),
                    binding.editLecturer.getContent(),
                    binding.editAuditory.getContent()
                )
            )
            dismiss()
        }
    }
}