package at.avollmaier.compassapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompassActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.selectedItemId = R.id.navigation_compass
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

    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}