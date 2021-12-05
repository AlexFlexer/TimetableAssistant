package com.alexflex.timetableassistant

import android.app.Application
import androidx.room.Room
import com.alexflex.timetableassistant.database.TimetableDatabase
import com.alexflex.timetableassistant.ui.main.addtimetable.AddTimetableViewModel
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

const val DATABASE_NAME = "tt_db"

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), TimetableDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()
    }
    single { get<TimetableDatabase>().timetableDao() }
    single { GsonBuilder()/* Setup what you need*/.create() }
}

val viewModelModule = module { viewModel { AddTimetableViewModel(get()) } }

class ThisApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ThisApp)
            modules(databaseModule, viewModelModule)
        }
    }
}