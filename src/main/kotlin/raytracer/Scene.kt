package raytracer

import raytracer.core.*
import raytracer.core.Scalar.sqr
import raytracer.geometry.Geometry
import raytracer.materials.Material

private const val SHADOW_EPSILON = 0.00000001

class Scene(val lights: Array<Light>, private val objects: Array<Geometry>, private val ambient: Color = Color.BLACK) {

    fun computeAmbient(color: Color) = ambient * color

    fun findClosestAlong(ray: Ray): Intersection {
        var hitRay = Ray.ZERO
        var material = Material.DEFAULT
        var closest = Double.POSITIVE_INFINITY
        objects.forEach {
            val intersect = it.testAgainst(ray)
            if (intersect != Ray.ZERO) {
                val distSqr = Vector3.distanceSquared(intersect.origin, ray.origin)
                if (distSqr < closest) {
                    closest = distSqr
                    hitRay = intersect
                    material = it.material
                }
            }
        }
        return Intersection(hitRay, material)
    }

    fun isShadowed(position: Vector3, lightPosition: Vector3): Boolean {
        val distToLightSqr = Vector3.distanceSquared(lightPosition, position)
        val toLight = Vector3.normalize(lightPosition - position)
        val ray = Ray(position + toLight * SHADOW_EPSILON, toLight)
        objects.forEach { if (sqr(it.distanceAlong(ray)) < distToLightSqr) return true }
        return false
    }
}
