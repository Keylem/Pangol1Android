package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable
import kotlin.math.min

@Composable
fun InteractiveChartView(
    dataTable: DataTable?,
    selectedXColumn: Int = 0,
    selectedYColumn: Int = 1,
    chartType: String = "bar",
    isDarkMode: Boolean = false
) {
    var textSizeMultiplier by remember { mutableStateOf(1f) }
    var axisLabelSizeMultiplier by remember { mutableStateOf(1f) }
    var zoomLevel by remember { mutableStateOf(1f) }
    var panOffsetX by remember { mutableStateOf(0f) }
    var panOffsetY by remember { mutableStateOf(0f) }
    var selectedDataPoint by remember { mutableStateOf<Int?>(null) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showAxisLabelSettings by remember { mutableStateOf(false) }
    var customColors by remember { mutableStateOf<List<Color>>(emptyList()) }
    var chartContainerHeight by remember { mutableStateOf(300.dp) }
    
    // Initialize default colors
    if (customColors.isEmpty()) {
        customColors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            Color(0xFFFF6B6B),
            Color(0xFF4ECDC4),
            Color(0xFFFFE66D),
            Color(0xFF95E1D3),
            Color(0xFFC7CEEA)
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Main chart with zoom and pan capability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartContainerHeight)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .graphicsLayer(
                    scaleX = zoomLevel,
                    scaleY = zoomLevel,
                    translationX = panOffsetX,
                    translationY = panOffsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, gestureZoom, rotation ->
                        // Update zoom
                        zoomLevel = (zoomLevel * gestureZoom).coerceIn(0.8f, 2.0f)
                        // Update pan
                        panOffsetX += pan.x
                        panOffsetY += pan.y
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Convert offset to chart coordinates considering zoom and pan
                        val chartX = (offset.x - panOffsetX) / zoomLevel
                        val chartY = (offset.y - panOffsetY) / zoomLevel
                        
                        // Estimate which data point was tapped (simplified approach)
                        // This will be refined based on actual chart dimensions
                        if (chartX in 40f..350f && chartY in 20f..280f) {
                            // Rough mapping: divide chart into data point zones
                            val chartWidth = 310f
                            val dataPointIndex = ((chartX - 40f) / chartWidth * 10).toInt()
                            val selectedIndex = dataPointIndex.coerceIn(0, 9)
                            selectedDataPoint = if (selectedDataPoint == selectedIndex) null else selectedIndex
                        }
                    }
                }
        ) {
            SimpleChartViewWithEnhancements(
                dataTable = dataTable,
                selectedXColumn = selectedXColumn,
                selectedYColumn = selectedYColumn,
                chartType = chartType,
                isDarkMode = isDarkMode,
                textSizeMultiplier = textSizeMultiplier,
                axisLabelSizeMultiplier = axisLabelSizeMultiplier,
                selectedDataPoint = selectedDataPoint,
                customColors = customColors,
                onDataPointSelected = { selectedDataPoint = it }
            )
        }
        
        // Tooltip/Explanation popup when data point is selected
        if (selectedDataPoint != null && dataTable != null && !dataTable.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Value Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    val yColumn = dataTable.columns.getOrNull(selectedYColumn)
                    val xColumn = dataTable.columns.getOrNull(selectedXColumn)
                    if (yColumn != null && xColumn != null) {
                        val yValues = dataTable.getColumnAsDoubles(yColumn)
                        val xLabels = dataTable.getColumn(xColumn)
                        
                        if (selectedDataPoint!! < yValues.size) {
                            val value = yValues[selectedDataPoint!!]
                            val label = xLabels.getOrNull(selectedDataPoint!!) ?: "Item ${selectedDataPoint!!}"
                            
                            Text(
                                text = "$xColumn: $label",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "$yColumn: ${"%.2f".format(value)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            
                            val allYValues = dataTable.getColumnAsDoubles(yColumn)
                            val totalSum = allYValues.sum()
                            val percentage = (value / totalSum) * 100
                            Text(
                                text = "Percentage: ${"%.1f".format(percentage)}%",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Controls Panel
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Chart Controls",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Text Size Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Text Size:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(60.dp)
                    )
                    
                    Button(
                        onClick = { textSizeMultiplier = (textSizeMultiplier - 0.1f).coerceAtLeast(0.7f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Slider(
                        value = textSizeMultiplier,
                        onValueChange = { textSizeMultiplier = it },
                        valueRange = 0.7f..1.5f,
                        steps = 7,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    )
                    
                    Button(
                        onClick = { textSizeMultiplier = (textSizeMultiplier + 0.1f).coerceAtMost(1.5f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Text(
                        text = "${"%.1f".format(textSizeMultiplier)}x",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(35.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Axis Label Size Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Axis Labels:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(60.dp)
                    )
                    
                    Button(
                        onClick = { axisLabelSizeMultiplier = (axisLabelSizeMultiplier - 0.1f).coerceAtLeast(0.5f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Slider(
                        value = axisLabelSizeMultiplier,
                        onValueChange = { axisLabelSizeMultiplier = it },
                        valueRange = 0.5f..2.0f,
                        steps = 14,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    )
                    
                    Button(
                        onClick = { axisLabelSizeMultiplier = (axisLabelSizeMultiplier + 0.1f).coerceAtMost(2.0f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Text(
                        text = "${"%.1f".format(axisLabelSizeMultiplier)}x",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(35.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Zoom Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Zoom:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(60.dp)
                    )
                    
                    Button(
                        onClick = { zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(0.8f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Zoom Out",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Slider(
                        value = zoomLevel,
                        onValueChange = { zoomLevel = it },
                        valueRange = 0.8f..2.0f,
                        steps = 11,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    )
                    
                    Button(
                        onClick = { zoomLevel = (zoomLevel + 0.1f).coerceAtMost(2.0f) },
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Zoom In",
                            modifier = Modifier.height(16.dp)
                        )
                    }
                    
                    Text(
                        text = "${"%.1f".format(zoomLevel)}x",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(35.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Chart Height Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Height:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(60.dp)
                    )
                    
                    Slider(
                        value = chartContainerHeight.value,
                        onValueChange = { chartContainerHeight = it.dp },
                        valueRange = 200f..500f,
                        steps = 29,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    )
                    
                    Text(
                        text = "${"%.0f".format(chartContainerHeight.value)}dp",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(50.dp),
                        textAlign = TextAlign.End
                    )
                }
                
                // Color Customization Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showColorPicker = !showColorPicker }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showColorPicker) "▼ Colors" else "▶ Colors",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Color Palette (expandable)
                if (showColorPicker) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Preset color palettes
                        val colorPalettes = listOf(
                            "Default" to listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.tertiary,
                                Color(0xFFFF6B6B),
                                Color(0xFF4ECDC4),
                                Color(0xFFFFE66D),
                                Color(0xFF95E1D3),
                                Color(0xFFC7CEEA)
                            ),
                            "Warm" to listOf(
                                Color(0xFFE8764C),
                                Color(0xFFF4A460),
                                Color(0xFFFFA07A),
                                Color(0xFFFFB347),
                                Color(0xFFDEB887),
                                Color(0xFFD2691E),
                                Color(0xFFCD853F),
                                Color(0xFFCC5500)
                            ),
                            "Cool" to listOf(
                                Color(0xFF4A90E2),
                                Color(0xFF357ABD),
                                Color(0xFF00B4D8),
                                Color(0xFF0096C7),
                                Color(0xFF0077B6),
                                Color(0xFF00B8D4),
                                Color(0xFF00ACC1),
                                Color(0xFF0097A7)
                            ),
                            "Vibrant" to listOf(
                                Color(0xFFFF0000),
                                Color(0xFFFF7F00),
                                Color(0xFFFFFF00),
                                Color(0xFF00FF00),
                                Color(0xFF0000FF),
                                Color(0xFF4B0082),
                                Color(0xFF9400D3),
                                Color(0xFFFF1493)
                            )
                        )
                        
                        colorPalettes.forEach { (name, palette) ->
                            Column {
                                Text(
                                    text = name,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    palette.forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(24.dp)
                                                .background(color)
                                                .clickable {
                                                    customColors = palette
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Clear selection button
                if (selectedDataPoint != null) {
                    Button(
                        onClick = { selectedDataPoint = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear Selection", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleChartViewWithEnhancements(
    dataTable: DataTable?,
    selectedXColumn: Int = 0,
    selectedYColumn: Int = 1,
    chartType: String = "bar",
    isDarkMode: Boolean = false,
    textSizeMultiplier: Float = 1f,
    axisLabelSizeMultiplier: Float = 1f,
    selectedDataPoint: Int? = null,
    customColors: List<Color> = emptyList(),
    onDataPointSelected: (Int?) -> Unit = {}
) {
    if (dataTable == null || dataTable.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "No data to display",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = (16.sp * textSizeMultiplier),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val xColumn = dataTable.columns.getOrNull(selectedXColumn)
    val yColumn = dataTable.columns.getOrNull(selectedYColumn)

    if (xColumn == null || yColumn == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Invalid column selection",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = (16.sp * textSizeMultiplier),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val xLabels = dataTable.getColumn(xColumn)
    val yValues = dataTable.getColumnAsDoubles(yColumn)

    if (yValues.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "No numeric data found",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = (16.sp * textSizeMultiplier),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    // Render appropriate chart based on type
    when (chartType) {
        "bar" -> BarChart(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode,
            textSizeMultiplier = textSizeMultiplier,
            axisLabelSizeMultiplier = axisLabelSizeMultiplier,
            selectedDataPoint = selectedDataPoint,
            customColors = customColors,
            onDataPointSelected = onDataPointSelected
        )
        "line" -> LineChart(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode,
            textSizeMultiplier = textSizeMultiplier,
            axisLabelSizeMultiplier = axisLabelSizeMultiplier,
            selectedDataPoint = selectedDataPoint,
            customColors = customColors,
            onDataPointSelected = onDataPointSelected
        )
        "scatter" -> ScatterPlot(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode,
            textSizeMultiplier = textSizeMultiplier,
            axisLabelSizeMultiplier = axisLabelSizeMultiplier,
            selectedDataPoint = selectedDataPoint,
            customColors = customColors,
            onDataPointSelected = onDataPointSelected
        )
        "pie" -> PieChart(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode,
            textSizeMultiplier = textSizeMultiplier,
            axisLabelSizeMultiplier = axisLabelSizeMultiplier,
            selectedDataPoint = selectedDataPoint,
            customColors = customColors,
            onDataPointSelected = onDataPointSelected
        )
        "histogram" -> Histogram(
            dataTable = dataTable,
            columnIndex = selectedYColumn,
            isDarkMode = isDarkMode,
            textSizeMultiplier = textSizeMultiplier,
            axisLabelSizeMultiplier = axisLabelSizeMultiplier,
            selectedDataPoint = selectedDataPoint,
            customColors = customColors,
            onDataPointSelected = onDataPointSelected
        )
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Unknown chart type: $chartType",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = (16.sp * textSizeMultiplier),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
