package com.dorox.pinapp.domain.repository

import android.content.Context


interface CityRepository {

    suspend fun loadCitiesFromFile(): List<String>
}