package com.example.nasa.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.nasa.Asteroid
import com.example.nasa.Repository.AsteroidRepository
import com.example.nasa.api.PictureOfDay
import com.example.nasa.database.DatabaseAsteroid
import com.example.nasa.database.getDatabase
import kotlinx.coroutines.*
import java.lang.Exception

enum class Filter(val value: String) { SHOW_SAVED("saved"), SHOW_TODAY("today"), SHOW_WEEK("week") }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _PicOfDay = MutableLiveData<PictureOfDay>()
    val PicOfDay: LiveData<PictureOfDay>
        get() = _PicOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidDetailsNavigated() {
        _navigateToAsteroidDetails.value = null
    }

    init {
        viewModelScope.launch{
            try{
                _PicOfDay.value = asteroidRepository.loadPicOfDay()
                asteroidRepository.refreshData()
            }catch (e :Exception){
                Toast.makeText(application,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val filter = MutableLiveData(Filter.SHOW_SAVED)
    val list = Transformations.switchMap(filter){
        when (it) {
            Filter.SHOW_TODAY -> asteroidRepository.asteroidsToday
            Filter.SHOW_WEEK -> asteroidRepository.asteroidsWeek
            else -> asteroidRepository.allAsteroids
        }
    }

    fun updateFilter(filter: Filter) {
        this.filter.value = filter
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}