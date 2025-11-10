package com.example.autometazapper.ui

@Database(entities = [Preset::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetDao
}
