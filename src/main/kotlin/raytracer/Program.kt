package raytracer

import javax.swing.SwingUtilities

object Program {

    @JvmStatic
    fun main(args: Array<String>) = SwingUtilities.invokeLater { Window().start("test02.xml") }
}
