package uz.quar.timetable.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.quar.timetable.BASE_URL
import uz.quar.timetable.BuildConfig
import java.util.concurrent.TimeUnit


class RetrofitInstance {
    companion object {
        //lazy
        private val retrofit by lazy {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(15, TimeUnit.SECONDS) // write timeout
                .readTimeout(15, TimeUnit.SECONDS) // read timeout
                .addInterceptor(getHttpLogger())

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()

        }

        private fun getHttpLogger(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            }
        }

        val api: API by lazy {
            retrofit.create(API::class.java)
        }

    }
}

