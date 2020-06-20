package com.app.boombox.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.boombox.models.Song

@Dao
interface SongDAO {

    @Query("SELECT * FROM song_table ORDER BY name ASC")
    fun getAllSongs() : LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: Song)
}