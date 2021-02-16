package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Scalar.min
import raytracer.core.Vector3
import raytracer.materials.Material
import kotlin.math.sqrt

class Cylinder(val position: Vector3, val radius: Double, val height: Double, material: Material) : Geometry(material) {

    override fun distanceAlong(ray: Ray): Double {
        val a = ray.direction.x * ray.direction.x + ray.direction.z * ray.direction.z
        if (a != 0.0) {
            val ro = ray.origin - position
            val b = 2.0 * (ro.x * ray.direction.x + ro.z * ray.direction.z)
            val c = ro.x * ro.x + ro.z * ro.z - radius * radius
            val discriminant = b * b - 4.0 * a * c
            if (discriminant >= 0.0) {
                val d = sqrt(discriminant)
                val inv2A = 1.0 / (2.0 * a)
                val t0 = (-b - d) * inv2A
                val t1 = (-b + d) * inv2A
                var t = Double.POSITIVE_INFINITY
                var intersects = false
                if (t0 > 0.0) {
                    intersects = true
                    val y0 = ray.origin.y + ray.direction.y * t0 - position.y
                    if (y0 > 0.0 && y0 < height) t = min(t, t0)
                }
                if (t1 > 0.0) {
                    intersects = true
                    val y1 = ray.origin.y + ray.direction.y * t1 - position.y
                    if (y1 > 0.0 && y1 < height) t = min(t, t1)
                }
                if (intersects) return t
            }
        }
        return Double.POSITIVE_INFINITY
    }

    override fun testAgainst(ray: Ray): Ray {
        val t = distanceAlong(ray)
        if (t == Double.POSITIVE_INFINITY) return Ray.ZERO
        val origin = ray.getPoint(t)
        val direction = Vector3.normalize(Vector3(origin.x - position.x, 0.0, origin.z - position.z))
        return if (Vector3.dot(direction, ray.direction) > 0.0) Ray(origin, -direction) else Ray(origin, direction)
    }
}
