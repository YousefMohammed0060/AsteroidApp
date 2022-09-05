package com.example.nasa.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nasa.Asteroid

@Dao
interface AsteroidDao {

    @Query("select * from asteroid_table order by closeApproachDate desc")
    fun getAllAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate BETWEEN :startDate  AND :endDate  order by closeApproachDate desc")
    fun getWeeklyAsteroids(startDate: String, endDate: String) : LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :today")
    fun getTodayAsteroids(today: String) : LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

}

private lateinit var INSTANCE: AsteroidDatabase
fun getDatabase(context: Context): AsteroidDatabase {

    if (!::INSTANCE.isInitialized) {
        INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            AsteroidDatabase::class.java,
            "asteroid"
        ).build()
    }

    return INSTANCE
}
