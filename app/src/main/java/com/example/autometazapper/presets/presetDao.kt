package com.example.autometazapper.ui

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PresetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: Preset)

    @Query("SELECT * FROM presets")
    suspend fun getAll(): List<Preset>

    @Query("SELECT * FROM presets WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Preset?
}
