package com.alexflex.timetableassistant.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.database.TimetableDao
import com.alexflex.timetableassistant.database.TimetableEntity
import com.alexflex.timetableassistant.databinding.ActivityMainBinding
import com.alexflex.timetableassistant.databinding.DialogTimetableNameBinding
import com.alexflex.timetableassistant.databinding.ItemTimetableBinding
import com.alexflex.timetableassistant.ui.main.addtimetable.AddTimetableActivity
import com.alexflex.timetableassistant.utils.createAndShowDialogWithCustomView
import com.alexflex.timetableassistant.utils.getLayoutInflater
import com.alexflex.timetableassistant.utils.onClick
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val mTimetablesDao: TimetableDao by inject()

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListeners()
        setupDaoObserving()
    }

    private fun setupDaoObserving() {
        mTimetablesDao.getAllTimetables()
            .onEach {
                binding.recyclerTimetables.adapter = TimetablesAdapter(it) { entity ->
                    AddTimetableActivity.startForSpecificTimetable(
                        this@MainActivity, entity.id
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupClickListeners() {
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

class TimetablesAdapter(
    private val mTimetables: List<TimetableEntity>,
    private val mOnItemClicked: (item: TimetableEntity) -> Unit
) : RecyclerView.Adapter<TimetablesAdapter.Holder>() {

    class Holder(val bind: ItemTimetableBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemTimetableBinding.inflate(parent.getLayoutInflater(), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = mTimetables[position]
        holder.bind.apply {
            root.onClick { mOnItemClicked(item) }
            txtTitle.text = item.name
        }
    }

    override fun getItemCount(): Int = mTimetables.size
}