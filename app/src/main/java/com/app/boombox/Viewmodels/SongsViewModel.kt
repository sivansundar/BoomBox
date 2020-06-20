package com.app.boombox.Viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.boombox.models.Song
import com.app.boombox.Repository.SongRepository
import com.app.boombox.database.BoomboxDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SongsViewModel(application : Application) : AndroidViewModel(application) {

    var song : MutableLiveData<List<Song>> = MutableLiveData()

    private val repository : SongRepository

    val allSongs : LiveData<List<Song>>

    init {
        Timber.i("SongsViewModel Created")
        val songsDao = BoomboxDatabase.getDatabase(application, viewModelScope).songDao()
        repository = SongRepository(songsDao)
        allSongs = repository.allSongs
    }


    override fun onCleared() {
        super.onCleared()
        Timber.i("SongsViewModel Destroyed")
    }

}