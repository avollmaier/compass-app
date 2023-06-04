package at.avollmaier.compassapp

import DatabaseManager
import WaypointAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.avollmaier.compassapp.models.Waypoint
import com.google.android.material.bottomnavigation.BottomNavigationView

class WaypointsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dbManager: DatabaseManager
    private lateinit var waypointAdapter: WaypointAdapter
    private lateinit var rvWaypoints: RecyclerView
    private lateinit var btnAddWaypoint: Button
    private lateinit var txtLabel: TextView
    private lateinit var txtLatitude: TextView
    private lateinit var txtLongitude: TextView

    override fun onResume() {
        super.onResume()
        dbManager = DatabaseManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoints)
        dbManager = DatabaseManager(this)
        rvWaypoints = findViewById(R.id.rv_waypoints)
        btnAddWaypoint = findViewById(R.id.btnAddWaypoint)
        txtLabel = findViewById(R.id.txt_label)
        txtLatitude = findViewById(R.id.txt_latitude)
        txtLongitude = findViewById(R.id.txt_longitude)

        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.selectedItemId = R.id.navigation_waypoints
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    startActivity(MainActivity::class.java)
                    true
                }

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
        waypointAdapter = WaypointAdapter(emptyList())
        rvWaypoints.layoutManager = LinearLayoutManager(this)
        rvWaypoints.adapter = waypointAdapter

        btnAddWaypoint.setOnClickListener {
            onAddWaypoint()
        }

        displayWaypoints()
    }

    private fun onAddWaypoint() {
        val label = txtLabel.text.toString().trim()
        val latitude = txtLatitude.text.toString().toDoubleOrNull()
        val longitude = txtLongitude.text.toString().toDoubleOrNull()

        if (label.isNotEmpty() && latitude != null && longitude != null) {
            val id = dbManager.insertWaypoint(label, latitude, longitude)
            if (id != -1L) {
                Toast.makeText(this, "Waypoint added successfully", Toast.LENGTH_SHORT).show()
                clearInputFields()
                displayWaypoints()
            } else {
                Toast.makeText(this, "Failed to add waypoint", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    private fun displayWaypoints() {
        val waypointsCursor: Cursor = dbManager.getAllWaypoints()
        val waypoints = mutableListOf<Waypoint>()

        if (waypointsCursor.moveToFirst()) {
            do {
                val label =
                    waypointsCursor.getString(waypointsCursor.getColumnIndex(DatabaseManager.COLUMN_LABEL))
                val latitude =
                    waypointsCursor.getDouble(waypointsCursor.getColumnIndex(DatabaseManager.COLUMN_LATITUDE))
                val longitude =
                    waypointsCursor.getDouble(waypointsCursor.getColumnIndex(DatabaseManager.COLUMN_LONGITUDE))

                val waypoint = Waypoint(label, latitude, longitude)
                waypoints.add(waypoint)
            } while (waypointsCursor.moveToNext())
        }

        waypointAdapter.waypoints = waypoints
        waypointAdapter.notifyDataSetChanged()
    }

    private fun clearInputFields() {
        txtLabel.text = ""
        txtLatitude.text = ""
        txtLongitude.text = ""
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}
