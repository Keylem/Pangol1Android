package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable
import com.example.pangol1_android.svg.SVGGenerator

@Composable
fun ChartSelector(
    dataTable: DataTable?,
    getString: (String) -> String
) {
    var selectedChart by remember { mutableStateOf<String?>(null) }
    var showColumnSelection by remember { mutableStateOf(false) }
    var selectedXColumn by remember { mutableStateOf(0) }
    var selectedYColumn by remember { mutableStateOf(if ((dataTable?.columns?.size ?: 0) > 1) 1 else 0) }
    
    if (dataTable == null || dataTable.isEmpty()) {
        Text(
            text = getString("no_data"),
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp,
            color = Color.Gray
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = getString("visualization"),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        
        // Chart type buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                "bar_chart" to "bar",
                "line_chart" to "line",
                "scatter_plot" to "scatter"
            ).forEach { (labelKey, chartType) ->
                Button(
                    onClick = {
                        selectedChart = chartType
                        showColumnSelection = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(getString(labelKey), fontSize = 10.sp)
                }
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                "Pie" to "pie",
                "Histogram" to "histogram"
            ).forEach { (label, chartType) ->
                Button(
                    onClick = {
                        selectedChart = chartType
                        showColumnSelection = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(label, fontSize = 10.sp)
                }
            }
        }
        
        // Column selection dropdown
        if (showColumnSelection && dataTable.columns.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("X-Axis:", fontSize = 12.sp)
                    DropdownMenu(
                        expanded = false,
                        onDismissRequest = {}
                    ) {
                        dataTable.columns.forEachIndexed { index, column ->
                            DropdownMenuItem(
                                text = { Text(column) },
                                onClick = { selectedXColumn = index }
                            )
                        }
                    }
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text("Y-Axis:", fontSize = 12.sp)
                    DropdownMenu(
                        expanded = false,
                        onDismissRequest = {}
                    ) {
                        dataTable.columns.forEachIndexed { index, column ->
                            DropdownMenuItem(
                                text = { Text(column) },
                                onClick = { selectedYColumn = index }
                            )
                        }
                    }
                }
            }
            
            // Generate and display chart
            val svgContent = when (selectedChart) {
                "bar" -> SVGGenerator.generateBarChart(dataTable, selectedXColumn, selectedYColumn)
                "line" -> SVGGenerator.generateLineChart(dataTable, selectedXColumn, selectedYColumn)
                "scatter" -> SVGGenerator.generateScatterPlot(dataTable, selectedXColumn, selectedYColumn)
                "pie" -> SVGGenerator.generatePieChart(dataTable, selectedXColumn, selectedYColumn)
                "histogram" -> SVGGenerator.generateHistogram(dataTable, selectedXColumn)
                else -> ""
            }
            
            if (svgContent.isNotEmpty()) {
                SVGView(
                    svgContent = svgContent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}
