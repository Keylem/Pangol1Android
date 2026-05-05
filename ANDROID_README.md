# Pangol1 Android - Feature Parity Implementation

This is the Android version of Pangol1, a data visualization application that generates SVG charts from CSV data.

## Architecture Overview

The Android implementation maintains feature parity with the PC application while adapting to Android's component-based architecture.

### Project Structure

```
app/src/main/java/com/example/pangol1_android/
├── core/
│   ├── config/          # Configuration (AppConfig, Languages, Strings)
│   └── model/           # Data models (DataTable, DataRow)
├── geometry/            # Geometric shapes (Point, Circle, Rectangle, Line, Polygon, etc.)
├── io/                  # Input/Output (CSVLoader, ExportService)
├── svg/                 # SVG generation (SVGGenerator)
├── ui/
│   ├── composables/     # Reusable UI components
│   ├── screens/         # Full screen compositions
│   ├── PangolViewModel.kt
│   └── PangolViewModelFactory.kt
└── MainActivity.kt
```

## Features

### 1. Data Loading ✓
- Load CSV data from URLs
- Load CSV data from raw text
- Parse CSV with quoted values and escape sequences

### 2. Data Models ✓
- DataTable: Main container for tabular data
- DataRow: Individual row with key-value mapping
- Geometry: Point, Circle, Rectangle, Line, Ellipse, Polygon, Triangle, Text, Path, Group

### 3. Visualization Types ✓
- Bar Chart
- Line Chart
- Scatter Plot
- Pie Chart
- Histogram

### 4. Configuration ✓
- Multi-language support (10 languages)
- Theme selection (Light/Dark)
- Settings stored in DataStore
- Language options: English, French, Spanish, German, Italian, Portuguese, Russian, Japanese, Arabic, Chinese

### 5. Export Functionality ✓
- Export data to CSV
- Export visualizations to SVG
- Files saved to device storage in Pangol1Exports folder

### 6. UI Components ✓
- DataLoadingPanel: Load CSV from URL
- DataTableView: Display tabular data (first 100 rows)
- ChartSelector: Choose visualization type
- SVGView: Display SVG in WebView
- SettingsPanel: Language and theme settings
- ExportPanel: Export data and charts

## Permissions

The app requires:
- `INTERNET`: For loading CSV from URLs
- `READ_EXTERNAL_STORAGE`: For importing files
- `WRITE_EXTERNAL_STORAGE`: For exporting files

## Dependencies

Key libraries used:
- **Jetpack Compose**: Modern UI toolkit
- **OkHttp**: HTTP client for downloading CSV files
- **DataStore**: Secure configuration storage
- **KotlinX Serialization**: JSON serialization
- **AndroidX Lifecycle**: ViewModel and state management

## Key Classes

### Core Modules

#### DataTable & DataRow
- Represent tabular CSV data
- Support filtering and column operations
- Type-safe access with `getAsInt()`, `getAsDouble()`

#### Geometry
- `IShape`: Base interface for all shapes
- `Point`: 2D point with distance calculation
- Basic shapes: Circle, Rectangle, Line, Ellipse, Polygon, Triangle
- `Group`: Container for multiple shapes
- All shapes can: move, scale, rotate, get bounds, generate SVG

#### SVGGenerator
- `generateBarChart()`: Create bar charts
- `generateLineChart()`: Create line charts
- `generateScatterPlot()`: Create scatter plots
- `generatePieChart()`: Create pie charts with legend
- `generateHistogram()`: Create histograms with bins

#### CSVLoader
- `loadFromUrl()`: Download and parse CSV from URL (async)
- `loadFromString()`: Parse CSV from text
- Handles quoted values and escaped quotes

#### ExportService
- `exportToCSV()`: Save data table as CSV
- `exportToSVG()`: Save visualization as SVG
- Organized in Pangol1Exports folder

### UI/State Management

#### PangolViewModel
- Central state management using StateFlow
- Manages: currentTable, isLoading, error, language, theme
- Handles data loading and language changes
- Provides localized strings

#### Composables
All UI components follow Compose best practices with:
- Proper modifier usage
- State management
- Reusability
- Clear separation of concerns

## Usage

1. **Load Data**: Navigate to "Data" tab → "Load Data" → Enter CSV URL
2. **View Data**: Click "Data Table" tab to see raw data
3. **Visualize**: Click "Visualization" tab → Choose chart type
4. **Export**: Use export buttons to save data or charts
5. **Settings**: Navigate to "Settings" to change language/theme

## Feature Parity with PC Version

| Feature | PC Version | Android Version |
|---------|-----------|-----------------|
| Load CSV from URL | ✓ | ✓ |
| Parse CSV files | ✓ | ✓ |
| Data filtering | ✓ | Planned |
| Multi-language | ✓ | ✓ |
| Bar Chart | ✓ | ✓ |
| Line Chart | ✓ | ✓ |
| Scatter Plot | ✓ | ✓ |
| Pie Chart | ✓ | ✓ |
| Histogram | ✓ | ✓ |
| Export to CSV | ✓ | ✓ |
| Export to SVG | ✓ | ✓ |
| Custom themes | ✓ | Planned |
| Advanced filters | ✓ | Planned |

## Future Enhancements

- [ ] File picker for local CSV import
- [ ] Data filtering UI
- [ ] More chart types
- [ ] PDF export
- [ ] Chart animation
- [ ] Custom color schemes
- [ ] Statistical analysis features
- [ ] Data transformation operations
- [ ] Batch operations
- [ ] Cloud sync

## Building and Running

1. Open in Android Studio
2. Sync Gradle files
3. Run on emulator or device (Min SDK 24, Target SDK 36)

## Notes

- Shapes implement rotation/scale operations but simplified implementations - can be extended with matrix math
- SVG rendering uses WebView for compatibility
- CSV parsing handles quoted values and escape sequences
- Data table viewer shows first 100 rows; can be extended for pagination
