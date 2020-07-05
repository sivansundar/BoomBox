package com.app.boombox.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.boombox.database.AlbumDAO
import com.app.boombox.database.SongDAO
import com.app.boombox.models.Song

class SongRepository(
    private val songsDao: SongDAO,
    private val albumDAO: AlbumDAO
) : Application() {

    private val responseLiveData: MutableLiveData<List<Song>> = MutableLiveData()

    //Holds all the songs for the songs adapter
    val allSongs: LiveData<List<Song>> =
        songsDao.getAllSongs()

    val popularAlbums: LiveData<List<Song>> =
        albumDAO.getPopularAlbums()

    val top10Songs: LiveData<List<Song>> =
        songsDao.getTop10Songs()

    suspend fun insert(song: Song) {
        songsDao.insert(song)
    }
}