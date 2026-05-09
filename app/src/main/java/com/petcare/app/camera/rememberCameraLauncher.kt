package com.petcare.app.camera

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import java.io.File


@Composable
fun rememberCameraLauncher(
    context: Context,
    onSuccess: (Uri) -> Unit
): () -> Unit {
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) pendingUri?.let { onSuccess(it) }
    }

    return remember {
        {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "pet_photo_${System.currentTimeMillis()}.jpg"
            )
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            pendingUri = uri
            cameraLauncher.launch(uri)
        }
    }
}