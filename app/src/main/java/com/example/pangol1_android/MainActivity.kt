package com.example.pangol1_android

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.pangol1_android.ui.PangolViewModel
import com.example.pangol1_android.ui.PangolViewModelFactory
import com.example.pangol1_android.ui.screens.MainScreen
import com.example.pangol1_android.ui.theme.Pangol1AndroidTheme
import java.io.BufferedReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val factory = PangolViewModelFactory(this)
        val viewModel = ViewModelProvider(this, factory).get(PangolViewModel::class.java)
        
        setContent {
            val currentTheme by viewModel.currentTheme.collectAsState()
            
            // File picker launcher
            val filePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                if (uri != null) {
                    try {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            val csvContent = BufferedReader(inputStream.bufferedReader()).use { reader ->
                                reader.readText()
                            }
                            val fileName = uri.path?.substringAfterLast("/") ?: "imported_data"
                            viewModel.loadDataFromString(csvContent, fileName)
                        }
                    } catch (e: Exception) {
                        viewModel.setError("Failed to read file: ${e.message}")
                    }
                }
            }
            
            // Determine effective dark theme (auto = system preference)
            val isDarkTheme = if (currentTheme == "auto") {
                isSystemInDarkTheme()
            } else {
                currentTheme == "dark"
            }
            
            Pangol1AndroidTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    viewModel = viewModel,
                    onPickFile = { filePickerLauncher.launch("text/plain") },
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                )
            }
        }
    }
}
