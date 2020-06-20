package com.app.boombox.Repository

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.boombox.database.SongDAO
import com.app.boombox.models.Song
import timber.log.Timber

class SongRepository(private val songsDao : SongDAO) : Application() {

    private val responseLiveData: MutableLiveData<List<Song>> = MutableLiveData()

    val allSongs: LiveData<List<Song>> =
        songsDao.getAllSongs()

    suspend fun insert(song: Song) {
        songsDao.insert(song)


    }
}