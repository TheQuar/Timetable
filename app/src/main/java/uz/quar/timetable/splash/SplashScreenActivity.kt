package uz.quar.timetable.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import uz.quar.timetable.R
import uz.quar.timetable.database.AppDatabase
import uz.quar.timetable.main.MainActivity
import uz.quar.timetable.network.Resource
import uz.quar.timetable.network.RetrofitInstance
import uz.quar.timetable.prefs
import uz.quar.timetable.utils.fullScreen
import uz.quar.timetable.utils.getDate
import uz.quar.timetable.utils.handleApiError
import uz.quar.timetable.utils.loaderDialog
import uz.quar.timetable.vm.MainRepository
import uz.quar.timetable.vm.MainViewModel
import uz.quar.timetable.vm.MainViewModelFactory


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var AUTO_RUN_BOOT = 1133
    private var RECEIVE_BOOT_COMPLETED = Manifest.permission.RECEIVE_BOOT_COMPLETED

    private var splashTime = 3000L
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val viewModelProviderFactory =
            MainViewModelFactory(
                AppDatabase.invoke(this),
                MainRepository(RetrofitInstance.api),
                applicationContext
            )
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[(MainViewModel::class.java)]


        fullScreen(window)

        observe()
        if (prefs.roomNumber == 0) {
            if (viewModel.checkConn()) viewModel.checkUpdate()
        } else checkPermission()
    }

    private fun startHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                RECEIVE_BOOT_COMPLETED
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_WIFI_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) startHandler() else {
            if ("xiaomi".equals(Build.MANUFACTURER, ignoreCase = true))
                autoRunXiaomiDialog()
            else
                sendRequest(arrayOf(RECEIVE_BOOT_COMPLETED))
        }
    }

    private fun sendRequest(permission: Array<String>) {
        ActivityCompat.requestPermissions(this, permission, AUTO_RUN_BOOT)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AUTO_RUN_BOOT ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    checkPermission() else startHandler()
        }
    }

    private fun autoRunXiaomiDialog() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this@SplashScreenActivity).create()
        alertDialog.setTitle("Alert")
        alertDialog.setMessage(resources.getString(R.string.app_name) + "You agree to add the program to autorun!")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Yes"
        ) { dialog, which ->
            dialog.dismiss()
            autoRunXiaomi()
        }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "No"
        ) { dialog, which ->
            dialog.dismiss()
            checkPermission()
        }

        alertDialog.show()
    }

    private fun autoRunXiaomi() {
        val intent = Intent()
        intent.component = ComponentName(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
        startActivity(intent)
    }


    private fun observe() {
        viewModel.updateData.observe(this) {
            loaderDialog(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    prefs.updateDate =
                        getDate(it.value[0].updated_at)
                    viewModel.getRoomList()
                }
                is Resource.Failure -> handleApiError(it)
                else -> {}
            }
        }

        viewModel.roomListData.observe(this) {
            loaderDialog(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    dialog(it.value.data)
                }
                is Resource.Failure -> handleApiError(it)
                else -> {}
            }
        }
    }

    private fun dialog(list: List<String>) {
        val builderSingle = AlertDialog.Builder(this@SplashScreenActivity)
        builderSingle.setTitle("Xona raqamini tanlang")

        val arrayAdapter =
            ArrayAdapter(
                this@SplashScreenActivity,
                android.R.layout.select_dialog_singlechoice,
                list
            )

        builderSingle.setAdapter(arrayAdapter) { _, which ->
            val roomNumber = arrayAdapter.getItem(which)

            roomNumber?.let {
                prefs.roomNumber = it.toInt()
                viewModel.getLessons(it.toInt())
            }
            startHandler()
        }
        builderSingle.show()
    }


}