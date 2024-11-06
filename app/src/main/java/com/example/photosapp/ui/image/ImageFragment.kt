package com.example.photosapp.ui.image

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photosapp.R
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.common.extensions.formatToDate
import com.example.photosapp.databinding.FragmentImageBinding
import com.example.photosapp.ui.image.adapter.CommentsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : BaseFragment<FragmentImageBinding>() {

    private val viewModel: ImageViewModel by viewModels()

    private val adapter by lazy {
        CommentsAdapter (
            removeOnItemLongClick =  { image ->
                showDeleteConfirmationDialog(image)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getInt("imageId")?.let { viewModel.setImageId(it) }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galleryRecyclerView.adapter = adapter
        viewModel.getImageById()
        viewModel.loadNextItems()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.imageScreenState.collect { screenState ->
                updateUi(screenState)
                adapter.submitList(screenState.items)
                screenState.error?.let { errorMessage ->
                    Toast.makeText(context, "End of list!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        bindRecyclerScroll()

        binding.sendMessageIcon.setOnClickListener {
            viewModel.sendComment(binding.commentTextInputField.text.toString())
            hideKeyboard()
            binding.commentTextInputField.text?.clear()
        }
    }

    private fun bindRecyclerScroll(){
        binding.galleryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextItems()
                }
            }
        })
    }

    private fun updateUi(screenState: ImageScreenState) {
        with(binding) {
            photoImageView.load(screenState.imageData?.url) {
                crossfade(true)
            }
            dateTextView.text = screenState.imageData?.date?.formatToDate()
        }

    }
    private fun showDeleteConfirmationDialog(imageId:Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение удаления")
            .setMessage("Вы уверены, что хотите удалить этот комментарий?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.removeCommentById(imageId)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentImageBinding = FragmentImageBinding.inflate(inflater, container, false)

}

