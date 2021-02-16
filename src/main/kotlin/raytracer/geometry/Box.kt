package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Scalar.EPSILON
import raytracer.core.Scalar.max
import raytracer.core.Scalar.min
import raytracer.core.Vector3
import raytracer.materials.Material

class Box(val minimum: Vector3, val maximum: Vector3, material: Material) : Geometry(material) {

    override fun distanceAlong(ray: Ray): Double {
        val t1 = (minimum.x - ray.origin.x) / ray.direction.x
        val t2 = (maximum.x - ray.origin.x) / ray.direction.x
        val t3 = (minimum.y - ray.origin.y) / ray.direction.y
        val t4 = (maximum.y - ray.origin.y) / ray.direction.y
        val t5 = (minimum.z - ray.origin.z) / ray.direction.z
        val t6 = (maximum.z - ray.origin.z) / ray.direction.z
        val tMax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6))
        val tMin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6))
        if (tMax < 0.0 || tMin > tMax) return Double.POSITIVE_INFINITY
        return tMin
    }

    override fun testAgainst(ray: Ray): Ray {
        val t = distanceAlong(ray)
        if (t == Double.POSITIVE_INFINITY) return Ray.ZERO
        val origin = ray.getPoint(t)
        val direction = when {
            origin.x < minimum.x + EPSILON -> Vector3.LEFT
            origin.x > maximum.x - EPSILON -> Vector3.RIGHT
            origin.y < minimum.y + EPSILON -> Vector3.DOWN
            origin.y > maximum.y - EPSILON -> Vector3.UP
            origin.z < minimum.z + EPSILON -> Vector3.BACK
            else -> Vector3.FRONT
        }
        return Ray(origin, direction)
    }
}
