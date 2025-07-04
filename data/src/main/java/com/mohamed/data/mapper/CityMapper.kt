package com.mohamed.data.mapper

import com.mohamed.data.model.CityResponse
import com.mohamed.domain.entity.CityEntity

class CityMapper {

    fun map(list: List<CityResponse>): List<CityEntity> {
        return list.map { it.toEntity() }
    }

    private fun CityResponse.toEntity(): CityEntity {
        return CityEntity(
            id = _id,
            displayName = name,
            country = country,
            latitude = coord.lat,
            longitude = coord.lon
        )
    }
}
