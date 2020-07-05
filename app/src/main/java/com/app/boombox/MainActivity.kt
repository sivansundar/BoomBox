package com.app.boombox

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.app.boombox.database.BoomboxDatabase
import com.app.boombox.database.SongDAO
import com.app.boombox.databinding.ActivityMainBinding
import com.app.boombox.fragments.AlbumsFragment
import com.app.boombox.fragments.MainFragment
import com.app.boombox.fragments.PlaylistFragment
import com.app.boombox.fragments.SongsFragment
import com.app.boombox.models.Song
import com.app.boombox.viewmodels.PlaybackViewModel
import com.app.boombox.viewmodels.SongsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mtechviral.mplaylib.MusicFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val READ_EXTERNAL_STORAGE_CODE = 1

    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SongsViewModel
    private lateinit var playbackViewModel: PlaybackViewModel

    var pager: ViewPager? = null

    private lateinit var standardBottomSheetBehavior: BottomSheetBehavior<View>

    private val startColor = Color.parseColor("#00FFFFFF")
    private val endColor = Color.parseColor("#FFFFFFFF")
    private val textColor = Color.parseColor("#FF000000")


    private lateinit var MainFragment: MainFragment
    private lateinit var AlbumsFragment: AlbumsFragment
    private lateinit var PlaylistFragment: PlaylistFragment
    private lateinit var SongsFragment: SongsFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        MainFragment = MainFragment()
        AlbumsFragment = AlbumsFragment()
        PlaylistFragment = PlaylistFragment()
        SongsFragment = SongsFragment()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // setSupportActionBar(binding.mainToolbar)

        binding.tabbedLayout.setupWithViewPager(binding.viewPager)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(MainFragment, "Overview")
        viewPagerAdapter.addFragment(SongsFragment, "Songs")

        viewPagerAdapter.addFragment(AlbumsFragment, "Albums")
        viewPagerAdapter.addFragment(PlaylistFragment, "Playlist")
        binding.viewPager.adapter = viewPagerAdapter

        viewPagerAdapter.notifyDataSetChanged()
        setupPermissions()


        playbackViewModel = ViewModelProviders.of(this).get(PlaybackViewModel::class.java)


        binding.playpauseButton.setOnClickListener { view ->

            if (binding.playpauseButton.background.constantState != resources.getDrawable(
                    R.drawable.round_play_arrow_black_48,
                    theme
                ).constantState
            ) {
                binding.playpauseButton.background =
                    resources.getDrawable(R.drawable.round_pause_black_48, theme)


            } else {
                binding.playpauseButton.background =
                    resources.getDrawable(R.drawable.round_play_arrow_black_48, theme)


            }


        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Timber.i("Permission to read external storage denied")
            makeRequest()
        } else {
            Timber.i("Permission to read external storage has already been granted.")

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            READ_EXTERNAL_STORAGE_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Timber.i("Permission has been denied by the user")
                } else {
                    Timber.i("Permission has been granted by the user")
                    getSongs()
                }
            }
        }
    }

    private fun getSongs() {
        viewModel = ViewModelProviders.of(this).get(SongsViewModel::class.java)
        val songsDao = BoomboxDatabase.getDatabase(application, lifecycleScope).songDao()
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            getSongsFromDevice(songsDao)

        }

    }

    suspend fun getSongsFromDevice(
        songDAO: SongDAO
    ) {

        Timber.i("getSongsFromDevice called")

        val musicFinder = MusicFinder(contentResolver)
        musicFinder.prepare()

        val songs = musicFinder.allSongs


        for (song in songs) {
            Timber.i("ID : ${song.id} \nTitle : ${song.title} \nArtist : ${song.artist} \n Duration : ${song.duration} \nURI : ${song.uri} ")
            songDAO.insert(
                Song(
                    song.id,
                    song.title,
                    song.artist,
                    song.duration,
                    song.uri.toString(),
                    song.albumArt.toString(),
                    song.albumId,
                    song.album
                )
            )
        }
        Timber.i("Total songs : ${songs.size}" )

    }

}