package com.pilltracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun PillTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography(),
        content = content
    )
}
