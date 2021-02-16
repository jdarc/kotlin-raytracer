package raytracer.geometry

import raytracer.core.Intersectable
import raytracer.materials.Material

abstract class Geometry protected constructor(var material: Material) : Intersectable
