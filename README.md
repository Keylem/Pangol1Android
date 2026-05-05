# Pangol1 Android Application

## Overview

This is the Android adaptation of Pangol1, a powerful data visualization tool that generates SVG charts from CSV data. The Android version maintains full feature parity with the desktop version while being optimized for mobile use.

## What Was Implemented

This project is a complete Android implementation of the Pangol1 charting application, adapted from the PC version found in `Pangol1PCApp/`. All files in the `Pangol1PCApp` folder remain untouched.

### Architecture & Project Structure

```
Pangol1Android/
├── app/
│   ├── src/main/java/com/example/pangol1_android/
│   │   ├── core/                    # Core business logic
│   │   │   ├── config/              # Configuration & strings
│   │   │   └── model/               # Data models
│   │   ├── geometry/                # Geometric shapes library
│   │   ├── io/                      # CSV loading & export
│   │   ├── svg/                     # SVG visualization generation
│   │   ├── ui/                      # UI layer
│   │   │   ├── composables/         # Reusable UI components
│   │   │   ├── screens/             # Full screens
│   │   │   ├── PangolViewModel.kt
│   │   │   └── PangolViewModelFactory.kt
│   │   └── MainActivity.kt
│   ├── AndroidManifest.xml          # App permissions & configuration
│   └── build.gradle.kts             # App dependencies
├── gradle/libs.versions.toml        # Centralized dependency versions
├── build.gradle.kts                 # Root gradle configuration
└── ANDROID_README.md                # Detailed Android implementation docs
```

## Core Features Implemented

### 1. **Data Loading** ✓
- Load CSV data from URLs (via OkHttp)
- Parse raw CSV text
- Handle quoted values and escaped characters
- Asynchronous loading with progress feedback

### 2. **Data Models** ✓
- `DataTable`: Container for tabular data with column/row operations
- `DataRow`: Individual row with typed access methods
- 9 geometric shape types: Point, Circle, Rectangle, Line, Ellipse, Polygon, Triangle, Text, Path, Group

### 3. **Chart Types** ✓
- **Bar Chart**: Classic bar visualization with labels and values
- **Line Chart**: Connected points with trend visualization
- **Scatter Plot**: Multi-dimensional data visualization
- **Pie Chart**: Proportion visualization with legend
- **Histogram**: Distribution analysis with configurable bins

### 4. **Geometry System** ✓
- Fully-featured geometry library matching PC version
- All shapes implement: `move()`, `scale()`, `rotate()`, `getBounds()`, `toSVGElement()`
- Support for shape groups/containers
- SVG generation from geometric objects

### 5. **SVG Generation** ✓
- `SVGGenerator` service creates complete SVG documents
- Automatic axis generation, labeling, and formatting
- Color schemes for all chart types
- Legend support for pie charts
- Properly escaped SVG output

### 6. **Internationalization** ✓
- 10 language support: English, French, Spanish, German, Italian, Portuguese, Russian, Japanese, Arabic, Chinese
- Configuration saved to DataStore
- Language-specific string resources
- Easy to extend with new languages

### 7. **Configuration Management** ✓
- `AppConfig` using AndroidX DataStore for secure storage
- Theme selection (Light/Dark)
- Language preferences
- Persistent across app sessions

### 8. **Export Functionality** ✓
- Export data tables to CSV format
- Export visualizations as SVG files
- Files organized in device storage
- Timestamp-based file naming

### 9. **User Interface** ✓
- Modern Jetpack Compose UI
- Tab-based navigation for data operations
- Bottom navigation between Data and Settings
- WebView for SVG rendering
- Responsive design for various screen sizes

## Technical Details

### Technologies Used

| Component | Library | Version |
|-----------|---------|---------|
| UI Framework | Jetpack Compose | Latest |
| HTTP Client | OkHttp | 4.11.0 |
| Data Storage | AndroidX DataStore | 1.0.0 |
| Serialization | KotlinX Serialization | 1.6.0 |
| Lifecycle | AndroidX Lifecycle | 2.6.1 |
| Build System | Gradle with Version Catalog | Latest |

### Key Classes

#### Data Layer
- `DataTable.kt`: Tabular data container with filtering
- `DataRow.kt`: Row wrapper with typed accessors
- `ExportService.kt`: CSV and SVG export

#### Geometry Layer
- `IShape.kt`: Shape interface and Bounds class
- `Point.kt`: 2D point with calculations
- `Circle.kt`, `Rectangle.kt`, `Line.kt`, `Ellipse.kt`, `Polygon.kt`, `Triangle.kt`, `Text.kt`, `Path.kt`, `Group.kt`

#### IO/SVG Layer
- `CSVLoader.kt`: URL and string CSV parsing with proper quote handling
- `SVGGenerator.kt`: Chart generation (bar, line, scatter, pie, histogram)

#### Configuration Layer
- `AppConfig.kt`: DataStore-based preferences
- `Strings.kt`: Multi-language string resources

#### UI/ViewModel
- `PangolViewModel.kt`: Central state management with StateFlow
- `PangolViewModelFactory.kt`: Factory for context injection
- `MainScreen.kt`: Main app navigation and layout
- Composables: `DataLoadingPanel`, `DataTableView`, `ChartSelector`, `SVGView`, `SettingsPanel`, `ExportPanel`

## How to Build & Run

