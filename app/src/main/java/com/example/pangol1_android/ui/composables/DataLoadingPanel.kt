package com.example.pangol1_android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
fun DataLoadingPanel(
    onLoadUrl: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onErrorDismiss: () -> Unit,
    getString: (String) -> String
) {
    var urlInput by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = getString("load_data"),
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        
        TextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text(getString("enter_url")) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onLoadUrl(urlInput) },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                enabled = !isLoading && urlInput.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.White
                    )
                }
                Text(if (isLoading) getString("loading") else getString("load_data"))
            }
        }
        
        if (error != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFCDD2))
                    .padding(8.dp)
            ) {
                Text(
                    text = error,
                    color = Color(0xFFB71C1C),
                    fontSize = 12.sp
                )
                Button(
                    onClick = onErrorDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                ) {
                    Text("Dismiss")
                }
            }
        }
    }
}
