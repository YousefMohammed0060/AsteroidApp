package com.example.nasa.network


import com.example.nasa.database.DatabaseAsteroid
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroid: List<DatabaseAsteroid>)

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroid.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
