package com.pilltracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pilltracker.ui.PillTrackerRoot
import com.pilltracker.ui.theme.PillTrackerTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PillTrackerTheme {
                PillTrackerRoot()
            }
        }

        askNotificationPermissionIfNeeded()
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < 33) return

        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
