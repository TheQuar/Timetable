package uz.quar.timetable.vm

import uz.quar.timetable.network.API
import uz.quar.timetable.network.SafeApiCall

class MainRepository(private var apiClient: API) : SafeApiCall {
    suspend fun checkUpdate(date :String) = safeApiCall { apiClient.checkUpdate(date) }
    suspend fun getRoomList() = safeApiCall { apiClient.getRoomList() }
    suspend fun getLessons(roomNumber :Int) = safeApiCall { apiClient.getLessons(roomNumber) }

}