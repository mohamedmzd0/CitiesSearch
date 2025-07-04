package com.mohamed.domain.repo

import com.mohamed.domain.entity.CityEntity

interface CityRepository {

    suspend fun loadCities(value: String): List<CityEntity>
}