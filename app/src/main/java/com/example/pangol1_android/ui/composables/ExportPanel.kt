package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pangol1_android.core.model.DataTable

@Composable
fun ExportPanel(
    dataTable: DataTable?,
    onExportCSV: () -> Unit,
    onExportSVG: () -> Unit,
    getString: (String) -> String,
    modifier: Modifier = Modifier
) {
    var showExportMessage by remember { mutableStateOf(false) }
    var exportMessage by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = getString("export"),
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    onExportCSV()
                    exportMessage = "Data exported to CSV"
                    showExportMessage = true
                },
                modifier = Modifier.weight(1f),
                enabled = dataTable != null && !dataTable.isEmpty()
            ) {
                Icon(Icons.Filled.Download, contentDescription = "Export CSV")
                Text("CSV", modifier = Modifier.padding(start = 8.dp))
            }
            
            Button(
                onClick = {
                    onExportSVG()
                    exportMessage = "Visualization exported to SVG"
                    showExportMessage = true
                },
                modifier = Modifier.weight(1f),
                enabled = dataTable != null && !dataTable.isEmpty()
            ) {
                Icon(Icons.Filled.Share, contentDescription = "Export SVG")
                Text("SVG", modifier = Modifier.padding(start = 8.dp))
            }
        }
        
        if (showExportMessage) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFC8E6C9))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exportMessage,
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { showExportMessage = false },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("OK", fontSize = 11.sp)
                }
            }
        }
    }
}
