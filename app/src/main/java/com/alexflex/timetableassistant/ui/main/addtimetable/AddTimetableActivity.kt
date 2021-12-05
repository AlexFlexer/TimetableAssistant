package com.alexflex.timetableassistant.ui.main.addtimetable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexflex.timetableassistant.R
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityAddTimetableBinding
import com.alexflex.timetableassistant.utils.createBundleAndPut
import com.alexflex.timetableassistant.utils.extraNullable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTimetableActivity : BaseBindingActivity<ActivityAddTimetableBinding>() {

    companion object {
        fun startForNamedTimetable(context: Context, timetableName: String) {
            context.startActivity(Intent(context, AddTimetableActivity::class.java).also {
                createBundleAndPut(AddTimetableActivity::mTimetableName to timetableName)
            })
        }

        fun startForSpecificTimetable(context: Context, id: Int) {
            context.startActivity(Intent(context, AddTimetableActivity::class.java).also {
                createBundleAndPut(AddTimetableActivity::mTimetableId to id)
            })
        }
    }

    private val mViewModel: AddTimetableViewModel by viewModel()
    val mTimetableName: String? by extraNullable()
    val mTimetableId: Int? by extraNullable()

    override fun createBinding(): ActivityAddTimetableBinding {
        return ActivityAddTimetableBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mTimetableId == null) mViewModel.fetchEntityById(mTimetableId ?: 0)
        else mViewModel.createNewEntity(mTimetableName.orEmpty())
        mViewModel.item.observe(this) {
            binding.apply {
                toolbar.text =
                    getString(R.string.main_add_timetable_title, mTimetableName.orEmpty())
                pagerWeekdays.adapter = PagerAdapter(this@AddTimetableActivity)
                TabLayoutMediator(tabsWeekdays, pagerWeekdays) { tab, positon ->
                }
            }
        }
        binding.btnDone.setOnClickListener {
            lifecycleScope.launch {
                mViewModel.putEntityBack()
                finish()
            }
        }
    }
}

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return TimetableContentFragment().apply {
            mIndex = position
        }
    }
}