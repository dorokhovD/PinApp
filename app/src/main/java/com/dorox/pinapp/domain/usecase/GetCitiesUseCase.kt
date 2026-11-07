package com.dorox.pinapp.domain.usecase

import com.dorox.pinapp.domain.entity.Post
import com.dorox.pinapp.domain.repository.CityRepository
import com.dorox.pinapp.domain.repository.PostsRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {

    suspend operator fun invoke(): List<String> {
       return repository.loadCitiesFromFile()
    }
}