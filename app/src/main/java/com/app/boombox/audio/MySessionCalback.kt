package com.app.boombox.audio

import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.SimpleExoPlayer

class MySessionCallback(mExoPlayer: SimpleExoPlayer) : MediaSessionCompat.Callback() {

    val player = mExoPlayer

    override fun onPlay() {

        player.playWhenReady = true
    }

    override fun onPause() {
        player.playWhenReady = false
    }


    override fun onSkipToPrevious() {
        player.seekTo(0)
    }
}