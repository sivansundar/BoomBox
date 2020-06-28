package com.app.boombox.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.boombox.audio.AudioPlayerService
import com.app.boombox.databinding.SongItemBinding
import com.app.boombox.models.Song
import com.google.android.exoplayer2.util.Util
import timber.log.Timber


class SongAdapter(
    private val songitem: List<Song>,
    context: Context?
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = songitem.size


    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        holder.bind(songitem[position])


    }

    class ViewHolder(
        private val binding: SongItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song) {
            binding.song = item
            binding.executePendingBindings()
            //   binding.albumArtImageView.setImageBitmap(BitmapFactory.decodeFile(item.albumArt))




            binding.songCardView.setOnClickListener { itemView ->
                Timber.i("Values : ${item.name} : URI ${item.uri}")
                val intent = Intent(context, AudioPlayerService::class.java)
                intent.putExtra("songURI", item)
                Util.startForegroundService(context, intent)


            }

        }

    }

    fun startIntent() {
    }
}