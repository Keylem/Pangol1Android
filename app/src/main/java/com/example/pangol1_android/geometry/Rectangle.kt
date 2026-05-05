package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a rectangle shape
 */
@Serializable
data class Rectangle(
    override val center: Point = Point(),
    override val width: Double = 100.0,
    override val height: Double = 50.0,
    val rotation: Double = 0.0
) : IShape {
    
    override fun move(dx: Double, dy: Double): Rectangle {
        return copy(center = center.move(dx, dy))
    }
    
    override fun scale(factor: Double): Rectangle {
        return copy(width = width * factor, height = height * factor)
    }
    
    override fun rotate(degrees: Double): Rectangle {
        return copy(rotation = (rotation + degrees) % 360)
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
        val x = center.x - width / 2
        val y = center.y - height / 2
        val transform = if (rotation != 0.0) {
            """ transform="rotate($rotation, ${center.x}, ${center.y})""""
        } else {
            ""
        }
        return """<rect x="$x" y="$y" width="$width" height="$height" fill="currentColor"$transform />"""
    }
}
