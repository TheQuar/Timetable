package uz.quar.timetable.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.quar.timetable.database.AppDatabase

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val database: AppDatabase,
    private val repository: MainRepository,
    private val context: Context,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(database, repository, context) as T
    }
}