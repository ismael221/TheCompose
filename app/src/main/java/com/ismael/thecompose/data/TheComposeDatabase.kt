package com.ismael.thecompose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class TheComposeDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {

        @Volatile
        private var Instance: TheComposeDatabase? = null

        fun getDatabase(context: Context): TheComposeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TheComposeDatabase::class.java, "thecompose_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}