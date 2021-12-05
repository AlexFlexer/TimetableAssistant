package com.alexflex.timetableassistant.ui.main.addtimetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexflex.timetableassistant.base.BaseBindingFragment
import com.alexflex.timetableassistant.databinding.FragmentWeekdayTimetableBinding
import com.alexflex.timetableassistant.databinding.ItemTimetableItemBinding
import com.alexflex.timetableassistant.utils.createBundleAndPut
import com.alexflex.timetableassistant.utils.visibleIf
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimetableContentFragment : BaseBindingFragment<FragmentWeekdayTimetableBinding>() {

    private val mViewModel: AddTimetableViewModel by sharedViewModel()
    var mIndex: Int = -1

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentWeekdayTimetableBinding {
        return FragmentWeekdayTimetableBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObserving()
        binding.btnAddTimetableItem.setOnClickListener {
            AddNewItemDialogFragment().also {
                arguments = createBundleAndPut(AddNewItemDialogFragment::mIndex to mIndex)
            }.show(childFragmentManager, "jnviruvbiejbijcbuvie")
        }
    }

    private fun setupObserving() {
        mViewModel.item.observe(viewLifecycleOwner) { entity ->
            binding.layoutLectures.removeAllViews()
            val weekdayTimetable = entity.lessons[mIndex].lectures
            binding.btnAddTimetableItem.visibleIf(weekdayTimetable.size < 10)
            weekdayTimetable.forEach { weekdayLesson ->
                val itemBinding = ItemTimetableItemBinding.inflate(layoutInflater, null, false)
                itemBinding.apply {
                    txtLectureOrder.text = weekdayLesson.index.toString()
                    txtLectureName.text = weekdayLesson.name
                    txtLectureLecturer.text = weekdayLesson.description
                    txtLectureAuditory.text = weekdayLesson.auditory
                }
                binding.layoutLectures.addView(itemBinding.root)
            }
        }
    }
}