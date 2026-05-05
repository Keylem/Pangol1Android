package com.example.pangol1_android.core.config

/**
 * Language support for the application
 */
object Languages {
    val SUPPORTED = mapOf(
        "en" to "English",
        "fr" to "Français",
        "es" to "Español",
        "de" to "Deutsch",
        "it" to "Italiano",
        "pt" to "Português",
        "ru" to "Русский",
        "ja" to "日本語",
        "ar" to "العربية",
        "zh" to "中文"
    )
    
    fun getName(code: String): String = SUPPORTED[code] ?: code
    fun isSupported(code: String): Boolean = code in SUPPORTED
}

/**
 * Multi-language string resources
 */
object Strings {
    private val translations = mapOf(
        "en" to mapOf(
            "app_name" to "Pangol1 Android",
            "load_data" to "Load Data",
            "data_table" to "Data Table",
            "visualization" to "Visualization",
            "bar_chart" to "Bar Chart",
            "line_chart" to "Line Chart",
            "scatter_plot" to "Scatter Plot",
            "export" to "Export",
            "settings" to "Settings",
            "language" to "Language",
            "theme" to "Theme",
            "light" to "Light",
            "dark" to "Dark",
            "loading" to "Loading...",
            "error" to "Error",
            "no_data" to "No data loaded",
            "enter_url" to "Enter CSV URL or file path"
        ),
        "fr" to mapOf(
            "app_name" to "Pangol1 Android",
            "load_data" to "Charger les données",
            "data_table" to "Tableau de données",
            "visualization" to "Visualisation",
            "bar_chart" to "Diagramme en barres",
            "line_chart" to "Graphique en ligne",
            "scatter_plot" to "Diagramme de dispersion",
            "export" to "Exporter",
            "settings" to "Paramètres",
            "language" to "Langue",
            "theme" to "Thème",
            "light" to "Clair",
            "dark" to "Sombre",
            "loading" to "Chargement...",
            "error" to "Erreur",
            "no_data" to "Aucune donnée chargée",
            "enter_url" to "Entrez l'URL CSV ou le chemin du fichier"
        )
        // Add more languages as needed
    )
    
    fun get(key: String, language: String = "en"): String {
        return translations[language]?.get(key) ?: translations["en"]?.get(key) ?: key
    }
}
