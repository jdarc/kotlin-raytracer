package raytracer

import raytracer.core.Color
import java.awt.AWTEvent
import java.awt.EventQueue
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.floor

class Window : JFrame("Ray Tracer") {
    private val raster = Raster(960, 540)
    private val tracer = Tracer(raster)
    private val camera = Camera(250.0, raster.height.toDouble())
    private var scene = Scene(emptyArray(), emptyArray(), Color.BLACK)
    private val viewport = Viewport(raster)

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        contentPane.add(viewport)
        pack()
        isResizable = false
        isVisible = true
        setLocationRelativeTo(null)
    }

    fun start(sceneFile: String) {
        scene = Loader.parse(sceneFile)
        Toolkit.getDefaultToolkit().systemEventQueue.push(object : EventQueue() {
            @Override
            override fun dispatchEvent(event: AWTEvent) {
                super.dispatchEvent(event)
                if (peekEvent() == null) {
                    renderFrame()
                    viewport.repaint()
                }
            }
        })
    }

    private fun renderFrame() {
        val tick = System.nanoTime()
        camera.rotate(0.6, tick / 7000000000.0)
        tracer.trace(scene, camera)
        val tock = System.nanoTime()
        raster.drawString("FPS: ${floor(1000000000.0 / (tock - tick)).toInt()}", 10, 20)
    }
}
