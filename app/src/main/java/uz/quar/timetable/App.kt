package uz.quar.timetable

import android.app.Application
import uz.quar.timetable.utils.Prefs


val prefs: Prefs by lazy {
    App.prefs!!
}


class App : Application() {
    companion object {
        lateinit var instance: App
            private set

        var prefs: Prefs? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        prefs = Prefs(applicationContext)

    }
}