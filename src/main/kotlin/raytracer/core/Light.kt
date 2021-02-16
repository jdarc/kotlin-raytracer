package raytracer.core

import kotlin.math.sqrt

data class Light(val position: Vector3, val color: Color) {
    fun computeIntensity(position: Vector3, normal: Vector3): Double {
        val x = this.position.x - position.x
        val y = this.position.y - position.y
        val z = this.position.z - position.z
        val len = sqrt(x * x + y * y + z * z)
        val dot = normal.x * x + normal.y * y + normal.z * z
        return (dot / len).coerceIn(0.0, 1.0)
    }
}
