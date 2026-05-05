package com.example.pangol1_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.ViewModelProvider
import com.example.pangol1_android.ui.PangolViewModel
import com.example.pangol1_android.ui.PangolViewModelFactory
import com.example.pangol1_android.ui.screens.MainScreen
import com.example.pangol1_android.ui.theme.Pangol1AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val factory = PangolViewModelFactory(this)
        val viewModel = ViewModelProvider(this, factory).get(PangolViewModel::class.java)
        
        setContent {
            Pangol1AndroidTheme {
                MainScreen(
                    viewModel = viewModel,
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                )
            }
        }
    }
}
