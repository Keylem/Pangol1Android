package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Represents a circle shape
 */
@Serializable
data class Circle(
    override val center: Point = Point(),
    val radius: Double = 50.0
) : IShape {
    
    override val width: Double get() = radius * 2
    override val height: Double get() = radius * 2
    
    override fun move(dx: Double, dy: Double): Circle {
        return copy(center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Circle {
        return copy(radius = radius * factor)
    }
    
    override fun rotate(degrees: Double): Circle {
        // Circles don't change with rotation
        return this
    }
    
    override fun getBounds(): Bounds {
        return Bounds(
            minX = center.x - radius,
            minY = center.y - radius,
            maxX = center.x + radius,
            maxY = center.y + radius
        )
    }
    
    override fun toSVGElement(): String {
        return """<circle cx="${center.x}" cy="${center.y}" r="$radius" fill="currentColor" />"""
    }
}
