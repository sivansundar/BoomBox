package com.app.boombox.audio

import android.content.Context
import android.view.LayoutInflater
import com.app.boombox.databinding.ActivityPlaybackBinding
import com.app.boombox.models.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import timber.log.Timber


data class AudioPlayer(val context: Context) : Player.EventListener {

    lateinit var mExoPlayer: ExoPlayer
    private lateinit var singleMediaSource: MediaSource
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    lateinit var player: SimpleExoPlayer

    val binding2 = ActivityPlaybackBinding.inflate(LayoutInflater.from(context))


    private lateinit var songItem: Song


    init {
        Timber.d("Audio Player Class Initialized")


    }


    fun initPlayer() {
        Timber.d("Audio Player init Initialized")


    }


    fun startPlaying(mediaSource: ExtractorMediaSource?) {
        Timber.d("Start playing called")


    }

    fun getExoPlayer(): SimpleExoPlayer {
        Timber.d("getExoPlayer called")

        return player

    }
}