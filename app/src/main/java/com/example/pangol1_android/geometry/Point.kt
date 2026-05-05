package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Represents a 2D point
 */
@Serializable
data class Point(
    val x: Double = 0.0,
    val y: Double = 0.0
) {
    fun move(dx: Double, dy: Double): Point = Point(x + dx, y + dy)
    
    fun distance(other: Point): Double {
        val dx = other.x - x
        val dy = other.y - y
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    
    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
    
    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)
    
    override fun toString(): String = "($x, $y)"
}
