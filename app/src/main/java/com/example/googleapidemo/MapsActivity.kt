package com.example.googleapidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.googleapidemo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.maps.android.clustering.ClusterManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var places : List<Place> = listOf(
        Place("Valencia Cyclery", LatLng(37.7557557, -122.4208508), "1077 Valencia Street, San Francisco", 4.2F),
        Place("San Francisco Bicycle Rentals", LatLng(37.80764569999999, -122.4195251), "425 Jefferson Street, San Francisco", 4.5F),
        Place("Blazing Saddles Bike Rentals & Tours", LatLng(37.8060487, -122.4206076), "2715 Hyde Street, San Francisco", 4.5F),
        Place("Mike's Bikes of San Francisco", LatLng(37.7757292, -122.4119508), "1233 Howard Street, San Francisco", 4.5F),
        Place("Huckleberry Bicycles", LatLng(37.7809098, -122.4117142), "Huckleberry Bicycles, 1073 Market Street, San Francisco", 4.5F),
    )

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.purple_700)
        BitMapHelper.vectorToBitmap(this, R.drawable.ic_baseline_electric_bike_24, color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer.
        val clusterManager = ClusterManager<Place>(this, googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this,
                googleMap,
                clusterManager
            )


        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        // Add the places to the ClusterManager.
        clusterManager.addItems(places)
        clusterManager.cluster()

        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.7809098, -122.4117142)))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addClusteredMarkers(mMap);
//        mMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
//        addMarkers(mMap);
    }

    private fun addMarkers(googleMap: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        val marker = googleMap.addMarker(
            MarkerOptions().title("Home")
                .position(sydney)
                .icon(bicycleIcon)
        )
        if (marker != null) {
            marker.tag = Place("David", sydney,"123 Debit Ave", 3.5F)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}