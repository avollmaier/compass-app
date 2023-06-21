package at.avollmaier.compassapp.models

class Waypoint(val label: String, val latitude: Double, val longitude: Double) {
    override fun toString(): String {
        return "$label: $latitude, $longitude"
    }

    //static
    companion object {
        fun deserialize(input: String): Waypoint {
            val parts = input.split(":")
            val label = parts[0]
            val coords = parts[1].split(",")
            val latitude = coords[0].toDouble()
            val longitude = coords[1].toDouble()
            return Waypoint(label, latitude, longitude)
        }
    }

}

