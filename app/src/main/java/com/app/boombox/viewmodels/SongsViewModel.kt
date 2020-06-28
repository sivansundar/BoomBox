package com.app.boombox.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.boombox.database.BoomboxDatabase
import com.app.boombox.models.Song
import com.app.boombox.repository.SongRepository
import timber.log.Timber

class SongsViewModel(application : Application) : AndroidViewModel(application) {

    var song : MutableLiveData<List<Song>> = MutableLiveData()

    private val repository : SongRepository

    val allSongs: LiveData<List<Song>>
    val popularAlbums: LiveData<List<Song>>

    init {
        Timber.i("SongsViewModel Created")
        val songsDao = BoomboxDatabase.getDatabase(application, viewModelScope).songDao()
        val albumDao = BoomboxDatabase.getDatabase(application, viewModelScope).albumDao()
        repository = SongRepository(songsDao, albumDao)
        allSongs = repository.allSongs
        popularAlbums = repository.popularAlbums
    }


    override fun onCleared() {
        super.onCleared()
        Timber.i("SongsViewModel Destroyed")
    }

}