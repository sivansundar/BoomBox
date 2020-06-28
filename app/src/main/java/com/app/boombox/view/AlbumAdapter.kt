package com.app.boombox.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.boombox.databinding.PopularAlbumsItemBinding
import com.app.boombox.models.Song

class AlbumAdapter(
    private val albumItem: List<Song>,
    context: Context?
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularAlbumsItemBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding, parent.context)

    }

    override fun getItemCount(): Int = albumItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(albumItem[position])
    }


    class ViewHolder(
        private val binding: PopularAlbumsItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Song) {
            binding.album = item
            binding.executePendingBindings()
        }
    }
}
