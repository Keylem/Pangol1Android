package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a group/container of shapes
 */
@Serializable
data class Group(
    val shapes: List<IShape> = emptyList(),
    override val center: Point = Point()
) : IShape {
    
    override val width: Double
        get() {
            if (shapes.isEmpty()) return 0.0
            val bounds = shapes.map { it.getBounds() }
            return bounds.maxOf { it.maxX } - bounds.minOf { it.minX }
        }
    
    override val height: Double
        get() {
            if (shapes.isEmpty()) return 0.0
            val bounds = shapes.map { it.getBounds() }
            return bounds.maxOf { it.maxY } - bounds.minOf { it.minY }
        }
    
    override fun move(dx: Double, dy: Double): Group {
        return copy(shapes = shapes.map { it.move(dx, dy) }, center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Group {
        return copy(shapes = shapes.map { it.scale(factor) })
    }
    
    override fun rotate(degrees: Double): Group {
        return copy(shapes = shapes.map { it.rotate(degrees) })
    }
    
    override fun getBounds(): Bounds {
        if (shapes.isEmpty()) {
            return Bounds(center.x, center.y, center.x, center.y)
        }
        val bounds = shapes.map { it.getBounds() }
        return Bounds(
            minX = bounds.minOf { it.minX },
            minY = bounds.minOf { it.minY },
            maxX = bounds.maxOf { it.maxX },
            maxY = bounds.maxOf { it.maxY }
        )
    }
    
    override fun toSVGElement(): String {
        val childElements = shapes.joinToString("\n") { it.toSVGElement() }
        return """<g>
$childElements
</g>"""
    }
    
    fun addShape(shape: IShape): Group {
        return copy(shapes = shapes + shape)
    }
}
