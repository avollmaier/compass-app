package at.avollmaier.compassapp.listeners

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import at.avollmaier.compassapp.models.DisplayRotation

class Compass(context: Context) : SensorEventListener {
    interface CompassListener {
        fun onAzimuthChange(azimuth: Float)
    }

    private var listener: CompassListener? = null
    private val sensorManager: SensorManager
    private val rotationSensor: Sensor

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            Toast.makeText(context, "Rotation vector sensor not supported", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun start() {
        sensorManager.registerListener(
            this, rotationSensor, SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun setListener(l: CompassListener?) {
        listener = l
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(
                rotationMatrix, floatArrayOf(event.values[0], event.values[1], event.values[2])
            )
            val remappedRotationMatrix =
                remapRotationMatrix(rotationMatrix, DisplayRotation.ROTATION_0)
            val magneticAzimuth =
                SensorManager.getOrientation(remappedRotationMatrix, FloatArray(9))
            var degreeMagneticAzimuth = Math.toDegrees(magneticAzimuth[0].toDouble()).toFloat()

            degreeMagneticAzimuth = normalizeAngle(degreeMagneticAzimuth)

            if (listener != null) {
                listener!!.onAzimuthChange(degreeMagneticAzimuth)
            }
        }
    }

    private fun normalizeAngle(angleInDegrees: Float): Float {
        return (angleInDegrees + 360f) % 360f
    }

    private fun remapRotationMatrix(
        rotationMatrix: FloatArray, displayRotation: DisplayRotation
    ): FloatArray {
        return when (displayRotation) {
            DisplayRotation.ROTATION_0 -> remapRotationMatrix(
                rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y
            )

            DisplayRotation.ROTATION_90 -> remapRotationMatrix(
                rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X
            )

            DisplayRotation.ROTATION_180 -> remapRotationMatrix(
                rotationMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y
            )

            DisplayRotation.ROTATION_270 -> remapRotationMatrix(
                rotationMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X
            )
        }
    }

    private fun remapRotationMatrix(rotationMatrix: FloatArray, newX: Int, newY: Int): FloatArray {
        val remappedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(rotationMatrix, newX, newY, remappedRotationMatrix)
        return remappedRotationMatrix
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}