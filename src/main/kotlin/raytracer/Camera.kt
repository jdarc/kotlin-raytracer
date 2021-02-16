package raytracer

import raytracer.core.Matrix4
import raytracer.core.Matrix4.Companion.rotation
import raytracer.core.Vector3

class Camera(private val distance: Double, private val fov: Double) {
    private var look = Matrix4.IDENTITY

    fun computeOrigin() = Vector3(-distance * look.m20, -distance * look.m21, -distance * look.m22)

    fun computeDirection(x: Double, y: Double) = look * Vector3(x, y, fov)

    fun rotate(aboutX: Double, aboutY: Double) {
        look = rotation(aboutX, aboutY, 0.0)
    }
}
