package uz.quar.timetable.network

import retrofit2.http.GET
import retrofit2.http.Query
import uz.quar.timetable.CHECK_UPDATE
import uz.quar.timetable.GET_LESSONS
import uz.quar.timetable.GET_ROOM_LIST
import uz.quar.timetable.models.Data
import uz.quar.timetable.models.LessonsData
import uz.quar.timetable.models.RoomListData

interface API {
    @GET(CHECK_UPDATE)
    suspend fun checkUpdate(@Query("date") date: String) :List<Data>

    @GET(GET_ROOM_LIST)
    suspend fun getRoomList():RoomListData

    @GET(GET_LESSONS)
    suspend fun getLessons(@Query("room_id") roomNumber:Int):List<LessonsData>
}