package com.example.pangol1_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for creating PangolViewModel with context
 */
class PangolViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PangolViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PangolViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
