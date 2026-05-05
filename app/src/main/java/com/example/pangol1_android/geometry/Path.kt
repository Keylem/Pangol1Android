package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a path (arbitrary shape defined by SVG path data)
 */
@Serializable
data class Path(
    val pathData: String = "",
    override val center: Point = Point(),
    override val width: Double = 100.0,
    override val height: Double = 100.0
) : IShape {
    
    override fun move(dx: Double, dy: Double): Path {
        return copy(center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Path {
        return copy(width = width * factor, height = height * factor)
    }
    
    override fun rotate(degrees: Double): Path {
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
        return """<path d="$pathData" fill="currentColor" />"""
    }
}
