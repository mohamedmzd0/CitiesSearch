package com.mohamed.domain.entity

data class CityEntity(
    val id: Int,
    val displayName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)