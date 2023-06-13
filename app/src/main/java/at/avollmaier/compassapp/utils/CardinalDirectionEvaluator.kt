package at.avollmaier.compassapp.utils


import at.avollmaier.compassapp.models.CardinalDirection


class CardinalDirectionEvaluator {
    private val sides = intArrayOf(0, 45, 90, 135, 180, 225, 270, 315, 360)
    private var names: Array<CardinalDirection>? = null

    fun format(azimuth: Float): CardinalDirection {
        if (names == null) {
            initLocalizedNames()
        }
        val iAzimuth = azimuth.toInt()
        val index = findClosestIndex(iAzimuth)
        return names!![index]
    }

    private fun initLocalizedNames() {
        if (names == null) {
            names = arrayOf(
                CardinalDirection.NORTH,
                CardinalDirection.NORTHEAST,
                CardinalDirection.EAST,
                CardinalDirection.SOUTHEAST,
                CardinalDirection.SOUTH,
                CardinalDirection.SOUTHWEST,
                CardinalDirection.WEST,
                CardinalDirection.NORTHWEST,
                CardinalDirection.NORTH
            )
        }
    }


    private fun findClosestIndex(target: Int): Int {
        var i = 0
        var j = sides.size
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2

            if (target < sides[mid]) {

                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target)
                }

                j = mid
            } else {
                if (mid < sides.size - 1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target)
                }
                i = mid + 1
            }
        }

        return mid
    }

    private fun getClosest(index1: Int, index2: Int, target: Int): Int {
        return if (target - sides[index1] >= sides[index2] - target) {
            index2
        } else index1
    }
}