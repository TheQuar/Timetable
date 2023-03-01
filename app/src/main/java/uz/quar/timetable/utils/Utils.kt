@file:Suppress("DEPRECATION")

package uz.quar.timetable.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import retrofit2.HttpException
import uz.quar.timetable.network.Resource
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun getTime(): String {
    val sdf = SimpleDateFormat("hh - mm")
    return sdf.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun getDayName(): String {
    val sdf = SimpleDateFormat("EEEE")
    return sdf.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun getDayFofMain(): String {
    val sdf = SimpleDateFormat("dd MMM")
    return sdf.format(Date())
}

//todo response da yoki kodda xatolik bor update vaqti to'g'ri kelmayabdi
@SuppressLint("SimpleDateFormat")
fun getDate(time: String): String {
    val date = Date(time.toLong())
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}




fun fullScreen(window: Window) {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)

    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        view.onApplyWindowInsets(windowInsets)
    }
}

fun wakeOn(win: Window) {
    win.addFlags(
        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
    )
}

fun Activity.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    showDialog("Response result", errors(failure), retry)
}

fun errors(failure: Resource.Failure): String {
    return when (failure.throwable) {
        is HttpException -> httError(failure.throwable.code())
        is java.net.SocketTimeoutException -> "Serverdan javob kelmadi"
        is java.net.UnknownHostException -> "Siz boshqa tarmoqdasiz, havfsiz tarmoqqa o'ting"
        else -> failure.throwable.message ?: "Unknown error"
    }
}

fun httError(code: Int): String {
    return when (code) {
        400 -> "Response not success"
        401 -> "Password error"
        404 -> "Not Found page"
        504 -> "Server not worked"
        else -> "Unknown error code:$code"
    }
}


