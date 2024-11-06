package com.example.photosapp.ui.gallery.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosapp.R
import com.example.photosapp.common.extensions.formatToDate
import com.example.photosapp.databinding.PhotoItemLayoutBinding
import com.example.photosapp.domain.model.images.ImagesDto


class GalleryAdapter(
    private val removeOnItemLongClick: (imageId:Int) -> Unit,
    private val navigateToImage: (imageId:Int) -> Unit
) : ListAdapter<ImagesDto,GalleryAdapter.GalleryViewHolder>(GalleryDiffUtils) {

    inner class GalleryViewHolder(private val binding: PhotoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImagesDto) {
            with(binding) {
                photoImageView.load(image.url) {
                    crossfade(true)
                    error(R.drawable.baseline_error_outline_24)
                }
                dateTextView.text = image.date.formatToDate()
                itemView.setOnLongClickListener {
                    removeOnItemLongClick(image.id)
                    true
                }
                itemView.setOnClickListener {
                    navigateToImage(image.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = PhotoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}