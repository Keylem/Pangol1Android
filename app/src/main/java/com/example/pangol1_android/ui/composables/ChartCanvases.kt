package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BarChart(
    dataTable: DataTable,
    xColumnIndex: Int,
    yColumnIndex: Int,
    isDarkMode: Boolean,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable.isEmpty()) return
    
    val xLabels = dataTable.getColumn(dataTable.columns[xColumnIndex])
    val yValues = dataTable.getColumnAsDoubles(dataTable.columns[yColumnIndex])
    
    if (yValues.isEmpty()) return
    
    val maxValue = yValues.maxOrNull() ?: 1.0
    val barColor = customColors.getOrNull(0) ?: MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val gridColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
    val baseFontSize = 12f * textSizeMultiplier
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "Bar Chart: ${dataTable.columns[yColumnIndex]}",
            fontSize = (14.sp * textSizeMultiplier),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val chartWidth = size.width * 0.85f
            val chartHeight = size.height * 0.80f
            val paddingLeft = 40f
            val paddingBottom = 50f
            val paddingTop = 10f
            
            // Draw grid and axes
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, paddingTop),
                end = Offset(paddingLeft, size.height - paddingBottom),
                strokeWidth = 2f
            )
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, size.height - paddingBottom),
                end = Offset(paddingLeft + chartWidth, size.height - paddingBottom),
                strokeWidth = 2f
            )
            
            val barWidth = chartWidth / yValues.size / 1.3f
            val spaceBetween = chartWidth / yValues.size
            
            yValues.forEachIndexed { index, value ->
                val barHeight = ((value / maxValue) * chartHeight).toFloat()
                val x = paddingLeft + (spaceBetween * index) + (spaceBetween - barWidth) / 2
                val y = size.height - paddingBottom - barHeight
                
                // Draw bar with highlight if selected
                val isSelected = selectedDataPoint == index
                drawRect(
                    color = if (isSelected) barColor.copy(alpha = 0.8f) else barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
                
                // Draw selection border if selected
                if (isSelected) {
                    drawRect(
                        color = Color.Yellow,
                        topLeft = Offset(x - 2, y - 2),
                        size = Size(barWidth + 4, barHeight + 4),
                        style = Stroke(width = 3f)
                    )
                }
                
                // Draw value label
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "%.1f".format(value),
                        x + barWidth / 2,
                        y - 5,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = baseFontSize
                        }
                    )
                }
            }
            
            // Draw x-axis labels
            yValues.indices.forEach { index ->
                val x = paddingLeft + (spaceBetween * index) + spaceBetween / 2
                val label = xLabels.getOrNull(index) ?: "Item $index"
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        label.take(8),
                        x,
                        size.height - paddingBottom + 20,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 11f
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(
    dataTable: DataTable,
    xColumnIndex: Int,
    yColumnIndex: Int,
    isDarkMode: Boolean,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable.isEmpty()) return
    
    val xLabels = dataTable.getColumn(dataTable.columns[xColumnIndex])
    val yValues = dataTable.getColumnAsDoubles(dataTable.columns[yColumnIndex]).filter { it > 0 }
    
    if (yValues.isEmpty()) return
    
    val textColor = MaterialTheme.colorScheme.onSurface
    val totalValue = yValues.sum()
    val baseFontSize = 13f * textSizeMultiplier
    val colors = if (customColors.isNotEmpty()) customColors else listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFC7CEEA)
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "Pie Chart: ${dataTable.columns[yColumnIndex]}",
            fontSize = (14.sp * textSizeMultiplier),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            val centerX = size.width * 0.45f
            val centerY = size.height * 0.50f
            val radius = minOf(size.width, size.height) * 0.30f
            
            var currentAngle = -90f
            yValues.forEachIndexed { index, value ->
                val sliceAngle = (value / totalValue * 360.0).toFloat()
                val sliceColor = colors[index % colors.size]
                val isSelected = selectedDataPoint == index
                
                // Draw slice with potential expansion if selected
                val expandRadius = if (isSelected) radius * 1.1f else radius
                val offsetX = if (isSelected) cos(Math.toRadians((currentAngle + sliceAngle / 2).toDouble())).toFloat() * radius * 0.1f else 0f
                val offsetY = if (isSelected) sin(Math.toRadians((currentAngle + sliceAngle / 2).toDouble())).toFloat() * radius * 0.1f else 0f
                
                val path = Path().apply {
                    moveTo(centerX + offsetX, centerY + offsetY)
                    arcTo(
                        Rect(
                            centerX + offsetX - expandRadius,
                            centerY + offsetY - expandRadius,
                            centerX + offsetX + expandRadius,
                            centerY + offsetY + expandRadius
                        ),
                        currentAngle,
                        sliceAngle,
                        false
                    )
                    close()
                }
                drawPath(path, if (isSelected) sliceColor.copy(alpha = 0.9f) else sliceColor)
                
                // Draw percentage label
                val labelAngle = currentAngle + sliceAngle / 2
                val labelRadius = radius * 0.65f
                val labelX = centerX + labelRadius * cos(Math.toRadians(labelAngle.toDouble())).toFloat()
                val labelY = centerY + labelRadius * sin(Math.toRadians(labelAngle.toDouble())).toFloat()
                val percentage = (value / totalValue) * 100
                
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "%.0f%%".format(percentage),
                        labelX,
                        labelY,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = baseFontSize
                            isFakeBoldText = true
                        }
                    )
                }
                
                currentAngle += sliceAngle
            }
            
            // Draw legend
            var legendY = size.height - 60f
            yValues.indices.forEach { index ->
                if (index < 4) { // Show first 4 in legend
                    val legendColor = colors[index % colors.size]
                    val label = xLabels.getOrNull(index) ?: "Item $index"
                    val legendX = 10f
                    
                    drawRect(
                        color = legendColor,
                        topLeft = Offset(legendX, legendY),
                        size = Size(10f, 10f)
                    )
                    
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label.take(15),
                            legendX + 15,
                            legendY + 10,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                                textSize = baseFontSize * 0.85f * axisLabelSizeMultiplier
                            }
                        )
                    }
                    
                    legendY -= 18f
                }
            }
        }
    }
}

