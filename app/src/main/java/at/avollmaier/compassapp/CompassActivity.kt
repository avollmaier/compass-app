package at.avollmaier.compassapp

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CompassActivity : AppCompatActivity() {
    private var compass: Compass? = null
    private var arrowView: ImageView? = null
    private var currentAzimuth = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        arrowView = findViewById<ImageView>(R.id.iv_compass)
        setupCompass()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "start compass")
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
        Log.d(TAG, "stop compass")
        compass?.stop()
    }

    private fun setupCompass() {
        compass = Compass(this)
        val cl: Compass.CompassListener = compassListener

        compass?.setListener(cl)
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(
            TAG, "will set rotation from " + currentAzimuth + " to "
                    + azimuth
        )
        val an: Animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        arrowView!!.startAnimation(an)
    }

    private val compassListener: Compass.CompassListener
        get() = object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                runOnUiThread {
                    adjustArrow(azimuth)
                }
            }
        }

    companion object {
        private const val TAG = "CompassActivity"
    }
}