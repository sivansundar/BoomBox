package com.app.boombox.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.boombox.activity.PlaybackActivity
import com.app.boombox.databinding.SongItemBinding
import com.app.boombox.models.Song
import timber.log.Timber


class SongAdapter(
    val songitem: List<Song?>,
    context: Context?
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding, parent.context, songitem.size)
    }

    override fun getItemCount(): Int = songitem.size


    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        songitem[position]?.let {
            holder.bind(it, songitem)
        }


    }

    class ViewHolder(
        private val binding: SongItemBinding,
        private val context: Context,
        size: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Song,
            songitem: List<Song?>
        ) {


            binding.song = item
            binding.executePendingBindings()

            //   binding.albumArtImageView.setImageBitmap(BitmapFactory.decodeFile(item.albumArt))

            val songQueue: ArrayList<Song> = ArrayList()

            /*   for (songitems in songitem.get(adapterPosition) to songitem.size) {
                   if (songitems != null) {
                       songQueue.add(songitems)
                   }
               }
   */
            binding.songCardView.setOnClickListener { itemView ->
                Timber.i("Values : ${item.name} : URI ${item.uri}")

                Timber.d("Adapter Position : $adapterPosition")
                /*val intent = Intent(context, AudioPlayerService::class.java)
                intent.putExtra("songURI", item)
                Util.startForegroundService(context, intent)*/


                val currentSongActivity = Intent(context, PlaybackActivity::class.java)
                    .putExtra("songURI", item)

                context.startActivity(currentSongActivity)

            }

        }

    }

    fun startIntent() {
    }
}