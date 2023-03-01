package uz.quar.timetable.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.quar.timetable.WIFI_NAME
import uz.quar.timetable.WIFI_PASSWORD
import uz.quar.timetable.database.AppDatabase
import uz.quar.timetable.models.Data
import uz.quar.timetable.models.LessonsData
import uz.quar.timetable.models.RoomListData
import uz.quar.timetable.network.Resource
import uz.quar.timetable.prefs
import uz.quar.timetable.utils.WifiUtils
import uz.quar.timetable.utils.getDayName


class MainViewModel(
    private val database: AppDatabase,
    private val repository: MainRepository,
    context: Context,
) :
    ViewModel() {
    private val wifiUtils = WifiUtils(context, WIFI_NAME, WIFI_PASSWORD)

    var updateData = MutableLiveData<Resource<List<Data>>>()
    var roomListData = MutableLiveData<Resource<RoomListData>>()
    var lessonsData = MutableLiveData<List<LessonsData?>>()

    fun checkConn(): Boolean {
        if (wifiUtils.checkConnection()) {
            return true
        } else
            if (wifiUtils.connectToNetworkWPA()) {
                return true
            }
        return false
    }

    fun checkUpdate() = viewModelScope.launch {
        updateData.value = Resource.Loading
        updateData.value = repository.checkUpdate(prefs.updateDate)
    }

    fun getRoomList() = viewModelScope.launch {
        roomListData.value = Resource.Loading
        roomListData.value = repository.getRoomList()
    }

    fun getLessons(roomNumber: Int) = viewModelScope.launch {
        when (val result = repository.getLessons(roomNumber)) {
            is Resource.Failure -> {
                Log.d("Quar_log", result.throwable.message ?: "not found error")
            }
            is Resource.Success -> {
                dataToDB(result.value)
            }
            else -> {}
        }
    }

    private fun dataToDB(lessons: List<LessonsData>) {
        database.dicDao().insert(lessons)
    }

    private fun getLessons(day: String) = viewModelScope.launch {
        lessonsData.value = database.dicDao().getLessons(day)
    }

    fun start() = viewModelScope.launch {
        while (true) {
            getLessons(getDayName())
            delay(3600000)
        }
    }
}