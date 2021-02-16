package raytracer

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import raytracer.core.Color
import raytracer.core.Light
import raytracer.core.Vector3
import raytracer.geometry.*
import raytracer.materials.Checkered
import raytracer.materials.Material
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object Loader {

    fun parse(name: String): Scene {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(File(ClassLoader.getSystemResource(name).file))
        doc.documentElement.normalize()

        val lights = parseLights(doc)
        val objects = parseGeometry(doc, parseMaterials(doc))
        val ambientColor = Color.parse(doc.documentElement.getAttribute("ambient"))

        return Scene(lights, objects, ambientColor)
    }

    private fun parseLights(doc: Document): Array<Light> = doc.getElementsByTagName("light").run {
        (0 until this.length).map {
            val attributes = this.item(it).attributes
            val color = attributes.getNamedItem("color").nodeValue
            Light(parseVector3(attributes), Color.parse(color))
        }.toTypedArray()
    }

    private fun parseMaterials(doc: Document): Map<String, Material> = doc.getElementsByTagName("material").run {
        (0 until this.length).map {
            val attributes = this.item(it).attributes
            val name = attributes.getNamedItem("name").nodeValue
            val type = attributes.getNamedItem("type").nodeValue
            val diffuse = Color.parse(attributes.getNamedItem("diffuse")?.nodeValue ?: "FFFFFF")
            val specular = Color.parse(attributes.getNamedItem("specular")?.nodeValue ?: "000000")
            val glossiness = (attributes.getNamedItem("glossiness")?.nodeValue ?: "24").toDouble()
            val reflectivity = (attributes.getNamedItem("reflectivity")?.nodeValue ?: "0").toDouble()
            name to when (type) {
                "SOLID" -> Material(diffuse, specular, glossiness, reflectivity)
                "CHECKERED" -> {
                    val altColor = Color.parse(attributes.getNamedItem("altColor")?.nodeValue ?: "000000")
                    Checkered(altColor, diffuse, specular, glossiness, reflectivity)
                }
                else -> throw RuntimeException("unknown material type")
            }
        }.toMap()
    }

    private fun parseGeometry(doc: Document, materials: Map<String, Material>): Array<Geometry> =
        doc.getElementsByTagName("geometry").run {
            (0 until this.length).map {
                val attributes = this.item(it).attributes
                val materialKey = attributes.getNamedItem("material")?.nodeValue ?: ""
                val material = materials.getOrDefault(materialKey, Material.DEFAULT)
                when (attributes.getNamedItem("type").nodeValue) {
                    "PLANE" -> Plane(parseVector3(attributes), attributes.parseDouble("distance"), material)
                    "SPHERE" -> Sphere(parseVector3(attributes), attributes.parseDouble("radius", 0.0), material)
                    "CYLINDER" -> parseCylinder(attributes, material)
                    "BOX" -> parseBox(attributes, material)
                    else -> throw RuntimeException("unknown primitive type")
                }
            }.toTypedArray()
        }

    private fun parseCylinder(attributes: NamedNodeMap, material: Material): Compound {
        val position = parseVector3(attributes)
        val radius = attributes.parseDouble("radius")
        val height = attributes.parseDouble("height")
        val cap = Vector3(0.0, height, 0.0)
        return Compound(Cylinder(position, radius, height, material), Disc(position + cap, Vector3.UP, radius, material))
    }

    private fun parseBox(attributes: NamedNodeMap, material: Material): Box {
        val xMin = attributes.parseDouble("xmin")
        val yMin = attributes.parseDouble("ymin")
        val zMin = attributes.parseDouble("zmin")
        val xMax = attributes.parseDouble("xmax")
        val yMax = attributes.parseDouble("ymax")
        val zMax = attributes.parseDouble("zmax")
        return Box(Vector3(xMin, yMin, zMin), Vector3(xMax, yMax, zMax), material)
    }

    private fun parseVector3(attributes: NamedNodeMap) =
        Vector3(attributes.parseDouble("x"), attributes.parseDouble("y"), attributes.parseDouble("z"))

    private fun NamedNodeMap.parseDouble(name: String, default: Double = 0.0) =
        this.getNamedItem(name)?.nodeValue?.toDoubleOrNull() ?: default
}
