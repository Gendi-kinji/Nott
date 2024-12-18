package com.example.nott

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        setContent {
            ScheduleNotificationScreen()
        }
    }
}

@Composable
fun ScheduleNotificationScreen() {
    Button(onClick = { scheduleCronJob() }) {
        Text("Start Notification Schedule")
    }
}

fun scheduleCronJob() {
    val workRequest = PeriodicWorkRequestBuilder<NotificationClass>(15, TimeUnit.MINUTES).build()
    WorkManager.getInstance().enqueueUniquePeriodicWork(
        "CronJobWork",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )

    Log.d("MainActivity", "scheduleCronJob: Work request enqueued")
}

@Preview(showBackground = true)
@Composable
fun ScheduleCronJobScreenPreview() {
    ScheduleNotificationScreen()
}

