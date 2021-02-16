package raytracer.core

interface Intersectable {

    fun distanceAlong(ray: Ray): Double

    fun testAgainst(ray: Ray): Ray
}
