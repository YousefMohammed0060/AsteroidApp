package com.example.nasa.api


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()



interface PictureOfDayApiServes {
    @GET("planetary/apod?api_key=WydaF1fsEphRxH00GA7XNWAZo5afX94lakAlxwaF")
    fun getPictureOfDayAsync():
            Deferred<PictureOfDay>
}


object PictureOfDayApi {
    val PictureOfDayretrofitService: PictureOfDayApiServes by lazy {
        retrofit.create(PictureOfDayApiServes::class.java)
    }
}

interface AsteroidApiServes {
    @GET("neo/rest/v1/feed")
    fun getAsteroidAsync(
        @Query("api_key") apiKey: String =Constants.API_KEY
    ):Deferred<ResponseBody>
}


object AsteroidApi {
    val AsteroidretrofitService: AsteroidApiServes by lazy {
        retrofit.create(AsteroidApiServes::class.java)
    }
}
