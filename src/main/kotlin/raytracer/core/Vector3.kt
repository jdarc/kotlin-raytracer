package raytracer.core

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector3(val x: Double, val y: Double, val z: Double) {

    val length get() = sqrt(x * x + y * y + z * z)

    operator fun plus(rhs: Vector3) = Vector3(x + rhs.x, y + rhs.y, z + rhs.z)

    operator fun minus(rhs: Vector3) = Vector3(x - rhs.x, y - rhs.y, z - rhs.z)

    operator fun times(rhs: Double) = Vector3(x * rhs, y * rhs, z * rhs)

    operator fun div(rhs: Double) = Vector3(x / rhs, y / rhs, z / rhs)

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    companion object {
        val ZERO = Vector3(0.0, 0.0, 0.0)
        val LEFT = Vector3(-1.0, 0.0, 0.0)
        val RIGHT = Vector3(1.0, 0.0, 0.0)
        val DOWN = Vector3(0.0, -1.0, 0.0)
        val UP = Vector3(0.0, 1.0, 0.0)
        val BACK = Vector3(0.0, 0.0, -1.0)
        val FRONT = Vector3(0.0, 0.0, 1.0)

        fun dot(lhs: Vector3, rhs: Vector3) = lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z

        fun normalize(vec: Vector3) = vec / vec.length

        fun distanceSquared(lhs: Vector3, rhs: Vector3) = (lhs.x - rhs.x).pow(2) + (lhs.y - rhs.y).pow(2) + (lhs.z - rhs.z).pow(2)

        fun reflect(i: Vector3, n: Vector3) = i - n * 2.0 * dot(n, i)
    }
}
