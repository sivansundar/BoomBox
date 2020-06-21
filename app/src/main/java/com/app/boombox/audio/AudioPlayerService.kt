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
import android.os.IBinder
import com.app.boombox.MainActivity
import com.app.boombox.models.Song
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class AudioPlayerService : Service() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var context: Context
    private lateinit var uri: Uri
    private lateinit var playerNotificationManager: PlayerNotificationManager


    private val NOTIFICATION_CHANNEL_ID = "playback_channel"
    private val NOTIFICATION_ID = 2

    private var playback: String = "Playback"

    private lateinit var songItem: Song

    override fun onCreate() {
        super.onCreate()



        context = this
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())


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

                override fun getCurrentContentTitle(player: Player?): String {
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

        playerNotificationManager.setPlayer(player)

    }

    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            songItem = intent.extras?.getParcelable("songURI")!!
            uri = Uri.parse(songItem.uri)

            var defaultDataSourceFactory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "AudioDef")
            )

            var mediaSource = ExtractorMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(uri)

            //Use Concatenating Media Source for Playlists

            player.prepare(mediaSource)
            player.playWhenReady = true

        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


}