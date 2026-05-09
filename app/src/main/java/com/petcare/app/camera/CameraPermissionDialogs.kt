package com.petcare.app.camera

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CameraPermissionDialogs(
    showRationale: Boolean,
    showSettings: Boolean,
    onRationaleConfirm: () -> Unit,
    onRationaleDismiss: () -> Unit,
    onSettingsConfirm: () -> Unit,
    onSettingsDismiss: () -> Unit
) {
    if (showRationale) {
        AlertDialog(
            onDismissRequest = onRationaleDismiss,
            title = { Text("Доступ до камери") },
            text = {
                Text(
                    "Щоб додати фото улюбленця, потрібен доступ до камери. " +
                            "Будь ласка, надайте дозвіл."
                )
            },
            confirmButton = {
                TextButton(onClick = onRationaleConfirm) {
                    Text("Надати дозвіл")
                }
            },
            dismissButton = {
                TextButton(onClick = onRationaleDismiss) {
                    Text("Скасувати")
                }
            }
        )
    }

    if (showSettings) {
        AlertDialog(
            onDismissRequest = onSettingsDismiss,
            title = { Text("Доступ до камери заблоковано") },
            text = {
                Text(
                    "Ви заборонили доступ до камери. Відкрийте налаштування " +
                            "застосунку та увімкніть дозвіл вручну."
                )
            },
            confirmButton = {
                TextButton(onClick = onSettingsConfirm) {
                    Text("Відкрити налаштування")
                }
            },
            dismissButton = {
                TextButton(onClick = onSettingsDismiss) {
                    Text("Скасувати")
                }
            }
        )
    }
}