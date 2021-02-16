package raytracer.materials

import raytracer.core.Color
import raytracer.core.Scalar.floor
import raytracer.core.Vector3

class Checkered(
    val altColor: Color,
    diffuse: Color,
    specular: Color,
    glossiness: Double = 24.0,
    reflectivity: Double = 0.0
) : Material(diffuse, specular, glossiness, reflectivity) {

    override fun computeDiffuse(position: Vector3): Color {
        val u = floor(position.x * 0.025)
        val v = floor(position.z * 0.025)
        return if ((v + u) % 2.0 != 0.0) super.computeDiffuse(position) else altColor
    }
}
