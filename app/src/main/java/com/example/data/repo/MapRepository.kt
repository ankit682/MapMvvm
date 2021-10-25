package com.example.data.repo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.data.MapMarkerHelper
import com.example.data.model.MapModelItem

class MapRepository(private val mapMarkerHelper: MapMarkerHelper) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMapData(field: String, context: Context): MutableLiveData<List<MapModelItem>> {
        return mapMarkerHelper.getMapData(field, context)
    }
}