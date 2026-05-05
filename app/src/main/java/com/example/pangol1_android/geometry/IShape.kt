package com.example.pangol1_android.geometry

import kotlinx.serialization.Serializable

/**
 * Base interface for all shapes
 */
interface IShape {
    val center: Point
    val width: Double
    val height: Double
    
    fun move(dx: Double, dy: Double): IShape
    fun scale(factor: Double): IShape
    fun rotate(degrees: Double): IShape
    fun getBounds(): Bounds
    fun toSVGElement(): String
}

/**
 * Represents the bounding box of a shape
 */
@Serializable
data class Bounds(
    val minX: Double,
    val minY: Double,
    val maxX: Double,
    val maxY: Double
) {
    val width: Double get() = maxX - minX
    val height: Double get() = maxY - minY
    val centerX: Double get() = (minX + maxX) / 2
    val centerY: Double get() = (minY + maxY) / 2
}
