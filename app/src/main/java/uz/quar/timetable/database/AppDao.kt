package uz.quar.timetable.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import uz.quar.timetable.models.LessonsData

@Dao
interface AppDao {
    @Insert(onConflict = REPLACE)
    fun insert(lessonsData: List<LessonsData>)

    @Query("SELECT * FROM LessonTable WHERE lesson_day=:day")
    suspend fun getLessons(day: String): List<LessonsData>?
}