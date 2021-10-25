package com.example.data

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.data.model.MapModelItem
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object MapMarkerHelper {

    private var mapModelList = MutableLiveData<List<MapModelItem>>()
    var hasMykiList = LinkedList<MapModelItem>()
    var expressList = LinkedList<MapModelItem>()

    var hasMyKiTopUp: Boolean = false
    private var route: String = ""
    var isExpress: Boolean = false


    @RequiresApi(Build.VERSION_CODES.O)
    fun getMapData(field: String, context: Context): MutableLiveData<List<MapModelItem>> {

//        val jsonObject = JSONObject(dataAsset(context))
//        Log.e("jsonObject", "$jsonObject")

        val jsonArray = JSONArray(dataAsset(context))
        Log.e("jsonArray", "${jsonArray.length()}")

        for (i in 0 until jsonArray.length()) {

            val departureTime = jsonArray.getJSONObject(i).getString("departureTime")

            val inputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val dateTime: LocalDateTime = LocalDateTime.parse(departureTime, inputFormatter)
            val formattedDate: String = dateTime.toString()
            Log.e("departureTime", "$formattedDate")

            val latitude = jsonArray.getJSONObject(i).getDouble("latitude")

            val longitude = jsonArray.getJSONObject(i).getDouble("longitude")

            val name = jsonArray.getJSONObject(i).getString("name")

            val typeId = jsonArray.getJSONObject(i).getInt("typeId")


            var mapModelItem: MapModelItem
            if (jsonArray.getJSONObject(i).has("hasMyKiTopUp") && jsonArray.getJSONObject(i)
                    .has("route")
            ) {
                hasMyKiTopUp = jsonArray.getJSONObject(i).getBoolean("hasMyKiTopUp")
                route = jsonArray.getJSONObject(i).getString("route")
                mapModelItem = MapModelItem(
                    formattedDate,
                    hasMyKiTopUp,
                    isExpress,
                    latitude,
                    longitude,
                    name,
                    route,
                    typeId
                )
                hasMykiList.add(mapModelItem)
            } else if (jsonArray.getJSONObject(i).has("isExpress")) {
                isExpress = jsonArray.getJSONObject(i).getBoolean("isExpress")
                mapModelItem = MapModelItem(
                    formattedDate,
                    hasMyKiTopUp,
                    isExpress,
                    latitude,
                    longitude,
                    name,
                    route,
                    typeId
                )
                expressList.add(mapModelItem)
            }

            Log.e("In isExpress.....", "adding......")
        }

        if (field.equals("isExpress")) {
            mapModelList.value = expressList
            expressList.remove()
            if (hasMykiList.size > 0) {
                hasMykiList.remove()
            }
        } else if (field.equals("hasMyKiTopUp")) {
            mapModelList.value = hasMykiList
            if (expressList.size > 0) {
                expressList.remove()
            }
            hasMykiList.remove()
        } else {
            mapModelList.value = hasMykiList + expressList
            expressList.remove()
            hasMykiList.remove()
        }


        Log.e("lenght......", "${mapModelList.value}")
        return mapModelList
    }

    private fun dataAsset(context: Context): String {

        var start = context.assets.open("data.json").bufferedReader()
        val data = start.use { it.readText() }

        Log.e("In dataAsset()   ", "$data...............")
        return data
    }
}