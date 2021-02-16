package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Vector3
import raytracer.materials.Material

class Disc(val position: Vector3, val normal: Vector3, val radius: Double, material: Material) : Geometry(material) {

    override fun distanceAlong(ray: Ray): Double {
        val vd = Vector3.dot(normal, ray.direction)
        if (vd != 0.0) {
            val x = ray.origin.x - position.x
            val y = ray.origin.y - position.y
            val z = ray.origin.z - position.z
            val t = -(x * normal.x + y * normal.y + z * normal.z) / vd
            if (t > 0.0) {
                val px = x + t * ray.direction.x
                val py = y + t * ray.direction.y
                val pz = z + t * ray.direction.z
                if (px * px + py * py + pz * pz < radius * radius) return t
            }
        }
        return Double.POSITIVE_INFINITY
    }

    override fun testAgainst(ray: Ray): Ray {
        val t = distanceAlong(ray)
        if (t == Double.POSITIVE_INFINITY) return Ray.ZERO
        val origin = ray.getPoint(t)
        return if (Vector3.distanceSquared(origin, position) < radius * radius) Ray(origin, normal) else Ray.ZERO
    }
}
