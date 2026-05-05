package com.example.pangol1_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pangol1_android.core.config.AppConfig
import com.example.pangol1_android.core.config.Strings
import com.example.pangol1_android.core.model.DataTable
import com.example.pangol1_android.io.CSVLoader
import com.example.pangol1_android.io.ExportService
import com.example.pangol1_android.svg.SVGGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing application state
 */
class PangolViewModel(private val context: Context) : ViewModel() {
    
    private val appConfig = AppConfig(context)
    
    private val _currentTable = MutableStateFlow<DataTable?>(null)
    val currentTable = _currentTable.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage = _currentLanguage.asStateFlow()
    
    private val _currentTheme = MutableStateFlow("light")
    val currentTheme = _currentTheme.asStateFlow()
    
    init {
        viewModelScope.launch {
            appConfig.getLanguageFlow().collect { lang ->
                _currentLanguage.value = lang
            }
        }
        
        viewModelScope.launch {
            appConfig.getThemeFlow().collect { theme ->
                _currentTheme.value = theme
            }
        }
    }
    
    fun loadDataFromUrl(url: String) {
        if (url.isBlank()) {
            _error.value = getString("error")
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = CSVLoader.loadFromUrl(url)
                result.onSuccess { table ->
                    _currentTable.value = table
                }.onFailure { throwable ->
                    _error.value = throwable.message ?: getString("error")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: getString("error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadDataFromString(csvContent: String, name: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = CSVLoader.loadFromString(csvContent, name)
                result.onSuccess { table ->
                    _currentTable.value = table
                }.onFailure { throwable ->
                    _error.value = throwable.message ?: getString("error")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: getString("error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setLanguage(language: String) {
        viewModelScope.launch {
            appConfig.setLanguage(language)
            _currentLanguage.value = language
        }
    }
    
    fun setTheme(theme: String) {
        viewModelScope.launch {
            appConfig.setTheme(theme)
            _currentTheme.value = theme
        }
    }
    
    fun clearTable() {
        _currentTable.value = null
        _error.value = null
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun exportDataToCSV(context: Context) {
        val table = _currentTable.value ?: return
        viewModelScope.launch {
            val result = ExportService.exportToCSV(context, table)
            result.onSuccess {
                _error.value = "Data exported to ${it.absolutePath}"
            }.onFailure {
                _error.value = "Export failed: ${it.message}"
            }
        }
    }
    
    fun exportVisualizationToSVG(context: Context, svgContent: String) {
        viewModelScope.launch {
            val result = ExportService.exportToSVG(context, svgContent)
            result.onSuccess {
                _error.value = "Chart exported to ${it.absolutePath}"
            }.onFailure {
                _error.value = "Export failed: ${it.message}"
            }
        }
    }
    
    fun getString(key: String): String {
        return Strings.get(key, _currentLanguage.value)
    }
}