### Prerequisites
- Android Studio (latest)
- JDK 11 or later
- Android SDK with API 24+ (Min) and API 36 (Target)

### Build Steps
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files (usually automatic)
4. Connect device or start emulator
5. Click "Run" or press Shift+F10

### APK Generation
- Build → Build Bundle(s)/APK(s) → Build APK(s)
- APK will be located at `app/build/outputs/apk/debug/`

## Permissions

The app requires the following Android permissions:
- `INTERNET`: To download CSV files from URLs
- `READ_EXTERNAL_STORAGE`: To import local CSV files
- `WRITE_EXTERNAL_STORAGE`: To export data and charts

## Usage Guide

### Loading Data
1. Open the app and go to the "Data" tab
2. Click "Load Data" sub-tab
3. Enter a CSV file URL (supports HTTP/HTTPS)
4. App downloads and parses the data
5. Confirmation appears when complete

### Viewing Data
1. Switch to "Data Table" tab
2. First 100 rows displayed in tabular format
3. All columns visible with horizontal scrolling

### Creating Visualizations
1. Click "Visualization" tab
2. Choose chart type (bar, line, scatter, pie, histogram)
3. Select X and Y columns (if applicable)
4. Chart renders in WebView
5. Use export button to save as SVG

### Exporting
1. With data loaded, click Export button
2. Choose CSV (data) or SVG (visualization)
3. Files saved to device in "Pangol1Exports" folder
4. Notification shows file location

### Settings
1. Switch to "Settings" tab
2. Choose language (10 available)
3. Select theme (Light/Dark)
4. Changes apply immediately

## Feature Parity with PC Version

| Feature | PC App | Android App | Notes |
|---------|--------|-------------|-------|
| CSV Loading | ✓ | ✓ | From URL or text |
| Data Parsing | ✓ | ✓ | Full quote/escape handling |
| Multi-language | ✓ | ✓ | 10 languages supported |
| Bar Chart | ✓ | ✓ | Full feature support |
| Line Chart | ✓ | ✓ | Full feature support |
| Scatter Plot | ✓ | ✓ | Full feature support |
| Pie Chart | ✓ | ✓ | With legend |
| Histogram | ✓ | ✓ | Configurable bins |
| SVG Export | ✓ | ✓ | Complete SVG output |
| CSV Export | ✓ | ✓ | Proper escaping |
| Themes | ✓ | Partial | Light/Dark available |
| Data Filtering | ✓ | Planned | Scheduled for v1.1 |
| Custom Styles | ✓ | Planned | Advanced features |

## Future Enhancements

The following features are planned for future releases:

- [ ] File picker for device CSV import
- [ ] Advanced data filtering UI
- [ ] More chart types (box plot, heatmap, etc.)
- [ ] PDF export
- [ ] Chart animation
- [ ] Custom color schemes
- [ ] Statistical summary display
- [ ] Undo/Redo functionality
- [ ] Cloud data source support
- [ ] Real-time data updates

## Code Quality

- **Language**: Kotlin 100%
- **Architecture**: MVVM with Jetpack Compose
- **State Management**: StateFlow for reactive updates
- **Dependency Injection**: Manual (can be extended with Hilt)
- **Testing**: Unit tests can be added for data models and SVG generation

## Performance Considerations

- CSV parsing is done asynchronously to prevent UI blocking
- Large datasets (>10K rows) show first 100 rows in table view for responsiveness
- SVG rendering uses WebView for hardware acceleration
- DataStore provides efficient preference storage

## Security

- All network requests go through OkHttp with standard certificate validation
- No sensitive data stored locally
- Permissions follow Android best practices
- App runs with minimal privilege elevation

## File Organization

The implementation is organized following Android best practices:

```
core/              - Business logic independent of Android
geometry/          - Pure Kotlin geometry library
io/                - File I/O and network operations
svg/               - SVG generation logic
ui/                - Compose UI layer
```

This separation allows for easy testing and potential code sharing with other platforms.

## Building the APK for Distribution

```bash
# Debug build (development)
./gradlew assembleDebug

# Release build (production)
./gradlew assembleRelease

# With custom signing
./gradlew assembleRelease -Pkeystore=/path/to/keystore
```

## Troubleshooting

### Common Issues

**Issue**: "Failed to load CSV: HTTP 404"
- Solution: Check URL is correct and file exists on server

**Issue**: Data not loading
- Solution: Ensure Internet permission is granted, check network connection

**Issue**: Charts not rendering
- Solution: Verify X and Y columns contain numeric data

**Issue**: Export fails
- Solution: Check storage permissions and available space

## Contributing

To add new features:

1. Follow the existing architecture (separate concerns)
2. Add unit tests for data models
3. Use Compose for UI components
4. Document public APIs with KDoc comments
5. Test on multiple screen sizes

## Documentation

- `ANDROID_README.md`: Detailed technical documentation
- Code comments: In-code documentation for complex logic
- This README: High-level overview

## License

Same as the original Pangol1 project (MIT License)

## Support

For issues or questions:
1. Check existing issues on the repository
2. Review the documentation in `ANDROID_README.md`
3. Test on the latest Android version
4. Provide clear reproduction steps for bugs

---

**Created**: 2026-05-05  
**Kotlin Version**: 2.2.10  
**Target Android API**: 36  
**Minimum Android API**: 24
