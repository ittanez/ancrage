package com.novahypnose.ancrage.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilitaires pour la gestion des dates et heures
 */
object DateTimeUtils {

    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.FRENCH)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.FRENCH)
    private val dateTimeFormat = SimpleDateFormat("d MMM yyyy 'à' HH:mm", Locale.FRENCH)

    /**
     * Convertit un timestamp en date lisible
     * Ex: "3 nov. 2025"
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    /**
     * Convertit un timestamp en heure lisible
     * Ex: "14:30"
     */
    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    /**
     * Convertit un timestamp en date et heure lisibles
     * Ex: "3 nov. 2025 à 14:30"
     */
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    /**
     * Retourne le timestamp de début de journée
     */
    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * Retourne le timestamp de fin de journée
     */
    fun getEndOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    /**
     * Retourne le timestamp d'il y a X jours
     */
    fun getDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        return calendar.timeInMillis
    }

    /**
     * Retourne une description relative du temps écoulé
     * Ex: "Il y a 5 minutes", "Il y a 2 heures", "Il y a 3 jours"
     */
    fun getRelativeTimeString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "À l'instant"
            minutes < 60 -> "Il y a ${minutes}min"
            hours < 24 -> "Il y a ${hours}h"
            days < 7 -> "Il y a ${days}j"
            else -> formatDate(timestamp)
        }
    }

    /**
     * Calcule le timestamp pour un moment de la journée
     * @param timeOfDay 'morning', 'noon', 'afternoon', 'evening', 'random'
     */
    fun getTimestampForTimeOfDay(timeOfDay: String): Long {
        val calendar = Calendar.getInstance()

        val (hour, minute) = when (timeOfDay.lowercase()) {
            "morning" -> Pair(8, 0)
            "noon" -> Pair(12, 30)
            "afternoon" -> Pair(16, 0)
            "evening" -> Pair(19, 30)
            "random" -> {
                // Entre 9h et 21h
                val randomHour = (9..21).random()
                val randomMinute = (0..59).random()
                Pair(randomHour, randomMinute)
            }
            else -> Pair(12, 0)
        }

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Si l'heure est déjà passée aujourd'hui, passer au lendemain
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        return calendar.timeInMillis
    }

    /**
     * Vérifie si un timestamp est dans les heures de sommeil (23h-7h)
     */
    fun isInSleepTime(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour >= 23 || hour < 7
    }
}
