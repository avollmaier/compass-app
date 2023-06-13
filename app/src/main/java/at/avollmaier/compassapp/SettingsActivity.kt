package at.avollmaier.compassapp

import DatabaseManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnClearDatabase: Button
    private lateinit var dbManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        dbManager = DatabaseManager(this)
        btnClearDatabase = findViewById(R.id.btn_clr_db)
        btnClearDatabase.setOnClickListener {
            onClearDatabaseClick()
        }

        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.selectedItemId = R.id.navigation_settings
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

    private fun onClearDatabaseClick() {
        dbManager.deleteAllData()
    }
}