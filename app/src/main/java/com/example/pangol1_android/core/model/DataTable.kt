package com.example.pangol1_android.core.model

/**
 * Represents a table of data loaded from CSV or other sources
 */
data class DataTable(
    val name: String = "",
    val columns: List<String> = emptyList(),
    val rows: List<DataRow> = emptyList(),
    val source: String = "" // URL or file path
) {
    fun getColumn(columnName: String): List<String> {
        return rows.mapNotNull { it.get(columnName) }
    }
    
    fun getColumnAsDoubles(columnName: String): List<Double> {
        return rows.mapNotNull { it.getAsDouble(columnName) }
    }
    
    fun filterRows(predicate: (DataRow) -> Boolean): DataTable {
        return this.copy(rows = rows.filter(predicate))
    }
    
    fun isEmpty(): Boolean = rows.isEmpty()
    
    fun size(): Int = rows.size
}
