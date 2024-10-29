package com.example.photosapp.ui.image.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photosapp.common.extensions.formatToDate
import com.example.photosapp.databinding.CommentItemLayoutBinding
import com.example.photosapp.domain.model.comment.CommentDto

class CommentsAdapter(
    private val removeOnItemLongClick: (commentId: Int) -> Unit
) : ListAdapter<CommentDto, CommentsAdapter.CommentsViewHolder>(CommentsDiffUtils) {

    inner class CommentsViewHolder(private val binding: CommentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentDto) {
            with(binding){
                commentTextView.text = comment.text
                dateTextView.text = comment.date.formatToDate()
                itemView.setOnLongClickListener {
                    removeOnItemLongClick(comment.id)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = CommentItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}