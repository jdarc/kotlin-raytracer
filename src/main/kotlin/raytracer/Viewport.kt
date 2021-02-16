package raytracer

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class Viewport(private val raster: Raster) : JPanel(false) {

    override fun paintComponent(g: Graphics) = raster.paint(g as Graphics2D)

    init {
        size = Dimension(raster.width, raster.height)
        preferredSize = size
    }
}
