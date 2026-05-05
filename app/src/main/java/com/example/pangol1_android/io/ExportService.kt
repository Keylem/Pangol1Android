package com.example.pangol1_android.io

import android.content.Context
import android.os.Environment
import com.example.pangol1_android.core.model.DataTable
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Service for exporting data and visualizations
 */
object ExportService {
    
    /**
     * Export data table to CSV file
     */
    fun exportToCSV(context: Context, dataTable: DataTable, fileName: String = ""): Result<File> = runCatching {
        val file = createExportFile(context, fileName.ifEmpty { "export_${getCurrentTimestamp()}.csv" })
        
        FileWriter(file).use { writer ->
            // Write header
            writer.write(dataTable.columns.joinToString(","))
            writer.write("\n")
            
            // Write rows
            dataTable.rows.forEach { row ->
                val values = dataTable.columns.map { col ->
                    val value = row.get(col) ?: ""
                    // Escape quotes and wrap in quotes if contains comma
                    if (value.contains(",") || value.contains("\"")) {
                        "\"${value.replace("\"", "\"\"")}\""
                    } else {
                        value
                    }
                }
                writer.write(values.joinToString(","))
                writer.write("\n")
            }
        }
        
        file
    }
    
    /**
     * Export SVG visualization to file
     */
    fun exportToSVG(context: Context, svgContent: String, fileName: String = ""): Result<File> = runCatching {
        val file = createExportFile(context, fileName.ifEmpty { "chart_${getCurrentTimestamp()}.svg" })
        
        FileWriter(file).use { writer ->
            writer.write(svgContent)
        }
        
        file
    }
    
    /**
     * Create export file with proper path
     */
    private fun createExportFile(context: Context, fileName: String): File {
        val exportDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Pangol1Exports")
        
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        
        return File(exportDir, fileName)
    }
    
    /**
     * Get timestamp for file naming
     */
    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    }
    
    /**
     * Get all exported files
     */
    fun getExportedFiles(context: Context): List<File> {
        val exportDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Pangol1Exports")
        return exportDir.listFiles()?.toList() ?: emptyList()
    }
    
    /**
     * Delete exported file
     */
    fun deleteExportedFile(file: File): Boolean {
        return file.delete()
    }
}
