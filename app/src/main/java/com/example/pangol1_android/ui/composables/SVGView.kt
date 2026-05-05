package com.example.pangol1_android.ui.composables

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SVGView(
    svgContent: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                loadDataWithBaseURL(
                    "http://localhost",
                    svgContent,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = modifier,
        update = { webView ->
            webView.loadDataWithBaseURL(
                "http://localhost",
                svgContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}
