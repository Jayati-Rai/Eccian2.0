package com.example.project20.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project20.PutPDF
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfResourcesScreen(path: String) {
    val context = LocalContext.current
    var pdfList by remember(path) { mutableStateOf<List<PutPDF>>(emptyList()) }
    var isLoading by remember(path) { mutableStateOf(true) }
    var uploadProgress by remember { mutableStateOf<Float?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(path) {
        loadPdfFiles(path) { files, error ->
            pdfList = files
            errorMessage = error
            isLoading = false
        }
    }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val filename = context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val nameColumn = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameColumn >= 0) cursor.getString(nameColumn) else null
            } ?: "uploaded_file.pdf"

            uploadProgress = 0f
            errorMessage = null
            val fileReference = FirebaseStorage.getInstance().reference
                .child(path)
                .child(filename)
            fileReference.putFile(uri)
                .addOnProgressListener { snapshot ->
                    uploadProgress = if (snapshot.totalByteCount > 0) {
                        snapshot.bytesTransferred.toFloat() / snapshot.totalByteCount
                    } else {
                        0f
                    }
                }
                .addOnSuccessListener { snapshot ->
                    snapshot.storage.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            val databaseReference = FirebaseDatabase.getInstance()
                                .getReference(path)
                            val pdf = PutPDF(name = filename, url = downloadUri.toString())
                            val key = databaseReference.push().key ?: UUID.randomUUID().toString()
                            databaseReference.child(key).setValue(pdf)
                                .addOnSuccessListener {
                                    uploadProgress = null
                                    Toast.makeText(context, "File Uploaded", Toast.LENGTH_LONG).show()
                                    loadPdfFiles(path) { files, error ->
                                        pdfList = files
                                        errorMessage = error
                                        isLoading = false
                                    }
                                }
                                .addOnFailureListener { error ->
                                    uploadProgress = null
                                    errorMessage = "Could not save PDF details: ${error.message}"
                                }
                        }
                        .addOnFailureListener { error ->
                            uploadProgress = null
                            errorMessage = "Could not get the uploaded PDF URL: ${error.message}"
                        }
                }
                .addOnFailureListener { error ->
                    uploadProgress = null
                    errorMessage = "Upload failed: ${error.message}"
                }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Path: $path") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { pdfPickerLauncher.launch("application/pdf") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add PDF")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                uploadProgress?.let { progress ->
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    pdfList.isEmpty() -> {
                        Text(
                            text = "No PDFs found.",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 24.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(pdfList) { pdf ->
                                PdfItemCard(pdf = pdf) {
                                    val url = pdf.url
                                    if (!url.isNullOrEmpty()) {
                                        downloadFile(context, url, pdf.name ?: "download.pdf")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PdfItemCard(pdf: PutPDF, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = pdf.name ?: "Untitled PDF",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = pdf.url ?: "No URL available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

private fun loadPdfFiles(path: String, onLoaded: (List<PutPDF>, String?) -> Unit) {
    FirebaseStorage.getInstance().reference.child(path).listAll()
        .addOnSuccessListener { result ->
            if (result.items.isEmpty()) {
                onLoaded(emptyList(), null)
                return@addOnSuccessListener
            }

            val pdfs = arrayOfNulls<PutPDF>(result.items.size)
            var remaining = result.items.size
            var failureMessage: String? = null
            result.items.forEachIndexed { index, item ->
                item.downloadUrl
                    .addOnSuccessListener { uri ->
                        pdfs[index] = PutPDF(name = item.name, url = uri.toString())
                        remaining--
                        if (remaining == 0) {
                            onLoaded(pdfs.filterNotNull(), failureMessage)
                        }
                    }
                    .addOnFailureListener { error ->
                        failureMessage = error.message ?: "Could not load a PDF URL."
                        remaining--
                        if (remaining == 0) {
                            onLoaded(pdfs.filterNotNull(), failureMessage)
                        }
                    }
            }
        }
        .addOnFailureListener { error ->
            onLoaded(emptyList(), "Could not load PDFs: ${error.message}")
        }
}

private fun downloadFile(context: Context, url: String, fileName: String) {
    Log.d("downloadfile", "Downloading $url")
    val localFile = File(context.getExternalFilesDir(null), fileName)

    FirebaseStorage.getInstance().getReferenceFromUrl(url)
        .getFile(localFile)
        .addOnSuccessListener {
            Toast.makeText(
                context,
                "PDF downloaded: ${localFile.absolutePath}",
                Toast.LENGTH_LONG
            ).show()
        }
        .addOnFailureListener { error ->
            Log.e("downloadfile", "PDF download failed", error)
            Toast.makeText(context, "Failed to download PDF", Toast.LENGTH_SHORT).show()
        }
}

@Preview
@Composable
fun PdfResourcesScreenPreview() {
    PdfResourcesScreen(path = "path")
}