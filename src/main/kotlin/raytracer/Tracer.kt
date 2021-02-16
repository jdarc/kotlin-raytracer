package raytracer

import raytracer.core.Color
import raytracer.core.Ray
import raytracer.core.Scalar.EPSILON
import raytracer.core.Vector3
import raytracer.materials.Material
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import kotlin.math.pow
import kotlin.math.sqrt

private const val MAX_DEPTH = 5

class Tracer(private val raster: Raster) {

    fun trace(scene: Scene, camera: Camera) {
        val origin = camera.computeOrigin()
        ForkJoinPool.commonPool().invokeAll((0 until raster.height).map { y ->
            Executors.callable {
                for (x in 0 until raster.width) {
                    val direction = camera.computeDirection(x - raster.width * 0.5, raster.height * 0.5 - y)
                    val ray = Ray(origin, Vector3.normalize(direction))
                    raster[x, y] = traceRay(scene, ray, 0)
                }
            }
        })
    }

    private fun traceRay(scene: Scene, ray: Ray, depth: Int): Color {
        if (depth < MAX_DEPTH) {
            val closest = scene.findClosestAlong(ray)
            if (closest.hit) {
                val (position, normal) = closest.ray
                val reflectivity = closest.material.computeReflectivity(position)
                if (reflectivity > 0.0) {
                    val reflected = Vector3.normalize(Vector3.reflect(ray.direction, normal))
                    val re = reflectivity + (1.0 - reflectivity) * fresnelReflectAmount(ray.direction, normal, 1.0003, 1.1)
                    val reflectedColor = traceRay(scene, Ray(position + normal * EPSILON, reflected), depth + 1) * re
                    val color = shade(scene, ray, closest.ray, closest.material)
                    return color * (1.0 - reflectivity) + reflectedColor * reflectivity
                }
                return shade(scene, ray, closest.ray, closest.material)
            }
        }
        return Color(0.7, 0.6, 0.9)
    }

    private fun shade(scene: Scene, ray: Ray, hitRay: Ray, material: Material): Color {
        val (position, normal) = hitRay
        val reflected = Vector3.normalize(Vector3.reflect(ray.direction, normal))
        val diffuse = material.computeDiffuse(position)
        val specular = material.computeSpecular(position)
        val glossiness = material.computeGlossiness(position)
        var color = scene.computeAmbient(diffuse)
        scene.lights.forEach {
            if (!scene.isShadowed(position, it.position)) {
                color += diffuse * it.color * it.computeIntensity(position, normal)
                color += specular * it.color * it.computeIntensity(position, reflected).pow(glossiness)
            }
        }
        return color
    }

    private fun fresnelReflectAmount(incident: Vector3, normal: Vector3, n1: Double, n2: Double): Double {
        var cosX = -Vector3.dot(incident, normal)
        if (n1 > n2) {
            val n = n1 / n2
            val sinT2 = n * n * (1.0 - cosX * cosX)
            if (sinT2 > 1.0) return 1.0
            cosX = sqrt(1.0 - sinT2)
        }
        val r0 = ((n1 - n2) / (n1 + n2)).pow(2)
        return r0 + (1.0 - r0) * (1.0 - cosX).pow(5)
    }
}
