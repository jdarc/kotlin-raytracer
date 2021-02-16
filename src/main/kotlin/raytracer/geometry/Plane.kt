package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Vector3
import raytracer.materials.Material

class Plane(normal: Vector3, val distance: Double, material: Material) : Geometry(material) {
    val normal = Vector3.normalize(normal)

    override fun distanceAlong(ray: Ray): Double {
        val vd = Vector3.dot(normal, ray.direction)
        if (vd != 0.0) {
            val t = -(Vector3.dot(normal, ray.origin) - distance) / vd
            if (t > 0.0) return t
        }
        return Double.POSITIVE_INFINITY
    }

    override fun testAgainst(ray: Ray): Ray {
        val t = distanceAlong(ray)
        return if (t == Double.POSITIVE_INFINITY) Ray.ZERO else Ray(ray.getPoint(t), normal)
    }
}
