package com.example.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.MapMarkerHelper
import com.example.data.model.MapModelItem
import com.example.data.repo.MapRepository

class MapViewModel() : ViewModel() {

    val mapRepository = MapRepository(MapMarkerHelper)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMapData(field: String, context: Context): MutableLiveData<List<MapModelItem>> {
        return mapRepository.getMapData(field, context)
    }
}