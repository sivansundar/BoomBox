package com.app.boombox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.app.boombox.database.BoomboxDatabase
import com.app.boombox.database.SongDAO
import com.app.boombox.databinding.ActivityMainBinding
import com.app.boombox.models.Song
import com.app.boombox.viewmodels.SongsViewModel
import com.mtechviral.mplaylib.MusicFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val READ_EXTERNAL_STORAGE_CODE = 1
    lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: SongsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.mainToolbar)
        setupPermissions()

        val navigation = Navigation.findNavController(this, R.id.nav_host_fragment)

        binding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_item -> {
                    // Respond to navigation item 1 click
                    Timber.d("onCreate: HOME")
                    navigation.navigate(R.id.mainFragment)
                    true
                }
                R.id.albums_item -> {
                    // Respond to navigation item 2 click
                    Log.d("TAG", "onCreate: Album")
                    navigation.navigate(R.id.albumFragment)

                    true
                }

                R.id.playlist_item -> {
                    Log.d("TAG", "onCreate: Playlist")
                    navigation.navigate(R.id.playlistFragment)
                    true

                }
                else -> false
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
                    song.albumArt.toString()
                )
            )
        }

        Timber.i("Total songs : ${songs.size}" )

    }

}