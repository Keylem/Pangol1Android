package com.example.pangol1_android.io

import com.example.pangol1_android.core.model.DataRow
import com.example.pangol1_android.core.model.DataTable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Service for loading CSV data from files and URLs
 */
object CSVLoader {
    
    private val httpClient = OkHttpClient()
    
    /**
     * Load CSV data from a URL
     */
    suspend fun loadFromUrl(url: String, name: String = ""): Result<DataTable> = runCatching {
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()
        
        if (!response.isSuccessful) {
            throw Exception("Failed to load CSV: HTTP ${response.code}")
        }
        
        val content = response.body?.string() ?: throw Exception("Empty response")
        parseCSV(content, name.ifEmpty { url })
    }
    
    /**
     * Load CSV data from raw text
     */
    fun loadFromString(csvContent: String, name: String = ""): Result<DataTable> = runCatching {
        parseCSV(csvContent, name)
    }
    
    /**
     * Parse CSV content
     */
    private fun parseCSV(content: String, source: String): DataTable {
        val lines = content.trim().split("\n")
        if (lines.isEmpty()) {
            return DataTable(source = source)
        }
        
        // Parse header
        val columns = parseCSVLine(lines[0])
        if (columns.isEmpty()) {
            return DataTable(source = source)
        }
        
        // Parse rows
        val rows = lines.drop(1)
            .filter { it.isNotBlank() }
            .map { line ->
                val values = parseCSVLine(line)
                val rowMap = columns.mapIndexed { index, column ->
                    column to (values.getOrNull(index) ?: "")
                }.toMap()
                DataRow(rowMap)
            }
        
        return DataTable(
            name = source.substringAfterLast("/"),
            columns = columns,
            rows = rows,
            source = source
        )
    }
    
    /**
     * Parse a single CSV line, handling quoted values
     */
    private fun parseCSVLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val ch = line[i]
            when {
                ch == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        // Escaped quote
                        current.append('"')
                        i++
                    } else {
                        // Toggle quote state
                        inQuotes = !inQuotes
                    }
                }
                ch == ',' && !inQuotes -> {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                }
                else -> current.append(ch)
            }
            i++
        }
        
        result.add(current.toString().trim())
        return result
    }
}
