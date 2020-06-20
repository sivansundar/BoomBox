package com.app.boombox.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.boombox.models.Song
import com.app.boombox.databinding.SongItemBinding

class SongAdapter(private val songitem : List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = songitem.size


    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        holder.bind(songitem[position])
    }

    class ViewHolder(private val binding : SongItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Song) {
            binding.song = item
            binding.executePendingBindings()
        }

    }

}