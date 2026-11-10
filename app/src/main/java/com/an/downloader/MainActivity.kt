package com.an.downloader

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DownloaderApp() }
    }
}

@Composable
fun DownloaderApp() {
    val context = LocalContext.current
    var url by remember { mutableStateOf("") }
    var dark by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("Ready to download") }

    MaterialTheme(colorScheme = if (dark) darkColorScheme() else lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("AN Downloader", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Switch(checked = dark, onCheckedChange = { dark = it })
                }
                Text("Download anything. Simply.", color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(25.dp))
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("Paste download link") }, placeholder = { Text("https://example.com/file.zip") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri), singleLine = true)
                Button(onClick = {
                    try {
                        val uri = Uri.parse(url.trim())
                        if ((uri.scheme != "https" && uri.scheme != "http") || uri.host.isNullOrBlank()) {
                            message = "Please enter a valid URL"
                        } else {
                            val request = DownloadManager.Request(uri)
                                .setTitle(uri.lastPathSegment ?: "AN Download")
                                .setDescription("Downloading with AN Downloader")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment ?: "download_file")
                            val manager = context.getSystemService(DownloadManager::class.java)
                            manager.enqueue(request)
                            message = "Download started!"
                        }
                    } catch (e: Exception) { message = "Download failed: check the link" }
                }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(16.dp)) {
                    Text("⬇  DOWNLOAD")
                }
                Text(message, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider()
                Text("AN Downloader • No Ads • Version 1.0", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
