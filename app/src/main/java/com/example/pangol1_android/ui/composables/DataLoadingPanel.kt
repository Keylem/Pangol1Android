package com.example.pangol1_android.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DataLoadingPanel(
    onLoadUrl: (String) -> Unit,
    onLoadDemoSales: () -> Unit,
    onLoadDemoTemperature: () -> Unit,
    onLoadDemoPopulation: () -> Unit,
    onLoadDemoGrades: () -> Unit,
    onLoadDemoTraffic: () -> Unit,
    onLoadCsvContent: (String) -> Unit,
    onPickFile: (() -> Unit)? = null,
    isLoading: Boolean,
    error: String?,
    onErrorDismiss: () -> Unit,
    getString: (String) -> String
) {
    val isDarkMode = isSystemInDarkTheme()
    var urlInput by remember { mutableStateOf("") }
    var csvInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tab selector with modern styling
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = { Icon(Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.padding(bottom = 4.dp)) },
                text = { Text(getString("load_url"), fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = { Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.padding(bottom = 4.dp)) },
                text = { Text(getString("paste_csv"), fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = { Icon(Icons.Filled.FolderOpen, contentDescription = null, modifier = Modifier.padding(bottom = 4.dp)) },
                text = { Text("Import", fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                icon = { Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.padding(bottom = 4.dp)) },
                text = { Text(getString("demo_data"), fontSize = 11.sp) }
            )
        }
        
        // Tab content with smooth transitions
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { 300 }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) { tab ->
            when (tab) {
                0 -> URLLoadTab(
                    urlInput, 
                    { urlInput = it }, 
                    onLoadUrl, 
                    isLoading, 
                    getString, 
                    isDarkMode
                )
                1 -> CSVPasteTab(
                    csvInput, 
                    { csvInput = it }, 
                    { onLoadCsvContent(csvInput) },
                    isLoading, 
                    getString, 
                    isDarkMode
                )
                2 -> FilePickerTab(
                    onPickFile,
                    getString,
                    isDarkMode
                )
                3 -> DemoDataTab(
                    onLoadDemoSales, 
                    onLoadDemoTemperature, 
                    onLoadDemoPopulation, 
                    onLoadDemoGrades, 
                    onLoadDemoTraffic, 
                    isLoading, 
                    getString, 
                    isDarkMode
                )
            }
        }
        
        // Error display with better styling
        if (error != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = onErrorDismiss,
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(getString("cancel"), fontSize = 11.sp)
                    }
                }
            }
        }
        
        Text("", modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun URLLoadTab(
    urlInput: String,
    onUrlChange: (String) -> Unit,
    onLoadUrl: (String) -> Unit,
    isLoading: Boolean,
    getString: (String) -> String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = getString("load_url"),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextField(
                value = urlInput,
                onValueChange = onUrlChange,
                label = { Text(getString("enter_url")) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )
            
            Button(
                onClick = { onLoadUrl(urlInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                enabled = !isLoading && urlInput.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    if (isLoading) getString("loading") else getString("load"),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CSVPasteTab(
    csvInput: String,
    onCsvChange: (String) -> Unit,
    onLoadCsv: () -> Unit,
    isLoading: Boolean,
    getString: (String) -> String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = getString("paste_csv"),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextField(
                value = csvInput,
                onValueChange = onCsvChange,
                label = { Text("CSV Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                enabled = !isLoading,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )
            
            Button(
                onClick = onLoadCsv,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                enabled = !isLoading && csvInput.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    if (isLoading) getString("loading") else getString("load"),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FilePickerTab(
    onPickFile: (() -> Unit)?,
    getString: (String) -> String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Import CSV File",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Select a CSV file from your device",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Button(
                onClick = { onPickFile?.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Filled.FolderOpen,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    "Choose File",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DemoDataTab(
    onLoadDemoSales: () -> Unit,
    onLoadDemoTemperature: () -> Unit,
    onLoadDemoPopulation: () -> Unit,
    onLoadDemoGrades: () -> Unit,
    onLoadDemoTraffic: () -> Unit,
    isLoading: Boolean,
    getString: (String) -> String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = getString("demo_data"),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Demo buttons in responsive grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DemoButton(
                    label = getString("demo_sales"),
                    onClick = onLoadDemoSales,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
                DemoButton(
                    label = getString("demo_temperature"),
                    onClick = onLoadDemoTemperature,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DemoButton(
                    label = getString("demo_population"),
                    onClick = onLoadDemoPopulation,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
                DemoButton(
                    label = getString("demo_grades"),
                    onClick = onLoadDemoGrades,
                    isLoading = isLoading,
                    modifier = Modifier.weight(1f)
                )
            }
            
            DemoButton(
                label = getString("demo_traffic"),
                onClick = onLoadDemoTraffic,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DemoButton(
    label: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
