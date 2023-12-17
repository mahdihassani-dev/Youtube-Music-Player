package com.example.musicplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ItemSongBinding
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.utils.MusicPlayerRemote

class SongAdapter(
    private val songs: List<MediaStoreSong>,
    private val callBack: OnItemClick,
) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(song: MediaStoreSong, pos: Int) {


            binding.itemTxtTitle.text = song.displayName
            binding.itemTxtArtist.text = song.artist
            Glide
                .with(binding.itemImgCover)
                .load(song.coverArt)
                .placeholder(R.drawable.place_holder)
                .into(binding.itemImgCover)

            itemView.setOnClickListener {
                callBack.onItemClickListener(song, pos)
                MusicPlayerRemote.sendAllSong(songs.toMutableList(), pos)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(songs[position], position)
    }

    interface OnItemClick {
        fun onItemClickListener(song: MediaStoreSong, pos: Int)
    }

}