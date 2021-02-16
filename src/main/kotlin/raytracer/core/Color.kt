package raytracer.core

data class Color(val red: Double, val grn: Double, val blu: Double) {

    constructor(rgb: Int) : this(rgb shr 16 and 255, rgb shr 8 and 255, rgb and 255)

    constructor(red: Int, grn: Int, blu: Int) : this(red / 255.0, grn / 255.0, blu / 255.0)

    operator fun plus(rhs: Color) = Color(red + rhs.red, grn + rhs.grn, blu + rhs.blu)

    operator fun times(rhs: Color) = Color(red * rhs.red, grn * rhs.grn, blu * rhs.blu)

    operator fun times(scalar: Double) = Color(red * scalar, grn * scalar, blu * scalar)

    fun toRGB(): Int {
        val r = (red * 255.0).coerceAtMost(255.0).toInt() shl 16
        val g = (grn * 255.0).coerceAtMost(255.0).toInt() shl 8
        val b = (blu * 255.0).coerceAtMost(255.0).toInt()
        return r or g or b
    }

    companion object {
        val WHITE = Color(1.0, 1.0, 1.0)
        val BLACK = Color(0.0, 0.0, 0.0)

        fun parse(str: String) = Color(str.toIntOrNull(16) ?: 0)
    }
}
