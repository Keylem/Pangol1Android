package com.example.pangol1_android.core.model

/**
 * Demo data for testing and demo mode
 */
object DemoData {
    
    fun getSalesData(): DataTable {
        val headers = listOf("Product", "Q1", "Q2", "Q3", "Q4")
        val rows = listOf(
            mapOf("Product" to "Apple", "Q1" to "100", "Q2" to "120", "Q3" to "150", "Q4" to "180"),
            mapOf("Product" to "Banana", "Q1" to "80", "Q2" to "95", "Q3" to "110", "Q4" to "140"),
            mapOf("Product" to "Orange", "Q1" to "120", "Q2" to "130", "Q3" to "125", "Q4" to "160"),
            mapOf("Product" to "Mango", "Q1" to "90", "Q2" to "110", "Q3" to "135", "Q4" to "170"),
            mapOf("Product" to "Grape", "Q1" to "75", "Q2" to "85", "Q3" to "95", "Q4" to "120"),
            mapOf("Product" to "Pear", "Q1" to "60", "Q2" to "70", "Q3" to "85", "Q4" to "105")
        )
        return DataTable(
            name = "Sales Data",
            columns = headers,
            rows = rows.map { DataRow(it) },
            source = "demo"
        )
    }
    
    fun getTemperatureData(): DataTable {
        val headers = listOf("Month", "High", "Low", "Average")
        val rows = listOf(
            mapOf("Month" to "January", "High" to "5", "Low" to "-2", "Average" to "1.5"),
            mapOf("Month" to "February", "High" to "7", "Low" to "-1", "Average" to "3"),
            mapOf("Month" to "March", "High" to "12", "Low" to "2", "Average" to "7"),
            mapOf("Month" to "April", "High" to "18", "Low" to "8", "Average" to "13"),
            mapOf("Month" to "May", "High" to "24", "Low" to "14", "Average" to "19"),
            mapOf("Month" to "June", "High" to "28", "Low" to "18", "Average" to "23"),
            mapOf("Month" to "July", "High" to "30", "Low" to "20", "Average" to "25"),
            mapOf("Month" to "August", "High" to "29", "Low" to "19", "Average" to "24"),
            mapOf("Month" to "September", "High" to "25", "Low" to "15", "Average" to "20"),
            mapOf("Month" to "October", "High" to "18", "Low" to "9", "Average" to "13.5"),
            mapOf("Month" to "November", "High" to "11", "Low" to "4", "Average" to "7.5"),
            mapOf("Month" to "December", "High" to "6", "Low" to "0", "Average" to "3")
        )
        return DataTable(
            name = "Temperature Data",
            columns = headers,
            rows = rows.map { DataRow(it) },
            source = "demo"
        )
    }
    
    fun getPopulationData(): DataTable {
        val headers = listOf("Country", "Population", "Growth")
        val rows = listOf(
            mapOf("Country" to "China", "Population" to "1400", "Growth" to "0.4"),
            mapOf("Country" to "India", "Population" to "1380", "Growth" to "0.8"),
            mapOf("Country" to "USA", "Population" to "330", "Growth" to "0.5"),
            mapOf("Country" to "Indonesia", "Population" to "270", "Growth" to "0.9"),
            mapOf("Country" to "Pakistan", "Population" to "230", "Growth" to "1.9"),
            mapOf("Country" to "Brazil", "Population" to "215", "Growth" to "0.7"),
            mapOf("Country" to "Nigeria", "Population" to "220", "Growth" to "2.4"),
            mapOf("Country" to "Bangladesh", "Population" to "170", "Growth" to "0.8")
        )
        return DataTable(
            name = "Population Data",
            columns = headers,
            rows = rows.map { DataRow(it) },
            source = "demo"
        )
    }
    
    fun getStudentGradesData(): DataTable {
        val headers = listOf("Subject", "Math", "Science", "English", "History")
        val rows = listOf(
            mapOf("Subject" to "Grade A", "Math" to "15", "Science" to "12", "English" to "18", "History" to "16"),
            mapOf("Subject" to "Grade B", "Math" to "28", "Science" to "32", "English" to "25", "History" to "29"),
            mapOf("Subject" to "Grade C", "Math" to "35", "Science" to "38", "English" to "32", "History" to "34"),
            mapOf("Subject" to "Grade D", "Math" to "18", "Science" to "14", "English" to "20", "History" to "17"),
            mapOf("Subject" to "Grade F", "Math" to "4", "Science" to "4", "English" to "5", "History" to "4")
        )
        return DataTable(
            name = "Student Grades",
            columns = headers,
            rows = rows.map { DataRow(it) },
            source = "demo"
        )
    }
    
    fun getWebsiteTrafficData(): DataTable {
        val headers = listOf("Day", "Desktop", "Mobile", "Tablet")
        val rows = listOf(
            mapOf("Day" to "Monday", "Desktop" to "1500", "Mobile" to "2800", "Tablet" to "800"),
            mapOf("Day" to "Tuesday", "Desktop" to "1600", "Mobile" to "2900", "Tablet" to "850"),
            mapOf("Day" to "Wednesday", "Desktop" to "1400", "Mobile" to "2700", "Tablet" to "750"),
            mapOf("Day" to "Thursday", "Desktop" to "1800", "Mobile" to "3100", "Tablet" to "900"),
            mapOf("Day" to "Friday", "Desktop" to "2000", "Mobile" to "3400", "Tablet" to "1000"),
            mapOf("Day" to "Saturday", "Desktop" to "1200", "Mobile" to "3000", "Tablet" to "950"),
            mapOf("Day" to "Sunday", "Desktop" to "900", "Mobile" to "2500", "Tablet" to "700")
        )
        return DataTable(
            name = "Website Traffic",
            columns = headers,
            rows = rows.map { DataRow(it) },
            source = "demo"
        )
    }
}
