package raytracer.core

import raytracer.materials.Material

data class Intersection(val ray: Ray, val material: Material) {
    val hit get() = ray != Ray.ZERO
}
