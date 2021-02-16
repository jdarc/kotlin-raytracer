package raytracer.geometry

import raytracer.core.Ray
import raytracer.core.Scalar.min
import raytracer.core.Vector3
import raytracer.materials.Material

class Compound(private vararg val primitives: Geometry) : Geometry(Material.DEFAULT) {

    override fun distanceAlong(ray: Ray): Double {
        var closest = Double.POSITIVE_INFINITY
        primitives.forEach { closest = min(closest, it.distanceAlong(ray)) }
        return closest
    }

    override fun testAgainst(ray: Ray): Ray {
        var hitRay = Ray.ZERO
        var closestSqr = Double.POSITIVE_INFINITY
        primitives.forEach {
            val result = it.testAgainst(ray)
            if (result != Ray.ZERO) {
                val distSqr = Vector3.distanceSquared(ray.origin, result.origin)
                if (distSqr < closestSqr) {
                    closestSqr = distSqr
                    hitRay = result
                    material = it.material
                }
            }
        }
        return hitRay
    }
}
