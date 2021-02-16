package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Scalar.min
import raytracer.core.Vector3
import raytracer.materials.Material
import kotlin.math.sqrt

class Sphere(val center: Vector3, val radius: Double, material: Material) : Geometry(material) {

    override fun distanceAlong(ray: Ray): Double {
        val oc = ray.origin - center
        val a = Vector3.dot(ray.direction, ray.direction)
        val b = 2.0 * Vector3.dot(oc, ray.direction)
        val c = Vector3.dot(oc, oc) - radius * radius
        val discriminant = b * b - 4.0 * a * c
        if (discriminant <= 0.0) return Double.POSITIVE_INFINITY
        val d = sqrt(discriminant)
        val t = 0.5 * min(-b - d, -b + d)
        return if (t > 0.0) t else Double.POSITIVE_INFINITY
    }

    override fun testAgainst(ray: Ray): Ray {
        val t = distanceAlong(ray)
        if (t == Double.POSITIVE_INFINITY) return Ray.ZERO
        val origin = ray.getPoint(t)
        return Ray(origin, (origin - center) / radius)
    }
}
