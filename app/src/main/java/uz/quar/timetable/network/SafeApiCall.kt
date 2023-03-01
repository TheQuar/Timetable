package uz.quar.timetable.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: HttpException) {
                Resource.Failure(throwable)
            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
    }
}