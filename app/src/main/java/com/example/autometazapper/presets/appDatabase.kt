package com.example.autometazapper.ui

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Preset::class], // our table
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetDao
}
