package com.alimasood.uberclone3

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class mapsactivity : AppCompatActivity(),OnMapReadyCallback ,GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var lastlocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        private val LOCATION_REQUEST_CODE=1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsactivity)
        val mapfrag=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapfrag.getMapAsync(this)
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap=googleMap
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.setOnMarkerClickListener (this)
        setupMap()
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
            {
ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
    LOCATION_REQUEST_CODE)
            return
            }

        mMap.isMyLocationEnabled=true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->

            if(location!=null){
                lastlocation=location
                val currentLatLong=LatLng(location.latitude,location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))

            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {

        val marketoptions= MarkerOptions().position(currentLatLong)
        marketoptions.title("$currentLatLong")
        mMap.addMarker(marketoptions)

    }

    override fun onMarkerClick(p0: Marker): Boolean =false
}