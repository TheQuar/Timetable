package uz.quar.timetable.utils

import android.content.Context
import android.content.SharedPreferences
import uz.quar.timetable.PREF_NAME

class Prefs(context: Context) {

    enum class PrefKey {
        ROOM_NUMBER, UPDATE_DATE
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var roomNumber: Int
        get() = preferences.getInt(PrefKey.ROOM_NUMBER.name, 0)
        set(value) = preferences.edit().putInt(PrefKey.ROOM_NUMBER.name, value).apply()

    var updateDate: String
        get() = preferences.getString(PrefKey.UPDATE_DATE.name, "23.01.2023") ?: "23.01.2023"
        set(value) = preferences.edit().putString(PrefKey.UPDATE_DATE.name, value).apply()

    fun clear() {
        preferences.edit().clear().apply()
    }
}