package com.example.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.data.model.MapModelItem
import com.example.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity() {

    lateinit var gMap: GoogleMap
    lateinit var context: Context

    lateinit var spinner: Spinner

    val spinnerList = ArrayList<String>()

    lateinit var mapViewModel: MapViewModel

    lateinit var mapData: List<MapModelItem>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        context = this

        spinner = findViewById(R.id.spinner)
        spinnerList.add("hasMyKiTopUp")
        spinnerList.add("isExpress")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        openMap(mapViewModel, "")
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                Log.e("onItemSelected()", "${item.toString()}")
                openMap(mapViewModel, item as String)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openMap(mapViewModel: MapViewModel, field: String) {
        Log.e("In openMap()", "1")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            gMap = googleMap
            mapViewModel.getMapData(field, context).observe(this, { it ->
                mapData = it

                Log.e("Lenght of mapData...", "${mapData.size}")
                mapData.forEach { mapModelItem ->

                    val marker = LatLng(mapModelItem.latitude, mapModelItem.longitude)
                    gMap.addMarker(
                        MarkerOptions().position(marker)
                            .title(mapModelItem.name)
                            .snippet(mapModelItem.departureTime)
                    )
                    gMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            marker
                        )
                    )
                }
            })
            Log.e("Inside getSync() ", " 1")
        }

        Log.e("In openMap() after", "2")
    }
}