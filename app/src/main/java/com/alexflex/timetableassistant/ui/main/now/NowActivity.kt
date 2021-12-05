package com.alexflex.timetableassistant.ui.main.now

import com.alexflex.timetableassistant.base.BaseBindingActivity
import com.alexflex.timetableassistant.databinding.ActivityNowBinding

class NowActivity : BaseBindingActivity<ActivityNowBinding>() {
    override fun createBinding(): ActivityNowBinding {
        return ActivityNowBinding.inflate(layoutInflater)
    }
}