package com.example.nott

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import kotlin.random.Random

class NotificationClass(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val CHANNEL_ID = "Nott"
    }

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
            Log.d("NotificationClass", "Notification sent: $message")
        } catch (e: Exception) {
            Log.e("NotificationClass", "Failed to send notification", e)
        }
    }

    private fun generateNotificationId(): Int {
        return Random.nextInt(1000, 9999) // Restrict to a reasonable range
    }
}
