package com.example.photosapp.ui.image.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.images.ImagesDto

object CommentsDiffUtils : DiffUtil.ItemCallback<CommentDto>() {

    override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
        return oldItem == newItem
    }
}