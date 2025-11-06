package com.novahypnose.ancrage.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.novahypnose.ancrage.MainActivity
import com.novahypnose.ancrage.R

/**
 * Helper pour gérer les notifications de rappel
 */
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "anchor_reminders"
        const val CHANNEL_NAME = "Rappels d'ancrage"
        const val NOTIFICATION_ID_BASE = 1000
    }

    init {
        createNotificationChannel()
    }

    /**
     * Crée le canal de notification (Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                importance
            ).apply {
                description = context.getString(R.string.notification_channel_description)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 100, 200)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Affiche une notification de rappel pour un ancrage
     * @param anchorId ID de l'ancrage
     * @param emotionName Nom de l'émotion
     * @param colorHex Couleur de l'ancrage
     */
    fun showAnchorReminder(
        anchorId: Long,
        emotionName: String,
        colorHex: String? = null
    ) {
        // Vérifier la permission POST_NOTIFICATIONS pour Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        // Intent pour ouvrir l'application sur l'ancrage
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("anchor_id", anchorId)
            putExtra("action", "reactivate")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            anchorId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construire la notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // TODO: Créer l'icône
            .setContentTitle(context.getString(R.string.notification_title, emotionName))
            .setContentText("Prenez un instant pour vous reconnecter à cette ressource")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 200))
            .build()

        // Afficher la notification
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_BASE + anchorId.toInt(), notification)
        }
    }

    /**
     * Annule une notification spécifique
     */
    fun cancelNotification(anchorId: Long) {
        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID_BASE + anchorId.toInt())
        }
    }

    /**
     * Annule toutes les notifications
     */
    fun cancelAllNotifications() {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
    }

    /**
     * Vérifie si les notifications sont autorisées
     */
    fun areNotificationsEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
}
