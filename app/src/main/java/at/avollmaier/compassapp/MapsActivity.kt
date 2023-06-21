package at.avollmaier.compassapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.avollmaier.compassapp.databinding.ActivityMapsBinding
import at.avollmaier.compassapp.models.Waypoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var uiSettings: UiSettings
    private var currentMarker: Marker? = null

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

        var waypoint: Waypoint =
            Waypoint("FH Joanneum Kapfenberg", 47.45374487973444, 15.330937171601185)
        val waypointParamString = intent.getStringExtra("waypoint")

        if (waypointParamString != null) {
            waypoint = Waypoint.deserialize(waypointParamString)
        }
        val waypointLocation = LatLng(waypoint.latitude, waypoint.longitude)

        currentMarker =
            mMap.addMarker(MarkerOptions().position(waypointLocation).title(waypoint.label))
        val bounds = LatLngBounds.Builder().include(waypointLocation).build()
        val padding = 50
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.moveCamera(cameraUpdate)
        mMap.animateCamera(cameraUpdate)
        uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.setAllGesturesEnabled(true)

        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener { marker ->
            val intent = Intent(this, WaypointsActivity::class.java)
            val wp = marker.title?.let {
                Waypoint(
                    it,
                    marker.position.latitude,
                    marker.position.longitude
                )
            }
            intent.putExtra("waypoint", wp.toString())
            startActivity(intent)
            false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.selectedItemId = R.id.navigation_map
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_compass -> {
                    startActivity(CompassActivity::class.java)
                    true
                }

                R.id.navigation_map -> {
                    startActivity(MapsActivity::class.java)
                    true
                }

                R.id.navigation_settings -> {
                    startActivity(SettingsActivity::class.java)
                    true
                }

                R.id.navigation_waypoints -> {
                    startActivity(WaypointsActivity::class.java)
                    true
                }

                else -> false
            }
        }
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    override fun onMapClick(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Marker")
        currentMarker?.remove()


        val marker = mMap.addMarker(markerOptions)
        currentMarker = marker

    }
}