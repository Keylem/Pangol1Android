package com.example.pangol1_android.core.model

/**
 * Represents a single row in a data table
 */
data class DataRow(
    val values: Map<String, String> = emptyMap()
) {
    fun get(columnName: String): String? = values[columnName]
    
    fun getAsInt(columnName: String): Int? = values[columnName]?.toIntOrNull()
    
    fun getAsDouble(columnName: String): Double? = values[columnName]?.toDoubleOrNull()
}
