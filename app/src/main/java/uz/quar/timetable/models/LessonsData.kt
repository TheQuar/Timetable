package uz.quar.timetable.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LessonTable")
data class LessonsData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lesson_no: String,
    val lesson_day: String,
    val teacher_name: String,
    val subject_name: String,
    val lesson_duration: String,
    val room_number: String,
    val class_name: String,
    val updated_at: String,
)