@Composable
fun LineChart(
    dataTable: DataTable,
    xColumnIndex: Int,
    yColumnIndex: Int,
    isDarkMode: Boolean,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable.isEmpty()) return
    
    val xLabels = dataTable.getColumn(dataTable.columns[xColumnIndex])
    val yValues = dataTable.getColumnAsDoubles(dataTable.columns[yColumnIndex])
    
    if (yValues.isEmpty()) return
    
    val maxValue = yValues.maxOrNull() ?: 1.0
    val minValue = yValues.minOrNull() ?: 0.0
    val lineColor = customColors.getOrNull(0) ?: MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val gridColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
    val baseFontSize = 11f * textSizeMultiplier
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "Line Chart: ${dataTable.columns[yColumnIndex]}",
            fontSize = (14.sp * textSizeMultiplier),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val chartWidth = size.width * 0.85f
            val chartHeight = size.height * 0.80f
            val paddingLeft = 40f
            val paddingBottom = 50f
            val paddingTop = 10f
            val range = (maxValue - minValue).toFloat()
            
            // Draw grid and axes
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, paddingTop),
                end = Offset(paddingLeft, size.height - paddingBottom),
                strokeWidth = 2f
            )
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, size.height - paddingBottom),
                end = Offset(paddingLeft + chartWidth, size.height - paddingBottom),
                strokeWidth = 2f
            )
            
            // Draw horizontal grid lines
            repeat(5) { i ->
                val y = paddingTop + (chartHeight / 4) * i
                drawLine(
                    color = gridColor.copy(alpha = 0.3f),
                    start = Offset(paddingLeft, y),
                    end = Offset(paddingLeft + chartWidth, y),
                    strokeWidth = 1f
                )
            }
            
            val pointRadius = 4f
            val pointSpacing = chartWidth / (yValues.size - 1).coerceAtLeast(1)
            
            // Draw line and points
            for (i in 0 until yValues.size - 1) {
                val x1 = paddingLeft + (pointSpacing * i)
                val y1 = size.height - paddingBottom - (((yValues[i] - minValue) / range) * chartHeight).toFloat()
                
                val x2 = paddingLeft + (pointSpacing * (i + 1))
                val y2 = size.height - paddingBottom - (((yValues[i + 1] - minValue) / range) * chartHeight).toFloat()
                
                drawLine(
                    color = lineColor,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 3f
                )
                
                val isSelected = selectedDataPoint == i
                drawCircle(color = if (isSelected) Color.Yellow else lineColor, radius = if (isSelected) pointRadius * 1.5f else pointRadius, center = Offset(x1, y1))
            }
            
            // Draw last point
            if (yValues.isNotEmpty()) {
                val lastX = paddingLeft + (pointSpacing * (yValues.size - 1))
                val lastY = size.height - paddingBottom - (((yValues.last() - minValue) / range) * chartHeight).toFloat()
                val isLastSelected = selectedDataPoint == yValues.size - 1
                drawCircle(color = if (isLastSelected) Color.Yellow else lineColor, radius = if (isLastSelected) pointRadius * 1.5f else pointRadius, center = Offset(lastX, lastY))
            }
            
            // Draw x-axis labels
            yValues.indices.forEach { index ->
                val x = paddingLeft + (pointSpacing * index)
                val label = xLabels.getOrNull(index) ?: "Item $index"
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        label.take(8),
                        x,
                        size.height - paddingBottom + 20,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = baseFontSize * 0.9f * axisLabelSizeMultiplier
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScatterPlot(
    dataTable: DataTable,
    xColumnIndex: Int,
    yColumnIndex: Int,
    isDarkMode: Boolean,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable.isEmpty()) return
    
    val xValues = dataTable.getColumnAsDoubles(dataTable.columns[xColumnIndex])
    val yValues = dataTable.getColumnAsDoubles(dataTable.columns[yColumnIndex])
    
    if (xValues.isEmpty() || yValues.isEmpty()) return
    
    val maxX = xValues.maxOrNull() ?: 1.0
    val minX = xValues.minOrNull() ?: 0.0
    val maxY = yValues.maxOrNull() ?: 1.0
    val minY = yValues.minOrNull() ?: 0.0
    val rangeX = (maxX - minX).toFloat()
    val rangeY = (maxY - minY).toFloat()
    val pointColor = customColors.getOrNull(0) ?: MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val gridColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
    val baseFontSize = 11f * textSizeMultiplier
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "Scatter Plot: ${dataTable.columns[yColumnIndex]}",
            fontSize = (14.sp * textSizeMultiplier),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val chartWidth = size.width * 0.85f
            val chartHeight = size.height * 0.80f
            val paddingLeft = 40f
            val paddingBottom = 50f
            val paddingTop = 10f

            
            // Draw axes
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, paddingTop),
                end = Offset(paddingLeft, size.height - paddingBottom),
                strokeWidth = 2f
            )
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, size.height - paddingBottom),
                end = Offset(paddingLeft + chartWidth, size.height - paddingBottom),
                strokeWidth = 2f
            )
            
            // Draw grid
            repeat(5) { i ->
                val y = paddingTop + (chartHeight / 4) * i
                drawLine(
                    color = gridColor.copy(alpha = 0.3f),
                    start = Offset(paddingLeft, y),
                    end = Offset(paddingLeft + chartWidth, y),
                    strokeWidth = 1f
                )
            }
            
            // Draw points
            repeat(kotlin.math.min(xValues.size, yValues.size)) { index ->
                val x = paddingLeft + (((xValues[index] - minX) / rangeX) * chartWidth).toFloat()
                val y = size.height - paddingBottom - (((yValues[index] - minY) / rangeY) * chartHeight).toFloat()
                val isSelected = selectedDataPoint == index
                drawCircle(color = if (isSelected) Color.Yellow else pointColor, radius = if (isSelected) 8f else 5f, center = Offset(x, y))
            }
        }
    }
}

