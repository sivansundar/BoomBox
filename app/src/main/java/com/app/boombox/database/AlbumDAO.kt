package com.app.boombox.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.app.boombox.models.Song

@Dao
interface AlbumDAO {


    /*  Query written for pulling out random Albums. Use this to reproduce the error at []
      @Query("SELECT album_id, album_name, album_art, artist FROM song_table ORDER BY RAND() LIMIT 5")
       @Query("SELECT album_id, album_name, album_art, artist FROM song_table LIMIT 5")
  */

    @Query("SELECT album_id, album_name, album_art, artist FROM song_table ORDER BY RANDOM() LIMIT 5")
    fun getPopularAlbums(): LiveData<List<Song>>
}