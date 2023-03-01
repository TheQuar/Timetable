package uz.quar.timetable.network

sealed class Resource<out T> {
    class Success<out T>(val value: T) : Resource<T>()
    class Failure(val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
