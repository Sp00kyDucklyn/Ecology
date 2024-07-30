package com.cursokotlin.eco.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.eco.HomeFragment
import com.cursokotlin.eco.HomeFragmentDirections
import com.cursokotlin.eco.ProjectFragmentDirections
import com.cursokotlin.eco.databinding.ItemAlbumBinding
import com.cursokotlin.eco.model.Album

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val itemBinding: ItemAlbumBinding): RecyclerView.ViewHolder(itemBinding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.albumCoords == newItem.albumCoords &&
                    oldItem.albumTitle == newItem.albumTitle
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentAlbum = differ.currentList[position]

        holder.itemBinding.albumTitle.text = currentAlbum.albumTitle

        holder.itemView.setOnClickListener{
            val directions = ProjectFragmentDirections.actionProjectFragmentToAlbumFragment(currentAlbum)
            it.findNavController().navigate(directions)
        }
    }


}