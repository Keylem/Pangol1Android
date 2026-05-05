package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a line between two points
 */
@Serializable
data class Line(
    val start: Point = Point(),
    val end: Point = Point()
) : IShape {
    
    override val center: Point
        get() = Point((start.x + end.x) / 2, (start.y + end.y) / 2)
    
    override val width: Double
        get() = kotlin.math.abs(end.x - start.x)
    
    override val height: Double
        get() = kotlin.math.abs(end.y - start.y)
    
    override fun move(dx: Double, dy: Double): Line {
        return copy(start = start.move(dx, dy), end = end.move(dx, dy))
    }
    
    override fun scale(factor: Double): Line {
        val c = center
        val newStart = Point(c.x + (start.x - c.x) * factor, c.y + (start.y - c.y) * factor)
        val newEnd = Point(c.x + (end.x - c.x) * factor, c.y + (end.y - c.y) * factor)
        return copy(start = newStart, end = newEnd)
    }
    
    override fun rotate(degrees: Double): Line {
        // Simple rotation - would need matrix math for proper implementation
        return this
    }
    
    override fun getBounds(): Bounds {
        return Bounds(
            minX = kotlin.math.min(start.x, end.x),
            minY = kotlin.math.min(start.y, end.y),
            maxX = kotlin.math.max(start.x, end.x),
            maxY = kotlin.math.max(start.y, end.y)
        )
    }
    
    override fun toSVGElement(): String {
        return """<line x1="${start.x}" y1="${start.y}" x2="${end.x}" y2="${end.y}" stroke="currentColor" stroke-width="2" />"""
    }
}
