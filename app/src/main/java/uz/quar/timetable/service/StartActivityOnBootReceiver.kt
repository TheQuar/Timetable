package uz.quar.timetable.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import uz.quar.timetable.splash.SplashScreenActivity

class StartActivityOnBootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            if (Intent.ACTION_BOOT_COMPLETED.equals(it.action)) {
                p0?.let { context->
                    val i = Intent(p0, SplashScreenActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(i)
                }
            }
        }
    }
}