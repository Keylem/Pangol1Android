package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a polygon (n-sided shape)
 */
@Serializable
data class Polygon(
    val points: List<Point> = emptyList()
) : IShape {
    
    override val center: Point
        get() {
            if (points.isEmpty()) return Point()
            val avgX = points.map { it.x }.average()
            val avgY = points.map { it.y }.average()
            return Point(avgX, avgY)
        }
    
    override val width: Double
        get() {
            if (points.isEmpty()) return 0.0
            val minX = points.minOf { it.x }
            val maxX = points.maxOf { it.x }
            return maxX - minX
        }
    
    override val height: Double
        get() {
            if (points.isEmpty()) return 0.0
            val minY = points.minOf { it.y }
            val maxY = points.maxOf { it.y }
            return maxY - minY
        }
    
    override fun move(dx: Double, dy: Double): Polygon {
        return copy(points = points.map { it.move(dx, dy) })
    }
    
    override fun scale(factor: Double): Polygon {
        val c = center
        val newPoints = points.map { p ->
            Point(c.x + (p.x - c.x) * factor, c.y + (p.y - c.y) * factor)
        }
        return copy(points = newPoints)
    }
    
    override fun rotate(degrees: Double): Polygon {
        // Simplified rotation - would need full matrix math for proper implementation
        return this
    }
    
    override fun getBounds(): Bounds {
        if (points.isEmpty()) {
            return Bounds(0.0, 0.0, 0.0, 0.0)
        }
        return Bounds(
            minX = points.minOf { it.x },
            minY = points.minOf { it.y },
            maxX = points.maxOf { it.x },
            maxY = points.maxOf { it.y }
        )
    }
    
    override fun toSVGElement(): String {
        if (points.isEmpty()) return ""
        val pointsStr = points.joinToString(" ") { "${it.x},${it.y}" }
        return """<polygon points="$pointsStr" fill="currentColor" />"""
    }
}

/**
 * Represents a triangle
 */
@Serializable
data class Triangle(
    val p1: Point = Point(),
    val p2: Point = Point(100.0, 0.0),
    val p3: Point = Point(50.0, 86.6)
) : IShape {
    
    override val center: Point
        get() = Point((p1.x + p2.x + p3.x) / 3, (p1.y + p2.y + p3.y) / 3)
    
    override val width: Double
        get() = kotlin.math.max(kotlin.math.max(p1.x, p2.x), p3.x) - 
                kotlin.math.min(kotlin.math.min(p1.x, p2.x), p3.x)
    
    override val height: Double
        get() = kotlin.math.max(kotlin.math.max(p1.y, p2.y), p3.y) - 
                kotlin.math.min(kotlin.math.min(p1.y, p2.y), p3.y)
    
    override fun move(dx: Double, dy: Double): Triangle {
        return copy(
            p1 = p1.move(dx, dy),
            p2 = p2.move(dx, dy),
            p3 = p3.move(dx, dy)
        )
    }
    
    override fun scale(factor: Double): Triangle {
        val c = center
        val newP1 = Point(c.x + (p1.x - c.x) * factor, c.y + (p1.y - c.y) * factor)
        val newP2 = Point(c.x + (p2.x - c.x) * factor, c.y + (p2.y - c.y) * factor)
        val newP3 = Point(c.x + (p3.x - c.x) * factor, c.y + (p3.y - c.y) * factor)
        return copy(p1 = newP1, p2 = newP2, p3 = newP3)
    }
    
    override fun rotate(degrees: Double): Triangle {
        // Simplified - would need matrix math
        return this
    }
    
    override fun getBounds(): Bounds {
        return Bounds(
            minX = kotlin.math.min(kotlin.math.min(p1.x, p2.x), p3.x),
            minY = kotlin.math.min(kotlin.math.min(p1.y, p2.y), p3.y),
            maxX = kotlin.math.max(kotlin.math.max(p1.x, p2.x), p3.x),
            maxY = kotlin.math.max(kotlin.math.max(p1.y, p2.y), p3.y)
        )
    }
    
    override fun toSVGElement(): String {
        return """<polygon points="${p1.x},${p1.y} ${p2.x},${p2.y} ${p3.x},${p3.y}" fill="currentColor" />"""
    }
}
