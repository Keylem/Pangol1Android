package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a text element
 */
@Serializable
data class Text(
    override val center: Point = Point(),
    val content: String = "",
    val fontSize: Double = 16.0,
    override val width: Double = 100.0,
    override val height: Double = 20.0
) : IShape {
    
    override fun move(dx: Double, dy: Double): Text {
        return copy(center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Text {
        return copy(fontSize = fontSize * factor, width = width * factor, height = height * factor)
    }
    
    override fun rotate(degrees: Double): Text {
        // Text rotation not typically used
        return this
    }
    
    override fun getBounds(): Bounds {
        val halfW = width / 2
        val halfH = height / 2
        return Bounds(
            minX = center.x - halfW,
            minY = center.y - halfH,
            maxX = center.x + halfW,
            maxY = center.y + halfH
        )
    }
    
    override fun toSVGElement(): String {
        return """<text x="${center.x}" y="${center.y}" font-size="$fontSize" text-anchor="middle">$content</text>"""
    }
}
