package com.app.boombox.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.boombox.database.SongDAO
import com.app.boombox.models.Song

class SongRepository(private val songsDao : SongDAO) : Application() {

    private val responseLiveData: MutableLiveData<List<Song>> = MutableLiveData()

    val allSongs: LiveData<List<Song>> =
        songsDao.getAllSongs()

    suspend fun insert(song: Song) {
        songsDao.insert(song)


    }
}