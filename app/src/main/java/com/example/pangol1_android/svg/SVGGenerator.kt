package com.example.pangol1_android.svg

import com.example.pangol1_android.core.model.DataTable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Service for generating SVG visualizations from data with theme support
 */
object SVGGenerator {
    
    // Theme-aware color schemes
    private fun getThemeColors(isDarkMode: Boolean): ThemeColors {
        return if (isDarkMode) {
            ThemeColors(
                background = "#121212",
                text = "#FFFFFF",
                gridLine = "#404040",
                axes = "#E0E0E0",
                bars = "#4DB8FF",
                line = "#64B5F6",
                scatter = "#81C784",
                pie = listOf("#FF7043", "#29B6F6", "#66BB6A", "#FDD835", "#AB47BC", "#EC407A", "#29B6F6", "#EF5350", "#FFCA28", "#42A5F5"),
                histogram = "#BA68C8"
            )
        } else {
            ThemeColors(
                background = "#FFFFFF",
                text = "#212121",
                gridLine = "#E0E0E0",
                axes = "#424242",
                bars = "#4CAF50",
                line = "#2196F3",
                scatter = "#FF9800",
                pie = listOf("#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E2", "#F8B88B", "#A9DFBF"),
                histogram = "#9C27B0"
            )
        }
    }
    
    private data class ThemeColors(
        val background: String,
        val text: String,
        val gridLine: String,
        val axes: String,
        val bars: String,
        val line: String,
        val scatter: String,
        val pie: List<String>,
        val histogram: String
    )
    
    /**
     * Generate a bar chart from data
     */
    fun generateBarChart(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600,
        isDarkMode: Boolean = false
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val colors = getThemeColors(isDarkMode)
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: dataTable.columns.firstOrNull()
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: dataTable.columns.lastOrNull()
        
        if (xColumn == null || yColumn == null) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val xLabels = dataTable.getColumn(xColumn)
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (yValues.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
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
                <rect x="$x" y="$y" width="${barWidth * 0.8}" height="$barHeight" fill="${colors.bars}" rx="4" ry="4" opacity="0.9" />
                <text x="${x + barWidth / 2}" y="${height - 30}" text-anchor="middle" font-size="12" fill="${colors.text}" font-family="sans-serif">$label</text>
                <text x="${x + barWidth / 2}" y="${y - 8}" text-anchor="middle" font-size="12" fill="${colors.text}" font-weight="bold" font-family="sans-serif">${"%.1f".format(value)}</text>
            </g>"""
        }
        
        return generateSVG(width, height, bars, colors)
    }
    
    /**
     * Generate a line chart from data
     */
    fun generateLineChart(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600,
        isDarkMode: Boolean = false
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val colors = getThemeColors(isDarkMode)
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (yValues.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val maxValue = yValues.maxOrNull() ?: 1.0
        val minValue = yValues.minOrNull() ?: 0.0
        val chartWidth = width - 100
        val chartHeight = height - 100
        val pointSpacing = chartWidth / maxOf(1, yValues.size - 1)
        
        // Generate points
        val points = mutableListOf<Pair<Double, Double>>()
        yValues.forEachIndexed { index, value ->
            val x = 50.0 + index * pointSpacing
            val normalizedValue = (value - minValue) / (maxValue - minValue).let { if (it == 0.0) 1.0 else it }
            val y = height - 50.0 - normalizedValue * (chartHeight * 0.8)
            points.add(Pair(x, y))
        }
        
        // Generate line path
        val pathData = points.mapIndexed { index, point ->
            if (index == 0) "M ${point.first} ${point.second}" else "L ${point.first} ${point.second}"
        }.joinToString(" ")
        
        val elements = mutableListOf(
            """<path d="$pathData" stroke="${colors.line}" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round" opacity="0.85" />"""
        )
        
        // Add points and labels
        points.forEachIndexed { index, point ->
            elements.add("""<circle cx="${point.first}" cy="${point.second}" r="5" fill="${colors.line}" opacity="0.9" />""")
            val xLabels = dataTable.getColumn(xColumn)
            val label = xLabels.getOrNull(index) ?: "Point $index"
            elements.add("""<text x="${point.first}" y="${height - 30}" text-anchor="middle" font-size="12" fill="${colors.text}" font-family="sans-serif">$label</text>""")
        }
        
        return generateSVG(width, height, elements, colors)
    }
    
