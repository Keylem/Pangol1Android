package com.example.pangol1_android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pangol1_android.ui.PangolViewModel
import com.example.pangol1_android.ui.composables.ChartSelector
import com.example.pangol1_android.ui.composables.DataLoadingPanel
import com.example.pangol1_android.ui.composables.DataTableView
import com.example.pangol1_android.ui.composables.ExportPanel
import com.example.pangol1_android.ui.composables.SettingsPanel

@Composable
fun MainScreen(viewModel: PangolViewModel, modifier: Modifier = Modifier) {
    val currentTable by viewModel.currentTable.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
    
    val currentNavItem = remember { mutableStateOf(0) }
    val currentTabIndex = remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, "Data") },
                    label = { Text("Data") },
                    selected = currentNavItem.value == 0,
                    onClick = { currentNavItem.value = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, "Settings") },
                    label = { Text("Settings") },
                    selected = currentNavItem.value == 1,
                    onClick = { currentNavItem.value = 1 }
                )
            }
        }
    ) { paddingValues ->
        when (currentNavItem.value) {
            0 -> {
                // Data screen with tabs
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TabRow(selectedTabIndex = currentTabIndex.value) {
                        Tab(
                            selected = currentTabIndex.value == 0,
                            onClick = { currentTabIndex.value = 0 },
                            text = { Text(viewModel.getString("load_data")) }
                        )
                        Tab(
                            selected = currentTabIndex.value == 1,
                            onClick = { currentTabIndex.value = 1 },
                            text = { Text(viewModel.getString("data_table")) }
                        )
                        Tab(
                            selected = currentTabIndex.value == 2,
                            onClick = { currentTabIndex.value = 2 },
                            text = { Text(viewModel.getString("visualization")) }
                        )
                    }
                    
                    when (currentTabIndex.value) {
                        0 -> DataLoadingPanel(
                            onLoadUrl = { viewModel.loadDataFromUrl(it) },
                            isLoading = isLoading,
                            error = error,
                            onErrorDismiss = { viewModel.clearError() },
                            getString = { viewModel.getString(it) }
                        )
                        1 -> DataTableView(currentTable ?: return@Column)
                        2 -> ChartSelector(currentTable, { viewModel.getString(it) })
                    }
                }
            }
            1 -> {
                // Settings screen
                SettingsPanel(
                    currentLanguage = currentLanguage,
                    currentTheme = currentTheme,
                    onLanguageChange = { viewModel.setLanguage(it) },
                    onThemeChange = { viewModel.setTheme(it) },
                    getString = { viewModel.getString(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}
