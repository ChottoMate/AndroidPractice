package com.example.dessertclicker.ui

import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert

data class DessertUiState(
    val desserts: List<Dessert> = dessertList,
    val dessertsSold: Int = 0,
    var revenue: Int = 0,
    val currentDessertIndex: Int = 0,
    var currentDessertPrice: Int = dessertList[currentDessertIndex].price,
    var currentDessertImageId: Int = dessertList[currentDessertIndex].imageId
)
