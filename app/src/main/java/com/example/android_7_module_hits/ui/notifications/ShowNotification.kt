package com.example.android_7_module_hits.ui.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun showNotification(
    scope: CoroutineScope,
    setNotification: (UiNotification?) -> Unit,
    notification: UiNotification,
    durationMillis: Long = 2000L
) {
    setNotification(notification)
    scope.launch {
        delay(durationMillis)
        setNotification(null)
    }
}
