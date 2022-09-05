package com.example.nasa.Repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.nasa.Asteroid
import com.example.nasa.DateUtils
import com.example.nasa.api.AsteroidApi
import com.example.nasa.api.PictureOfDay
import com.example.nasa.api.PictureOfDayApi
import com.example.nasa.api.parseAsteroidsJsonResult
import com.example.nasa.database.AsteroidDatabase
import com.example.nasa.database.asDomainModel
import com.example.nasa.network.asDatabaseModel
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*


class AsteroidRepository(private val database: AsteroidDatabase) {

    val startDate = DateUtils.convertDateStringToFormattedString(
        Calendar.getInstance().time,
        Constants.API_QUERY_DATE_FORMAT
    )
    val endDate = DateUtils.convertDateStringToFormattedString(
        DateUtils.addDaysToDate(
            Calendar.getInstance().time,
            7
        ), Constants.API_QUERY_DATE_FORMAT
    )

    suspend fun loadPicOfDay(): PictureOfDay? {
        var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay =
                PictureOfDayApi.PictureOfDayretrofitService.getPictureOfDayAsync().await()
        }
        return if (pictureOfDay.mediaType == "image") {
            pictureOfDay
        } else {
            null
        }
    }

    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroid()) {
            it.asDomainModel()
        }

    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getWeeklyAsteroids(startDate,endDate)) {
            it.asDomainModel()
        }
    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroids(startDate)) {
            it.asDomainModel()
        }


    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            val response = AsteroidApi.AsteroidretrofitService
                .getAsteroidAsync().await()
            val asteroidList = parseAsteroidsJsonResult(JSONObject(response.string()))
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }


}