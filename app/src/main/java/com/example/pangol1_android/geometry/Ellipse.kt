package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents an ellipse shape
 */
@Serializable
data class Ellipse(
    override val center: Point = Point(),
    val radiusX: Double = 50.0,
    val radiusY: Double = 30.0
) : IShape {
    
    override val width: Double get() = radiusX * 2
    override val height: Double get() = radiusY * 2
    
    override fun move(dx: Double, dy: Double): Ellipse {
        return copy(center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Ellipse {
        return copy(radiusX = radiusX * factor, radiusY = radiusY * factor)
    }
    
    override fun rotate(degrees: Double): Ellipse {
        // Ellipses don't rotate in this simple implementation
        return this
    }
    
    override fun getBounds(): Bounds {
        return Bounds(
            minX = center.x - radiusX,
            minY = center.y - radiusY,
            maxX = center.x + radiusX,
            maxY = center.y + radiusY
        )
    }
    
    override fun toSVGElement(): String {
        return """<ellipse cx="${center.x}" cy="${center.y}" rx="$radiusX" ry="$radiusY" fill="currentColor" />"""
    }
}
