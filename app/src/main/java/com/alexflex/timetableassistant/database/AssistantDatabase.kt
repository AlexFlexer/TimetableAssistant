package com.alexflex.timetableassistant.database

import androidx.room.*
import com.alexflex.timetableassistant.utils.parseType
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

const val TIMETABLE_TABLE = "tt_table"

// this class represents item in the timetable
data class TimetableItem(
    val index: Int,
    val name: String,
    val description: String,
    val auditory: String
)

data class WeekDayTimetable(
    val weekdayIndex: Int,
    val lectures: TimetableItem
)

@Entity(tableName = TIMETABLE_TABLE)
class TimetableEntity(
    val name: String,
    val lessons: List<WeekDayTimetable>,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Dao
interface TimetableDao {
    @Query("select * from tt_table")
    suspend fun getAllTimetables(): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putTimetableEntity(entity: TimetableEntity)

    @Query("select * from tt_table where id = :id")
    suspend fun selectById(id: Int)
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
        return json.parseType(gson) ?: emptyList()
    }
}