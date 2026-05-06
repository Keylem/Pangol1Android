package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable

@Composable
fun SimpleChartView(
    dataTable: DataTable?,
    selectedXColumn: Int = 0,
    selectedYColumn: Int = 1,
    chartType: String = "bar",
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    if (dataTable == null || dataTable.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "No data to display",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val xColumn = dataTable.columns.getOrNull(selectedXColumn)
    val yColumn = dataTable.columns.getOrNull(selectedYColumn)

    if (xColumn == null || yColumn == null) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Invalid column selection",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val xLabels = dataTable.getColumn(xColumn)
    val yValues = dataTable.getColumnAsDoubles(yColumn)

    if (yValues.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "No numeric data found",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
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
            isDarkMode = isDarkMode
        )
        "line" -> LineChart(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode
        )
        "scatter" -> ScatterPlot(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode
        )
        "pie" -> PieChart(
            dataTable = dataTable,
            xColumnIndex = selectedXColumn,
            yColumnIndex = selectedYColumn,
            isDarkMode = isDarkMode
        )
        "histogram" -> Histogram(
            dataTable = dataTable,
            columnIndex = selectedYColumn,
            isDarkMode = isDarkMode
        )
        else -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Unknown chart type: $chartType",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
