package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable

@Composable
fun DataTableView(
    dataTable: DataTable,
    modifier: Modifier = Modifier
) {
    if (dataTable.isEmpty()) {
        Text(
            text = "No data to display",
            modifier = modifier.padding(16.dp),
            fontSize = 14.sp,
            color = Color.Gray
        )
        return
    }
    
    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            dataTable.columns.forEach { column ->
                Text(
                    text = column,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
        
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        
        // Rows
        LazyColumn {
            items(dataTable.rows.take(100)) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dataTable.columns.forEach { column ->
                        Text(
                            text = row.get(column) ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            }
        }
        
        // Row count indicator
        if (dataTable.rows.size > 100) {
            Text(
                text = "Showing 100 of ${dataTable.rows.size} rows",
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
