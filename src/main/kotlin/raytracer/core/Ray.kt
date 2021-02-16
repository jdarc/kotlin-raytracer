package raytracer.core

data class Ray(val origin: Vector3, val direction: Vector3) {

    fun getPoint(t: Double) = Vector3(origin.x + t * direction.x, origin.y + t * direction.y, origin.z + t * direction.z)

    companion object {
        val ZERO = Ray(Vector3.ZERO, Vector3.ZERO)
    }
}
