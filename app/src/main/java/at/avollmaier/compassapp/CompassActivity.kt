package at.avollmaier.compassapp

import android.content.Intent
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import at.avollmaier.compassapp.listeners.Compass
import at.avollmaier.compassapp.listeners.Compass.CompassListener
import at.avollmaier.compassapp.service.WeatherApiService
import at.avollmaier.compassapp.utils.CardinalDirectionEvaluator
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


class CompassActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView


    private var compass: Compass? = null
    private var cardinalDirectionEvaluator: CardinalDirectionEvaluator? = null
    private lateinit var compassRoseImageView: ImageView
    private lateinit var statusDegreesText: TextView
    private lateinit var statusCardinalDirectionText: TextView
    private var sensorManager: SensorManager? = null
    private var currentAzimuth = 0f
    private var lastHapticAzimuth = 0

    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        initBottomNavigationBar()

        sensorManager = ActivityCompat.getSystemService(this, SensorManager::class.java)
        cardinalDirectionEvaluator = CardinalDirectionEvaluator()
        registerSensorListener()

        compassRoseImageView = findViewById(R.id.iv_roseImage)
        statusDegreesText = findViewById(R.id.tv_statusDegreeText)
        statusCardinalDirectionText = findViewById(R.id.tv_cardinalDirection)
        tvTemperature = findViewById(R.id.tv_temperature)
        tvHumidity = findViewById(R.id.tv_humidity)

        CoroutineScope(Dispatchers.Main).launch {
            tvTemperature.text = getString(R.string.degree_celsius_value, 0F)
            val weatherData = WeatherApiService().getWeatherData(47.0707, 15.4395) // Graz as default
            tvTemperature.text = getString(
                R.string.degree_celsius_value, kelvinToCelcius(weatherData.main.getTemp())
            )
            tvHumidity.text = getString(R.string.humidity_value, weatherData.main.getHumidity())
        }
    }

    private fun kelvinToCelcius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    override fun onStart() {
        super.onStart()
        compass?.start()
    }

    override fun onPause() {
        super.onPause()
        compass?.stop()
    }

    override fun onResume() {
        super.onResume()
        compass?.start()
    }

    override fun onStop() {
        super.onStop()
        compass?.stop()
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }


    private fun registerSensorListener() {
        compass = Compass(this)
        val cl: CompassListener = getCompassListener()
        compass!!.setListener(cl)
    }

    private fun getCompassListener(): CompassListener {
        return object : CompassListener {
            override fun onAzimuthChange(azimuth: Float) {
                adjustCompassRose(azimuth)
                adjustCardinalDirection(azimuth)
                if (abs(azimuth - lastHapticAzimuth) > 1) {
                    compassRoseImageView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    lastHapticAzimuth = azimuth.toInt()
                }
            }
        }
    }

    private fun adjustCardinalDirection(azimuth: Float) {
        statusCardinalDirectionText.text = cardinalDirectionEvaluator?.format(azimuth).toString()
        statusDegreesText.text = String.format(getString(R.string.degree_value), azimuth)
    }

    private fun adjustCompassRose(azimuth: Float) {
        val animation: Animation = RotateAnimation(
            -currentAzimuth,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth

        animation.duration = 500
        animation.repeatCount = 0
        animation.fillAfter = true

        compassRoseImageView.startAnimation(animation)
    }


    private fun initBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.selectedItemId = R.id.navigation_compass
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

}