@Composable
fun Histogram(
    dataTable: DataTable,
    columnIndex: Int,
    isDarkMode: Boolean,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable.isEmpty()) return
    
    val yValues = dataTable.getColumnAsDoubles(dataTable.columns[columnIndex])
    
    if (yValues.isEmpty()) return
    
    val barColor = customColors.getOrNull(0) ?: MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val gridColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
    val baseFontSize = 12f * textSizeMultiplier
    
    // Create bins (10 bins by default)
    val minValue = yValues.minOrNull() ?: 0.0
    val maxValue = yValues.maxOrNull() ?: 1.0
    val binCount = 10
    val binWidth = (maxValue - minValue) / binCount
    val bins = MutableList(binCount) { 0 }
    
    yValues.forEach { value ->
                val binIndex = (((value - minValue) / binWidth).toInt()).coerceIn(0, binCount - 1)
        bins[binIndex]++
    }
    
    val maxCount = bins.maxOrNull() ?: 1
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "Histogram: ${dataTable.columns[columnIndex]}",
            fontSize = (14.sp * textSizeMultiplier),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val chartWidth = size.width * 0.85f
            val chartHeight = size.height * 0.80f
            val paddingLeft = 40f
            val paddingBottom = 50f
            val paddingTop = 10f
            
            // Draw axes
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, paddingTop),
                end = Offset(paddingLeft, size.height - paddingBottom),
                strokeWidth = 2f
            )
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, size.height - paddingBottom),
                end = Offset(paddingLeft + chartWidth, size.height - paddingBottom),
                strokeWidth = 2f
            )
            
            val barWidth = chartWidth / binCount / 1.1f
            val spaceBetween = chartWidth / binCount
            
            bins.forEachIndexed { index, count ->
                val barHeight = ((count.toDouble() / maxCount) * chartHeight).toFloat()
                val x = paddingLeft + (spaceBetween * index) + (spaceBetween - barWidth) / 2
                val y = size.height - paddingBottom - barHeight
                
                val isSelected = selectedDataPoint == index
                // Draw bar with highlight if selected
                drawRect(
                    color = if (isSelected) barColor.copy(alpha = 0.8f) else barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
                
                if (isSelected) {
                    drawRect(
                        color = Color.Yellow,
                        topLeft = Offset(x - 2, y - 2),
                        size = Size(barWidth + 4, barHeight + 4),
                        style = Stroke(width = 3f)
                    )
                }
                
                // Draw count label
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        count.toString(),
                        x + barWidth / 2,
                        y - 5,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = baseFontSize
                        }
                    )
                }
            }
            
            // Draw bin labels
            bins.indices.forEach { index ->
                val x = paddingLeft + (spaceBetween * index) + spaceBetween / 2
                val binStart = minValue + (binWidth * index)
                val label = "%.1f".format(binStart)
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        label,
                        x,
                        size.height - paddingBottom + 20,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.valueOf(textColor.red, textColor.green, textColor.blue, textColor.alpha).toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = baseFontSize * 0.9f * axisLabelSizeMultiplier
                        }
                    )
                }
            }
        }
    }
}
