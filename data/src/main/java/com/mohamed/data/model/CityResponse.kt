package com.mohamed.data.model

data class CityResponse(
    val country: String,
    val name: String,
    val _id: Int,
    val coord: CoordResponse
)

data class CoordResponse(
    val lat: Double,
    val lon: Double,
)
