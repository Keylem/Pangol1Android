package com.example.pangol1_android.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectTransformGestures
import com.example.pangol1_android.core.model.DataTable

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
    var selectedDataPoint by remember { mutableStateOf<Int?>(null) }
    var selectedDataLabel by remember { mutableStateOf<String?>(null) }
    var selectedDataValue by remember { mutableStateOf<Double?>(null) }
    var showColorPicker by remember { mutableStateOf(false) }
    var customColors by remember { mutableStateOf<List<Color>>(emptyList()) }
    var chartContainerHeight by remember { mutableStateOf(350.dp) }
    
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
            .verticalScroll(rememberScrollState())
    ) {
        // Main chart with better zoom handling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartContainerHeight)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .graphicsLayer(
                    scaleX = zoomLevel,
                    scaleY = zoomLevel,
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.5f)
                )
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, gestureZoom, rotation ->
                        zoomLevel = (zoomLevel * gestureZoom).coerceIn(1.0f, 2.5f)
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
                onDataPointSelected = { index, label, value ->
                    selectedDataPoint = if (selectedDataPoint == index) null else index
                    selectedDataLabel = label
                    selectedDataValue = value
                }
            )
        }
        
        // Data Details Panel
        if (selectedDataPoint != null && selectedDataLabel != null && selectedDataValue != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📊 Data Details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        IconButton(
                            onClick = { selectedDataPoint = null },
                            modifier = Modifier.width(32.dp).height(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.height(20.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = "Item: $selectedDataLabel",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Text(
                        text = "Value: ${"%.3f".format(selectedDataValue)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    if (dataTable != null && !dataTable.isEmpty()) {
                        val yColumn = dataTable.columns.getOrNull(selectedYColumn)
                        if (yColumn != null) {
                            val yValues = dataTable.getColumnAsDoubles(yColumn)
                            val totalSum = yValues.sum()
                            val percentage = ((selectedDataValue ?: 0.0) / totalSum) * 100
                            Text(
                                text = "Percentage: ${"%.1f".format(percentage)}%",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
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
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "⚙️ Chart Controls",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Text Size Control
                ControlSlider(
                    label = "📝 Text Size",
                    value = textSizeMultiplier,
                    onValueChange = { textSizeMultiplier = it },
                    range = 0.7f..1.5f,
                    steps = 7,
                    displayValue = "${"%.1f".format(textSizeMultiplier)}x"
                )
                
                // Axis Label Size Control
                ControlSlider(
                    label = "📌 Axis Labels",
                    value = axisLabelSizeMultiplier,
                    onValueChange = { axisLabelSizeMultiplier = it },
                    range = 0.5f..2.0f,
                    steps = 14,
                    displayValue = "${"%.1f".format(axisLabelSizeMultiplier)}x"
                )
                
                // Zoom Control
                ControlSlider(
                    label = "🔍 Zoom Level",
                    value = zoomLevel,
                    onValueChange = { zoomLevel = it },
                    range = 1.0f..2.5f,
                    steps = 14,
                    displayValue = "${"%.1f".format(zoomLevel)}x"
                )
                
                // Chart Height Control
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📏 Height:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(65.dp)
                    )
                    
                    Slider(
                        value = chartContainerHeight.value,
                        onValueChange = { chartContainerHeight = it.dp },
                        valueRange = 250f..500f,
                        steps = 24,
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
                        text = if (showColorPicker) "🎨 ▼ Colors" else "🎨 ▶ Colors",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Color Palette
                if (showColorPicker) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    palette.forEach { color ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(28.dp)
                                                .background(color)
                                                .clickable {
                                                    customColors = palette
                                                    showColorPicker = false
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Clear Selection Button
                if (selectedDataPoint != null) {
                    Button(
                        onClick = { selectedDataPoint = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text("✕ Clear Selection", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ControlSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    displayValue: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(80.dp)
        )
        
        Button(
            onClick = { onValueChange((value - 0.1f).coerceAtLeast(range.start)) },
            modifier = Modifier
                .width(36.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Decrease",
                modifier = Modifier.height(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = steps,
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
        )
        
        Button(
            onClick = { onValueChange((value + 0.1f).coerceAtMost(range.endInclusive)) },
            modifier = Modifier
                .width(36.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increase",
                modifier = Modifier.height(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = displayValue,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
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
    onDataPointSelected: (Int, String, Double) -> Unit = { _, _, _ -> }
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
