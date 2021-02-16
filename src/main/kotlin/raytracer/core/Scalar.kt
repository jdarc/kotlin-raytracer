package raytracer.core

object Scalar {
    const val EPSILON = 0.00001

    fun min(a: Double, b: Double) = if (a < b) a else b

    fun max(a: Double, b: Double) = if (a > b) a else b

    fun floor(a: Double) = (1073741823.0 + a).toInt() - 0x3FFFFFFF

    fun sqr(a: Double) = a * a
}
