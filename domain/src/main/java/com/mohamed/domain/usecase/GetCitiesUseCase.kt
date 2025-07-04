package com.mohamed.domain.usecase

import com.mohamed.domain.entity.CityEntity
import com.mohamed.domain.repo.CityRepository
import javax.inject.Inject

interface GetCitiesUseCase {
    suspend fun invoke(searchKey: String): List<CityEntity>
}

class GetCitiesUseCaseImpl @Inject constructor(private val repository: CityRepository) :
    GetCitiesUseCase {
    override suspend fun invoke(searchKey: String) = repository.loadCities(searchKey)
}