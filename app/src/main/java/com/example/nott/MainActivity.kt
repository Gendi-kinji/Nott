package com.example.nott

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    companion object {
        const val CHANNEL_ID = "Nott"
        const val CHANNEL_NAME = "Notifications"
        const val CHANNEL_DESCRIPTION = "Channel for periodic tasks"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permission for notifications (if necessary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        // Create notification channel
        createNotificationChannel()

        // Set the content view with the UI
        setContent {
            ScheduleNotificationScreen()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val manager = getSystemService(NotificationManager::class.java)
            if (manager?.getNotificationChannel(CHANNEL_ID) == null) {
                manager?.createNotificationChannel(channel)
                Log.d("MainActivity", "Notification channel created: $CHANNEL_NAME")
            } else {
                Log.d("MainActivity", "Notification channel already exists: $CHANNEL_NAME")
            }
        }
    }

    @Composable
    fun ScheduleNotificationScreen() {
        Button(onClick = { scheduleCronJob() }) {
            Text("Start Notification Schedule")
        }
    }

    private fun scheduleCronJob() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CronJobWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        Log.d("MainActivity", "scheduleCronJob: Work request enqueued")
    }

    class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

        private val motivationQuotes = listOf(
            "Start small",
            "Follow your heart",
            "Be yourself",
            "Do your best",
            "Accept failure and move on",
            "Love yourself",
            "Don't live another's life",
            "Let it all out",
            "Don't be perfect",
            "Be consistent",
            "Practise. Practise. Practise",
            "Be self-sufficient",
            "Think outside the box",
            "Break work down to make it simple"
        )

        override fun doWork(): Result {
            val randomQuote = motivationQuotes.random()
            showNotification("Affirmation of the Day", randomQuote)
            return Result.success()
        }

        private fun showNotification(title: String, message: String) {
            try {
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification) // Ensure this drawable exists
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

                NotificationManagerCompat.from(applicationContext).notify(generateNotificationId(), notification)
                Log.d("NotificationWorker", "Notification sent: $message")
            } catch (e: Exception) {
                Log.e("NotificationWorker", "Failed to send notification", e)
            }
        }

        private fun generateNotificationId(): Int {
            return Random.nextInt(1000, 9999) // Restrict to a reasonable range
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleCronJobScreenPreview() {
    MainActivity().ScheduleNotificationScreen()
}