    /**
     * Generate a scatter plot from data
     */
    fun generateScatterPlot(
        dataTable: DataTable,
        xColumnIndex: Int = 0,
        yColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600,
        isDarkMode: Boolean = false
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val colors = getThemeColors(isDarkMode)
        val xColumn = dataTable.columns.getOrNull(xColumnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        val yColumn = dataTable.columns.getOrNull(yColumnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        
        val xValues = dataTable.getColumnAsDoubles(xColumn)
        val yValues = dataTable.getColumnAsDoubles(yColumn)
        
        if (xValues.isEmpty() || yValues.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val maxX = xValues.maxOrNull() ?: 1.0
        val minX = xValues.minOrNull() ?: 0.0
        val maxY = yValues.maxOrNull() ?: 1.0
        val minY = yValues.minOrNull() ?: 0.0
        
        val chartWidth = width - 100
        val chartHeight = height - 100
        
        val points = mutableListOf<String>()
        val minSize = minOf(xValues.size, yValues.size)
        
        for (i in 0 until minSize) {
            val normalizedX = (xValues[i] - minX) / (maxX - minX).let { if (it == 0.0) 1.0 else it }
            val normalizedY = (yValues[i] - minY) / (maxY - minY).let { if (it == 0.0) 1.0 else it }
            
            val x = 50 + normalizedX * chartWidth
            val y = height - 50 - normalizedY * chartHeight
            
            points.add("""<circle cx="$x" cy="$y" r="6" fill="${colors.scatter}" opacity="0.8" />""")
        }
        
        return generateSVG(width, height, points, colors)
    }
    
    /**
     * Generate a pie chart from data
     */
    fun generatePieChart(
        dataTable: DataTable,
        columnIndex: Int = 0,
        valueColumnIndex: Int = 1,
        width: Int = 800,
        height: Int = 600,
        isDarkMode: Boolean = false
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val colors = getThemeColors(isDarkMode)
        val labelColumn = dataTable.columns.getOrNull(columnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        val valueColumn = dataTable.columns.getOrNull(valueColumnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        
        val labels = dataTable.getColumn(labelColumn)
        val values = dataTable.getColumnAsDoubles(valueColumn)
        
        if (values.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val total = values.sum()
        val centerX = width / 2.0
        val centerY = height / 2.0 - 30
        val radius = minOf(width, height) * 0.3
        
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
            
            val color = colors.pie[index % colors.pie.size]
            slices.add("""<path d="$pathData" fill="$color" stroke="${colors.background}" stroke-width="2" opacity="0.9" />""")
            
            // Add label
            val labelAngle = currentAngle + sliceAngle / 2
            val labelRadians = labelAngle * PI / 180.0
            val labelRadius = radius * 0.7
            val labelX = centerX + labelRadius * cos(labelRadians)
            val labelY = centerY + labelRadius * sin(labelRadians)
            
            val label = labels.getOrNull(index) ?: "Item $index"
            val percentage_text = "%.1f%%".format(percentage * 100)
            
            slices.add("""<text x="$labelX" y="$labelY" text-anchor="middle" font-size="12" fill="${colors.text}" font-weight="bold" font-family="sans-serif">$percentage_text</text>""")
            
            currentAngle += sliceAngle
        }
        
        // Add legend
        var legendY = 20.0
        values.forEachIndexed { index, _ ->
            val label = labels.getOrNull(index) ?: "Item $index"
            val color = colors.pie[index % colors.pie.size]
            slices.add("""<rect x="10" y="${legendY - 10}" width="12" height="12" fill="$color" rx="2" opacity="0.9" />""")
            slices.add("""<text x="30" y="$legendY" font-size="12" fill="${colors.text}" font-family="sans-serif">$label</text>""")
            legendY += 20.0
        }
        
        return generateSVG(width, height, slices, colors, addAxes = false)
    }
    
    /**
     * Generate a histogram from data
     */
    fun generateHistogram(
        dataTable: DataTable,
        columnIndex: Int = 0,
        width: Int = 800,
        height: Int = 600,
        isDarkMode: Boolean = false,
        bins: Int = 10
    ): String {
        if (dataTable.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val colors = getThemeColors(isDarkMode)
        val column = dataTable.columns.getOrNull(columnIndex) ?: return generateEmptySVG(width, height, isDarkMode)
        val values = dataTable.getColumnAsDoubles(column)
        
        if (values.isEmpty()) {
            return generateEmptySVG(width, height, isDarkMode)
        }
        
        val minValue = values.minOrNull() ?: return generateEmptySVG(width, height, isDarkMode)
        val maxValue = values.maxOrNull() ?: return generateEmptySVG(width, height, isDarkMode)
        
        if (minValue == maxValue) {
            return generateEmptySVG(width, height, isDarkMode)
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
                <rect x="$x" y="$y" width="${barWidth * 0.9}" height="$barHeight" fill="${colors.histogram}" rx="3" ry="3" opacity="0.85" />
                <text x="${x + barWidth / 2}" y="${height - 30}" text-anchor="middle" font-size="10" fill="${colors.text}" font-family="sans-serif">$label</text>
                <text x="${x + barWidth / 2}" y="${y - 5}" text-anchor="middle" font-size="11" fill="${colors.text}" font-weight="bold" font-family="sans-serif">$count</text>
            </g>"""
        }
        
        return generateSVG(width, height, bars, colors)
    }
    
    /**
     * Generate axes and basic SVG structure
     */
    private fun generateSVG(
        width: Int,
        height: Int,
        elements: List<String>,
        colors: ThemeColors,
        addAxes: Boolean = true
    ): String {
        val elementsStr = elements.joinToString("\n")
        val axes = if (addAxes) """
    <!-- Grid lines -->
    <line x1="50" y1="50" x2="50" y2="${height - 50}" stroke="${colors.axes}" stroke-width="2" />
    <line x1="50" y1="${height - 50}" x2="${width - 50}" y2="${height - 50}" stroke="${colors.axes}" stroke-width="2" />
""" else ""
        
        return """<?xml version="1.0" encoding="UTF-8"?>
<svg viewBox="0 0 $width $height" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" style="width: 100%; height: 100%; background: ${colors.background};">
    <defs>
        <style>
            @keyframes slideIn {
                from { opacity: 0; transform: translateY(10px); }
                to { opacity: 1; transform: translateY(0); }
            }
            @keyframes fadeIn {
                from { opacity: 0; }
                to { opacity: 1; }
            }
            @keyframes scaleIn {
                from { opacity: 0; transform: scale(0.8); }
                to { opacity: 1; transform: scale(1); }
            }
            rect {
                animation: slideIn 0.6s ease-out forwards;
            }
            circle {
                animation: scaleIn 0.6s ease-out forwards;
            }
            text {
                animation: fadeIn 0.8s ease-out forwards;
            }
            path {
                animation: fadeIn 0.8s ease-out forwards;
                stroke-dasharray: 1000;
                stroke-dashoffset: 1000;
                animation: dashStroke 1.2s ease-out forwards;
            }
            @keyframes dashStroke {
                to { stroke-dashoffset: 0; }
            }
        </style>
    </defs>$axes
    <!-- Elements -->
    $elementsStr
</svg>"""
    }
    
    /**
     * Generate an empty SVG
     */
    private fun generateEmptySVG(width: Int, height: Int, isDarkMode: Boolean = false): String {
        val colors = getThemeColors(isDarkMode)
        return """<?xml version="1.0" encoding="UTF-8"?>
<svg viewBox="0 0 $width $height" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" style="width: 100%; height: 100%; background: ${colors.background};">
    <defs>
        <style>
            @keyframes fadeIn {
                from { opacity: 0; }
                to { opacity: 1; }
            }
            text {
                animation: fadeIn 0.8s ease-out forwards;
            }
        </style>
    </defs>
    <text x="${width / 2}" y="${height / 2}" text-anchor="middle" font-size="18" fill="${colors.text}" font-family="sans-serif">No data available</text>
</svg>"""
    }
}
