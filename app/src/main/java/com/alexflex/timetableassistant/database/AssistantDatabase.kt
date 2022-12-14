package com.alexflex.timetableassistant.database

import androidx.room.*
import com.alexflex.timetableassistant.utils.parseType
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

const val TIMETABLE_TABLE = "tt_table"

// this class represents item in the timetable
data class TimetableItem(
    val name: String,
    val description: String,
    val auditory: String,
    var index: Int = 0
)

data class WeekDayTimetable(
    val weekdayIndex: Int,
    val lectures: MutableList<TimetableItem>
)

@Entity(tableName = TIMETABLE_TABLE)
class TimetableEntity(
    val name: String,
    var lessons: List<WeekDayTimetable>,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Dao
interface TimetableDao {
    @Query("select * from tt_table")
    fun getAllTimetables(): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putTimetableEntity(entity: TimetableEntity)

    @Query("select * from tt_table where id = :id")
    suspend fun selectById(id: Int): TimetableEntity?
}

@Database(entities = [TimetableEntity::class], version = 1)
@TypeConverters(WeekDayTimetableConverter::class)
abstract class TimetableDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
}

class WeekDayTimetableConverter {

    @TypeConverter
    fun fromWeekdayTimetable(list: List<WeekDayTimetable>): String {
        if (list.isEmpty()) return ""
        val gson by inject<Gson>(Gson::class.java)
        return gson.toJson(list)
    }

    @TypeConverter
    fun toWeekdayTimetable(json: String?): List<WeekDayTimetable> {
        if (json.isNullOrBlank()) return emptyList()
        val gson by inject<Gson>(Gson::class.java)
        return json.parseType<WeekDayTimetableWrapper>(gson) ?: emptyList()
    }
}

class WeekDayTimetableWrapper : ArrayList<WeekDayTimetable>()