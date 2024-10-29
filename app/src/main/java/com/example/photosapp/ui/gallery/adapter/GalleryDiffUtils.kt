package com.example.photosapp.ui.gallery.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.photosapp.domain.model.images.ImagesDto

object GalleryDiffUtils : DiffUtil.ItemCallback<ImagesDto>() {

    override fun areItemsTheSame(oldItem: ImagesDto, newItem: ImagesDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImagesDto, newItem: ImagesDto): Boolean {
        return oldItem == newItem
    }
}