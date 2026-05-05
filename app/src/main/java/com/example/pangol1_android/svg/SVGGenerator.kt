package com.example.pangol1_android.svg

import com.example.pangol1_android.core.model.DataTable
import com.example.pangol1_android.geometry.Circle
import com.example.pangol1_android.geometry.Point
import com.example.pangol1_android.geometry.Rectangle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Service for generating SVG visualizations from data
 */
object SVGGenerator {
    
    /**
     * Generate a bar chart from data
     */
    fun generateBarChart(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: dataTable.columns.firstOrNull()
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: dataTable.columns.lastOrNull()
        
        if (xColumn == null || yColumn == null) {
            return generateEmptySVG(width, height)
        }
        
        val xLabels = dataTable.getColumn(xColumn)
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (yValues.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val maxValue = yValues.maxOrNull() ?: 1.0
        val chartWidth = width - 100
        val chartHeight = height - 100
        val barWidth = chartWidth / maxOf(1, yValues.size)
        
        val bars = yValues.mapIndexed { index, value ->
            val barHeight = (value / maxValue) * (chartHeight * 0.8)
            val x = 50 + index * barWidth
            val y = height - 50 - barHeight
            val label = xLabels.getOrNull(index) ?: "Item $index"
            
            """<g>
                <rect x="$x" y="$y" width="${barWidth * 0.8}" height="$barHeight" fill="#4CAF50" />
                <text x="${x + barWidth / 2}" y="${height - 30}" text-anchor="middle" font-size="12">$label</text>
                <text x="${x + barWidth / 2}" y="${y - 5}" text-anchor="middle" font-size="12">${"%.1f".format(value)}</text>
            </g>"""
        }
        
        return generateSVG(width, height, bars)
    }
    
    /**
     * Generate a line chart from data
     */
    fun generateLineChart(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: return generateEmptySVG(width, height)
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: return generateEmptySVG(width, height)
        
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (yValues.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val maxValue = yValues.maxOrNull() ?: 1.0
        val minValue = yValues.minOrNull() ?: 0.0
        val chartWidth = width - 100
        val chartHeight = height - 100
        val pointSpacing = chartWidth / maxOf(1, yValues.size - 1)
        
        // Generate points
        val points = yValues.mapIndexed { index, value ->
            val x = 50 + index * pointSpacing
            val normalizedValue = (value - minValue) / (maxValue - minValue).let { if (it == 0.0) 1.0 else it }
            val y = height - 50 - normalizedValue * (chartHeight * 0.8)
            Point(x.toDouble(), y)
        }
        
        // Generate line path
        val pathData = points.mapIndexed { index, point ->
            if (index == 0) "M ${point.x} ${point.y}" else "L ${point.x} ${point.y}"
        }.joinToString(" ")
        
        val elements = mutableListOf(
            """<path d="$pathData" stroke="#2196F3" stroke-width="2" fill="none" />"""
        )
        
        // Add points and labels
        points.forEachIndexed { index, point ->
            elements.add("""<circle cx="${point.x}" cy="${point.y}" r="4" fill="#2196F3" />""")
            val xLabels = dataTable.getColumn(xColumn)
            val label = xLabels.getOrNull(index) ?: "Point $index"
            elements.add("""<text x="${point.x}" y="${height - 30}" text-anchor="middle" font-size="12">$label</text>""")
        }
        
        return generateSVG(width, height, elements)
    }
    
    /**
     * Generate a scatter plot from data
     */
    fun generateScatterPlot(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: return generateEmptySVG(width, height)
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: return generateEmptySVG(width, height)
        
        val xValues = dataTable.getColumnAsDoubles(xColumn)
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (xValues.isEmpty() || yValues.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val maxX = xValues.maxOrNull() ?: 1.0
        val minX = xValues.minOrNull() ?: 0.0
        val maxY = yValues.maxOrNull() ?: 1.0
        val minY = yValues.minOrNull() ?: 0.0
        
        val chartWidth = width - 100
        val chartHeight = height - 100
        
        val points = xValues.zip(yValues).map { (x, y) ->
            val normalizedX = (x - minX) / (maxX - minX).let { if (it == 0.0) 1.0 else it }
            val normalizedY = (y - minY) / (maxY - minY).let { if (it == 0.0) 1.0 else it }
            
            val plotX = 50 + normalizedX * chartWidth
            val plotY = height - 50 - normalizedY * chartHeight
            
            """<circle cx="$plotX" cy="$plotY" r="5" fill="#FF9800" opacity="0.7" />"""
        }
        
        return generateSVG(width, height, points)
    }
    
    /**
     * Generate a pie chart from data
     */
    fun generatePieChart(
        dataTable: DataTable,
        labelColumnIndex: Int = 0,
        valueColumnIndex: Int = 1,
        width: Int = 600,
        height: Int = 600
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val labelColumn = dataTable.columns.getOrNull(labelColumnIndex) ?: return generateEmptySVG(width, height)
        val valueColumn = dataTable.columns.getOrNull(valueColumnIndex) ?: return generateEmptySVG(width, height)
        
        val labels = dataTable.getColumn(labelColumn)
        val values = dataTable.getColumnAsDoubles(valueColumn)
        
        if (values.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val total = values.sum()
        if (total <= 0) {
            return generateEmptySVG(width, height)
        }
        
        val centerX = width / 2.0
        val centerY = height / 2.0
        val radius = minOf(width, height) / 2.0 * 0.8
        
        val colors = listOf("#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", "#98D8C8", 
                           "#F7DC6F", "#BB8FCE", "#85C1E2", "#F8B88B", "#A9DFBF")
        
        var currentAngle = -90.0
        val slices = mutableListOf<String>()
        
        values.forEachIndexed { index, value ->
            val percentage = value / total
            val sliceAngle = percentage * 360.0
            
            val startRadians = currentAngle * PI / 180.0
            val endRadians = (currentAngle + sliceAngle) * PI / 180.0
            
            val x1 = centerX + radius * cos(startRadians)
            val y1 = centerY + radius * sin(startRadians)
            val x2 = centerX + radius * cos(endRadians)
            val y2 = centerY + radius * sin(endRadians)
            
            val largeArc = if (sliceAngle > 180) 1 else 0
            
            val pathData = "M $centerX $centerY L $x1 $y1 A $radius $radius 0 $largeArc 1 $x2 $y2 Z"
            
            val color = colors[index % colors.size]
            slices.add("""<path d="$pathData" fill="$color" stroke="white" stroke-width="2" />""")
            
            // Add label
            val labelAngle = currentAngle + sliceAngle / 2
            val labelRadians = labelAngle * PI / 180.0
            val labelRadius = radius * 0.7
            val labelX = centerX + labelRadius * cos(labelRadians)
            val labelY = centerY + labelRadius * sin(labelRadians)
            
            val label = labels.getOrNull(index) ?: "Item $index"
            val percentage_text = "%.1f%%".format(percentage * 100)
            
            slices.add("""<text x="$labelX" y="$labelY" text-anchor="middle" font-size="12" fill="white" font-weight="bold">$percentage_text</text>""")
            
            currentAngle += sliceAngle
        }
        
        // Add legend
        var legendY = 20.0
        values.forEachIndexed { index, _ ->
            val label = labels.getOrNull(index) ?: "Item $index"
            val color = colors[index % colors.size]
            slices.add("""<rect x="10" y="${legendY - 10}" width="10" height="10" fill="$color" />""")
            slices.add("""<text x="25" y="$legendY" font-size="12">$label</text>""")
            legendY += 20.0
        }
        
        return generateSVG(width, height, slices, addAxes = false)
    }
    
    /**
     * Generate a histogram from data
     */
    fun generateHistogram(
        dataTable: DataTable,
        columnIndex: Int = 0,
        width: Int = 800,
        height: Int = 600,
        bins: Int = 10
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val column = dataTable.columns.getOrNull(columnIndex) ?: return generateEmptySVG(width, height)
        val values = dataTable.getColumnAsDoubles(column)
        
        if (values.isEmpty()) {
            return generateEmptySVG(width, height)
        }
        
        val minValue = values.minOrNull() ?: return generateEmptySVG(width, height)
        val maxValue = values.maxOrNull() ?: return generateEmptySVG(width, height)
        
        if (minValue == maxValue) {
            return generateEmptySVG(width, height)
        }
        
        val binSize = (maxValue - minValue) / bins
        val binCounts = MutableList(bins) { 0 }
        
        values.forEach { value ->
            val binIndex = ((value - minValue) / binSize).toInt().coerceIn(0, bins - 1)
            binCounts[binIndex]++
        }
        
        val maxCount = binCounts.maxOrNull() ?: 1
        val chartWidth = width - 100
        val chartHeight = height - 100
        val barWidth = chartWidth / bins
        
        val bars = binCounts.mapIndexed { index, count ->
            val barHeight = (count.toDouble() / maxCount) * (chartHeight * 0.8)
            val x = 50 + index * barWidth
            val y = height - 50 - barHeight
            val binStart = minValue + index * binSize
            val binEnd = binStart + binSize
            val label = "%.1f-%.1f".format(binStart, binEnd)
            
            """<g>
                <rect x="$x" y="$y" width="${barWidth * 0.9}" height="$barHeight" fill="#9C27B0" />
                <text x="${x + barWidth / 2}" y="${height - 30}" text-anchor="middle" font-size="10">$label</text>
                <text x="${x + barWidth / 2}" y="${y - 5}" text-anchor="middle" font-size="11">$count</text>
            </g>"""
        }
        
        return generateSVG(width, height, bars)
    }
    
    /**
     * Generate axes and basic SVG structure
     */
    private fun generateSVG(width: Int, height: Int, elements: List<String>, addAxes: Boolean = true): String {
        val elementsStr = elements.joinToString("\n")
        val axes = if (addAxes) """
    <!-- Axes -->
    <line x1="50" y1="50" x2="50" y2="${height - 50}" stroke="black" stroke-width="1" />
    <line x1="50" y1="${height - 50}" x2="${width - 50}" y2="${height - 50}" stroke="black" stroke-width="1" />
""" else ""
        
        return """<?xml version="1.0" encoding="UTF-8"?>
<svg width="$width" height="$height" xmlns="http://www.w3.org/2000/svg" style="background: white;">$axes
    <!-- Elements -->
    $elementsStr
</svg>"""
    }
    
    /**
     * Generate an empty SVG
     */
    private fun generateEmptySVG(width: Int, height: Int): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
<svg width="$width" height="$height" xmlns="http://www.w3.org/2000/svg" style="background: white;">
    <text x="${width / 2}" y="${height / 2}" text-anchor="middle" font-size="18">No data available</text>
</svg>"""
    }
}
