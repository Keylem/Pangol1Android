package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable


@Composable
fun ChartTypeButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (isSelected) null else androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ChartSelector(
    dataTable: DataTable?,
    getString: (String) -> String
) {
    val isDarkMode = isSystemInDarkTheme()
    var selectedChart by remember { mutableStateOf("bar") }
    var selectedXColumn by remember { mutableStateOf(0) }
    var selectedYColumn by remember { mutableStateOf(if ((dataTable?.columns?.size ?: 0) > 1) 1 else 0) }
    var expandedXAxis by remember { mutableStateOf(false) }
    var expandedYAxis by remember { mutableStateOf(false) }
    
    if (dataTable == null || dataTable.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = getString("no_data"),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        Text(
            text = getString("visualization"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
        
        // Chart type selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = getString("visualization"),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChartTypeButton(
                        label = getString("bar_chart"),
                        icon = Icons.Filled.BarChart,
                        isSelected = selectedChart == "bar",
                        onClick = { selectedChart = "bar" },
                        modifier = Modifier.weight(1f)
                    )
                    ChartTypeButton(
                        label = getString("line_chart"),
                        icon = Icons.Filled.TrendingUp,
                        isSelected = selectedChart == "line",
                        onClick = { selectedChart = "line" },
                        modifier = Modifier.weight(1f)
                    )
                    ChartTypeButton(
                        label = getString("scatter_plot"),
                        icon = Icons.Filled.BarChart,
                        isSelected = selectedChart == "scatter",
                        onClick = { selectedChart = "scatter" },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ChartTypeButton(
                        label = getString("pie_chart"),
                        icon = Icons.Filled.PieChart,
                        isSelected = selectedChart == "pie",
                        onClick = { selectedChart = "pie" },
                        modifier = Modifier.weight(1f)
                    )
                    ChartTypeButton(
                        label = getString("histogram"),
                        icon = Icons.Filled.BarChart,
                        isSelected = selectedChart == "histogram",
                        onClick = { selectedChart = "histogram" },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    ) {}
                }
            }
        }
        
        // Column selection
        if (dataTable.columns.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // X-axis selection
                    if (selectedChart != "pie" && selectedChart != "histogram") {
                        Text(
                            text = getString("x_axis"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        
                        OutlinedButton(
                            onClick = { expandedXAxis = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Text(
                                dataTable.columns.getOrNull(selectedXColumn) ?: "Select",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                        
                        DropdownMenu(
                            expanded = expandedXAxis,
                            onDismissRequest = { expandedXAxis = false }
                        ) {
                            dataTable.columns.forEachIndexed { index, column ->
                                DropdownMenuItem(
                                    text = { Text(column) },
                                    onClick = {
                                        selectedXColumn = index
                                        expandedXAxis = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Y-axis selection
                    if (selectedChart != "pie" && selectedChart != "histogram") {
                        Text(
                            text = getString("y_axis"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    } else {
                        Text(
                            text = getString("select_columns"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { expandedYAxis = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            dataTable.columns.getOrNull(selectedYColumn) ?: "Select",
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                    
                    DropdownMenu(
                        expanded = expandedYAxis,
                        onDismissRequest = { expandedYAxis = false }
                    ) {
                        dataTable.columns.forEachIndexed { index, column ->
                            DropdownMenuItem(
                                text = { Text(column) },
                                onClick = {
                                    selectedYColumn = index
                                    expandedYAxis = false
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Display chart using InteractiveChartView
        InteractiveChartView(
            dataTable = dataTable,
            selectedXColumn = selectedXColumn,
            selectedYColumn = selectedYColumn,
            chartType = selectedChart,
            isDarkMode = isDarkMode
        )
    }
}
