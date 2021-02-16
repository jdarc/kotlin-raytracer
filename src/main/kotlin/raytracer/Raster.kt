package raytracer

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import kotlin.math.pow

class Raster(val width: Int, val height: Int) {
    private val font = Font("Space Mono", Font.BOLD, 15)
    private val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE)
    private val graphics = image.graphics as Graphics2D
    private val pixels = (image.raster.dataBuffer as DataBufferInt).data

    operator fun set(x: Int, y: Int, color: raytracer.core.Color) {
        pixels[y * width + x] = OPAQUE or gammaCorrect(color.toRGB())
    }

    fun paint(g: Graphics2D) = g.drawImage(image, null, 0, 0)

    fun drawString(str: String, x: Int, y: Int) {
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.font = font
        val bounds = font.getStringBounds(str, graphics.fontRenderContext).bounds; bounds.grow(8, 2)
        graphics.color = Color(0, 0, 0, 128)
        graphics.fillRect(bounds.x + x, bounds.y + y, bounds.width, bounds.height)
        graphics.color = Color.YELLOW
        graphics.drawString(str, x, y)
    }
}

private const val OPAQUE = 255.shl(24)

private const val GAMMA = 1.0 / 2.2

private val LUT_GAMMA = (0 until 256).map { (255.0 * (it / 255.0).pow(GAMMA)).toInt() }.toTypedArray()

private fun gammaCorrect(rgb: Int): Int {
    val red = LUT_GAMMA[255 and rgb.shr(16)]
    val grn = LUT_GAMMA[255 and rgb.shr(8)]
    val blu = LUT_GAMMA[255 and rgb]
    return red.shl(16) or grn.shl(8) or blu
}
