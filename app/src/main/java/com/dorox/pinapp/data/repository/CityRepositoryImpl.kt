package com.dorox.pinapp.data.repository

import android.content.Context
import com.dorox.pinapp.domain.repository.CityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CityRepository {

    private var cache: List<String>? = null

    override suspend fun loadCitiesFromFile(): List<String> = withContext(Dispatchers.IO) {
        cache ?: context.assets.open("cities_with_regions.txt")
            .bufferedReader()
            .readLines()
            .map {
                it.trim()
            }
            .filter {
                it.isNotBlank()
            }
            .sorted()
            .also {
                cache = it
            }
    }
}