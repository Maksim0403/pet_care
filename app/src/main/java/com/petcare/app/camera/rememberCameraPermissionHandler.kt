package com.petcare.app.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker


@Composable
fun rememberCameraPermissionHandler(
    context: Context,
    onGranted: () -> Unit
): () -> Unit {
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var permissionDeniedOnce by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onGranted()
        else {
            permissionDeniedOnce = true; showRationaleDialog = true
        }
    }

    CameraPermissionDialogs(
        showRationale = showRationaleDialog,
        showSettings = showSettingsDialog,
        onRationaleConfirm = {
            showRationaleDialog = false
            permissionLauncher.launch(Manifest.permission.CAMERA)
        },
        onRationaleDismiss = { showRationaleDialog = false },
        onSettingsConfirm = {
            showSettingsDialog = false
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            })
        },
        onSettingsDismiss = { showSettingsDialog = false }
    )

    return remember(permissionDeniedOnce) {
        {
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PermissionChecker.PERMISSION_GRANTED

            when {
                granted -> onGranted()
                permissionDeniedOnce -> showSettingsDialog = true
                else -> permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}