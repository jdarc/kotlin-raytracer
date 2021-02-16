package raytracer.materials

import raytracer.core.Color
import raytracer.core.Vector3

open class Material(
    val diffuse: Color,
    val specular: Color,
    val glossiness: Double = 24.0,
    val reflectivity: Double = 0.0
) {

    open fun computeDiffuse(position: Vector3) = diffuse

    open fun computeSpecular(position: Vector3) = specular

    open fun computeGlossiness(position: Vector3) = glossiness

    open fun computeReflectivity(position: Vector3) = reflectivity

    companion object {
        val DEFAULT = Material(Color.WHITE, Color.BLACK)
    }
}
