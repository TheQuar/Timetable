package uz.quar.timetable.main

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import uz.quar.timetable.R
import uz.quar.timetable.database.AppDatabase
import uz.quar.timetable.databinding.ActivityMainBinding
import uz.quar.timetable.models.LessonsData
import uz.quar.timetable.network.RetrofitInstance
import uz.quar.timetable.prefs
import uz.quar.timetable.utils.fullScreen
import uz.quar.timetable.utils.wakeOn
import uz.quar.timetable.vm.MainRepository
import uz.quar.timetable.vm.MainViewModel
import uz.quar.timetable.vm.MainViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set view
        fullScreen(window)

        wakeOn(window)
        wakeLock()

        addSlider()

        // init view model
        val viewModelProviderFactory =
            MainViewModelFactory(
                AppDatabase.invoke(this),
                MainRepository(RetrofitInstance.api),
                this
            )
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[(MainViewModel::class.java)]

        //fun
        observe()

        viewModel.checkConn()
        viewModel.start()

    }

    private fun addSlider() {
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.prm_1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.prm_2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.prm_3, ScaleTypes.CENTER_CROP))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

    }

    private fun addItem(lesson: LessonsData?): View {
        val view =
            LayoutInflater.from(this).inflate(R.layout.table_row_layout, null, false)

        val number = view.findViewById<TextView>(R.id.tv_lesson_number)
        val name = view.findViewById<TextView>(R.id.tv_lesson_name)
        val time = view.findViewById<TextView>(R.id.tv_lesson_time)
        val teacher = view.findViewById<TextView>(R.id.tv_lesson_teacher)

        lesson?.let {
            number.text = it.class_name.replace("GRE EN", "GREEN")
            name.text = it.subject_name
            time.text = it.lesson_duration.trim()
            teacher.text = it.teacher_name.replace(" - ${prefs.roomNumber}", "").replace(
                prefs.roomNumber.toString(), ""
            )
        }

        return view
    }

    private fun observe() {
        viewModel.lessonsData.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvTitle.visibility = View.GONE
                binding.tableViewMain.visibility = View.GONE
                binding.tableRow.visibility = View.GONE
            } else {
                binding.tvMessage.visibility = View.GONE
                binding.tvTitle.visibility = View.VISIBLE
                binding.tableViewMain.visibility = View.VISIBLE
                binding.tableRow.visibility = View.VISIBLE

                binding.tableViewMain.removeAllViews()
                it.sortedBy { it?.lesson_no }.map {
                    binding.tableViewMain.addView(addItem(it))

                }
            }
        }
    }


    private fun wakeLock() {
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                acquire()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.release()
    }
}