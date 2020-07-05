package com.app.boombox.audio

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.app.boombox.MainActivity
import com.app.boombox.activity.PlaybackActivity
import com.app.boombox.models.Song
import com.app.boombox.viewmodels.PlaybackViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import timber.log.Timber


class AudioPlayerService : Service(), Player.EventListener {

    private lateinit var mStateBuilder: PlaybackStateCompat.Builder
    private lateinit var mMediaSession: MediaSessionCompat
    lateinit var mExoPlayer: SimpleExoPlayer
    private lateinit var extractorMediaSource: ExtractorMediaSource.Factory
    private lateinit var mediaSource: ExtractorMediaSource

    private lateinit var context: Context
    private lateinit var uri: Uri
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private val mBinder: IBinder = LocalBinder()

    private val NOTIFICATION_CHANNEL_ID = "playback_channel"
    private val NOTIFICATION_ID = 2

    private var playback: String = "Playback"

    private lateinit var songItem: Song

    lateinit var songPlaylist: ArrayList<String>
    private lateinit var singleMediaSource: MediaSource

    lateinit var playbackViewModel: PlaybackViewModel
    lateinit var audioPlayer: AudioPlayer
    lateinit var playbackActivity: PlaybackActivity

    override fun onCreate() {
        super.onCreate()

        context = this


        //Create an instance of the ExoPlayer
        val trackSelector: TrackSelector = DefaultTrackSelector()
        val loadControl: LoadControl = DefaultLoadControl()
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)


    }

    private fun startPlayer() {

        val userAgent: String =
            com.google.android.exoplayer2.util.Util.getUserAgent(this, "My ExoPlayer")
        extractorMediaSource =
            ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent))
                .setExtractorsFactory(DefaultExtractorsFactory())

        mediaSource = extractorMediaSource.createMediaSource(Uri.parse(songItem.uri))
        mExoPlayer.prepare(mediaSource)
        mExoPlayer.playWhenReady = true
        mExoPlayer.addListener(this)

        mMediaSession = MediaSessionCompat(this, this.javaClass.simpleName)
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mMediaSession.setMediaButtonReceiver(null)

        mStateBuilder = PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )

        mMediaSession.setPlaybackState(mStateBuilder.build())
        mMediaSession.setCallback(MySessionCallback(mExoPlayer))
        mMediaSession.isActive = true

        setupPlayerNotificationManager()

    }

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

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(
                PlaybackStateCompat.STATE_PLAYING,
                mExoPlayer.currentPosition, 1f
            )
            Timber.d("onPlayerStateChanged:PLAYING")

        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(
                PlaybackStateCompat.STATE_PAUSED,
                mExoPlayer.currentPosition, 1f
            )
            Timber.d("onPlayerStateChanged: PAUSED")
        }

        mMediaSession.setPlaybackState(mStateBuilder.build())
    }

    fun getplayerInstance(): SimpleExoPlayer {
        startPlayer()
        return mExoPlayer
    }

    private fun releasePlayer() {
        mExoPlayer.stop()
        mExoPlayer.release()
    }

    override fun onDestroy() {
        // playerNotificationManager.setPlayer(null)
        releasePlayer()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        songItem = intent.extras?.getParcelable("songURI")!!
        startPlayer()

        /* uri = Uri.parse(songItem.uri)

        //Start playback from AudioPlayerClass

        singleMediaSource = ExtractorMediaSource.Factory(defaultDataSourceFactory)
            .createMediaSource(uri)
        //Use Concatenating Media Source for Playlists

//            audioPlayer.setPlaybackStates(songItem)

        player.prepare(singleMediaSource)
        player.playWhenReady = true
*/


        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }


    private fun setupPlayerNotificationManager() {
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOTIFICATION_CHANNEL_ID,
            com.google.android.exoplayer2.ui.R.string.exo_download_notification_channel_name,
            NOTIFICATION_ID,
            object : MediaDescriptionAdapter {
                override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                    val intent = Intent(context, MainActivity::class.java)
                    return PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                }

                override fun getCurrentContentText(player: Player?): String? {
                    return songItem.artist
                }

                override fun getCurrentContentTitle(player: Player?): String? {
                    return songItem.name
                }

                override fun getCurrentLargeIcon(
                    player: Player?,
                    callback: PlayerNotificationManager.BitmapCallback?
                ): Bitmap? {

                    return BitmapFactory.decodeResource(resources, R.drawable.gallery_thumb)
                }

            })

        playerNotificationManager.setNotificationListener(
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationCancelled(notificationId: Int) {
                    stopSelf()
                }

                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification?
                ) {
                    startForeground(notificationId, notification)
                }

            }
        )

        playerNotificationManager.setPlayer(mExoPlayer)
    }


    inner class LocalBinder : Binder() {
        fun getService(): AudioPlayerService = this@AudioPlayerService


    }
}

