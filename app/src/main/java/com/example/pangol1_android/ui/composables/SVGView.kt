package com.example.pangol1_android.ui.composables

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SVGView(
    svgContent: String,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean? = null
) {
    // Use system dark mode if not explicitly provided
    val effectiveIsDarkMode = isDarkMode ?: isSystemInDarkTheme()
    
    var contentLoaded by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(if (contentLoaded) 1f else 0.3f, label = "svgAlpha")
    
    // Simplified HTML that works better with WebView
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }
                html, body {
                    width: 100%;
                    height: 100%;
                    background: ${if (effectiveIsDarkMode) "#121212" else "#FFFFFF"};
                }
                body {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 100vh;
                    padding: 8px;
                }
                svg {
                    width: 100%;
                    height: 100%;
                    max-width: 100%;
                    max-height: 100%;
                }
            </style>
        </head>
        <body>
            $svgContent
        </body>
        </html>
    """.trimIndent()
    
    Box(modifier = modifier.fillMaxWidth().alpha(alphaAnim)) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        javaScriptEnabled = false
                        domStorageEnabled = false
                        databaseEnabled = false
                        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            contentLoaded = true
                        }
                    }
                    setBackgroundColor(0)
                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
