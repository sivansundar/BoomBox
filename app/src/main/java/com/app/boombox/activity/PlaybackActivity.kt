package com.app.boombox.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.boombox.R
import com.app.boombox.audio.AudioPlayer
import com.app.boombox.audio.AudioPlayerService
import com.app.boombox.audio.AudioPlayerService.LocalBinder
import com.app.boombox.databinding.ActivityPlaybackBinding
import com.app.boombox.models.Song
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.util.Util
import timber.log.Timber


class PlaybackActivity : AppCompatActivity(), Player.EventListener {

    private lateinit var mStateBuilder: PlaybackStateCompat.Builder
    private lateinit var mMediaSession: MediaSessionCompat
    lateinit var songItem: Song
    lateinit var binding: ActivityPlaybackBinding
    lateinit var audioPlayer: AudioPlayer
    lateinit var mExoPlayer: SimpleExoPlayer
    lateinit var mPlayerView: PlayerControlView

    private lateinit var mService: AudioPlayerService
    private var mBound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_playback)


        //Get Song Object from the Intent
        songItem = intent.extras?.getParcelable("songURI")!!

        //Create an instance of the ExoPlayer
        val trackSelector: TrackSelector = DefaultTrackSelector()
        val loadControl: LoadControl = DefaultLoadControl()


        //Call foreground service


        initPlayerView()

        val intent = Intent(this, AudioPlayerService::class.java)
        intent.putExtra("songURI", songItem)
        Util.startForegroundService(this, intent)

    }


    fun initPlayerView() {
        mPlayerView = binding.playerView
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Timber.d("Service connected")
            val binder = iBinder as LocalBinder
            mService = binder.getService()
            mBound = true
            initializePlayer()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Timber.d("Service disconnected")

            mBound = false
        }
    }

    private fun initializePlayer() {
        if (mBound) {
            val player = mService.getplayerInstance()
            mPlayerView.player = player
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, AudioPlayerService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        }

        initializePlayer()
    }


    override fun onStop() {
        unbindService(mConnection)
        mBound = false
        super.onStop()
    }

    override fun onRepeatModeChanged(repeatMode: Int) {}

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

    override fun onPlayerError(error: ExoPlaybackException?) {}

    override fun onPositionDiscontinuity(reason: Int) {}

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}

    override fun onSeekProcessed() {}


}
