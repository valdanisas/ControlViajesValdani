package com.ctrlmaterial.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.parseColor("#f59e0b")
        window.navigationBarColor = Color.parseColor("#080f1a")

        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview)

        // WebView settings
        with(webView.settings) {
            javaScriptEnabled          = true
            domStorageEnabled          = true
            allowFileAccess            = true
            allowContentAccess         = true
            cacheMode                  = WebSettings.LOAD_DEFAULT
            setSupportZoom(false)
            displayZoomControls        = false
            builtInZoomControls        = false
            loadWithOverviewMode       = true
            useWideViewPort            = true
            databaseEnabled            = true
            mixedContentMode           = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // Native bridge
        webView.addJavascriptInterface(AndroidBridge(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                // Silently handle errors for offline resources
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(msg: ConsoleMessage): Boolean {
                return true // suppress console logs
            }
        }

        // Load the embedded web app
        webView.loadUrl("file:///android_asset/www/index.html")
    }

    // Handle back button inside WebView
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    // ══════════════════════════════════════════════
    //  JavaScript ↔ Android Bridge
    // ══════════════════════════════════════════════
    inner class AndroidBridge {

        /**
         * Called from JS to share an Excel file via native Android share sheet.
         * JS passes: filename (String) + base64-encoded file bytes (String)
         */
        @JavascriptInterface
        fun shareFile(filename: String, base64Data: String) {
            try {
                // Decode base64 → bytes
                val bytes = Base64.decode(base64Data, Base64.DEFAULT)

                // Write to app cache
                val exportDir = File(cacheDir, "exports").also { it.mkdirs() }
                val file = File(exportDir, filename).also { it.writeBytes(bytes) }

                // FileProvider URI (needed for Android 7+)
                val uri = FileProvider.getUriForFile(
                    this@MainActivity,
                    "${packageName}.provider",
                    file
                )

                // Build share intent
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, filename)
                    putExtra(Intent.EXTRA_TEXT, "Registros Control de Material")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                runOnUiThread {
                    startActivity(
                        Intent.createChooser(shareIntent, "Compartir vía…")
                    )
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al exportar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        /** Show a native Android Toast from JS */
        @JavascriptInterface
        fun toast(message: String) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

        /** Check if running inside the native app */
        @JavascriptInterface
        fun isNativeApp(): Boolean = true
    }
}
