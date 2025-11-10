package com.example.autometazapper.ui

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "presets_db"
            ).build()
        }
        return INSTANCE!!
    }
}
