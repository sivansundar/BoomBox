package com.app.boombox.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.boombox.models.Song
import timber.log.Timber

class PlaybackViewModel(application: Application) : AndroidViewModel(application) {

    var playPauseState: MutableLiveData<Boolean> = MutableLiveData()

    var currentSong: MutableLiveData<Song> = MutableLiveData()

    fun setSong(song: Song) {
        currentSong.value = song

        Timber.d("Current song is ${song.name}")
    }

    fun settoPlay() {
        playPauseState.value = true
        Timber.d("Value = ${playPauseState.value}")
    }

    fun settoPause() {
        playPauseState.value = false
        Timber.d("Value = ${playPauseState.value}")

    }
}