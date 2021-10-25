package com.example.data.model

data class MapModelItem(
    val departureTime: String,
    val hasMyKiTopUp: Boolean,
    val isExpress: Boolean,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val route: String,
    val typeId: Int
)