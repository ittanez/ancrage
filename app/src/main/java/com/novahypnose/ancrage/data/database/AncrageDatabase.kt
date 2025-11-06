package com.novahypnose.ancrage.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.novahypnose.ancrage.data.database.dao.AnchorDao
import com.novahypnose.ancrage.data.database.dao.ReactivationDao
import com.novahypnose.ancrage.data.database.dao.ReminderDao
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.data.database.entities.Reactivation
import com.novahypnose.ancrage.data.database.entities.Reminder

/**
 * Base de données Room principale de l'application AncrAge
 */
@Database(
    entities = [
        Anchor::class,
        Reactivation::class,
        Reminder::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AncrageDatabase : RoomDatabase() {

    abstract fun anchorDao(): AnchorDao
    abstract fun reactivationDao(): ReactivationDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        private const val DATABASE_NAME = "ancrage_database"

        @Volatile
        private var INSTANCE: AncrageDatabase? = null

        fun getInstance(context: Context): AncrageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AncrageDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Pour le développement
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
