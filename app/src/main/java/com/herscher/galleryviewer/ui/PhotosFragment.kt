package com.herscher.galleryviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.herscher.galleryviewer.R
import com.herscher.galleryviewer.databinding.FragmentPhotosBinding
import com.herscher.galleryviewer.databinding.ViewHolderPhotoBinding
import com.herscher.galleryviewer.domain.Photo
import com.herscher.galleryviewer.util.showOnlyChildView
import com.herscher.galleryviewer.viewmodel.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosFragment : Fragment() {
    private var _binding: FragmentPhotosBinding? = null
    private val binding: FragmentPhotosBinding get() = _binding!!
    private val viewModel: PhotosViewModel by viewModels()

    // Do not hold a reference to the adapter as that could leak memory if the view is destroyed
    private val albumAdapter get() = binding.albumRecyclerView.adapter as PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        binding.apply {
            // Could base the column count on the width of the device by using a resource ID to
            // provide the integer value, which would be larger the wider the device
            albumRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            albumRecyclerView.adapter = PhotoAdapter(inflater, Glide.with(this@PhotosFragment))
            retryButton.setOnClickListener { viewModel.refresh() }
            albumName.text = (arguments?.getString(ALBUM_NAME_KEY)) ?: ""
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::renderUiState)
            }
        }

        val albumId = arguments?.getInt(ALBUM_ID_KEY)
            ?: throw java.lang.IllegalStateException("missing album ID")
        viewModel.viewCreated(albumId)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Avoid memory leaks; could lifecycle observer here as well
        _binding = null
    }

    private fun renderUiState(uiState: PhotosViewModel.UiState) {
        val viewToShow = when (uiState) {
            is PhotosViewModel.UiState.Content -> {
                albumAdapter.photoList = uiState.photos
                binding.contentLayout
            }
            PhotosViewModel.UiState.Error -> {
                binding.errorLayout
            }
            PhotosViewModel.UiState.Loading -> {
                binding.loadingLayout
            }
        }

        binding.container.showOnlyChildView(viewToShow)
    }

    private class PhotoAdapter(
        private val inflater: LayoutInflater,
        private val glide: RequestManager
    ) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

        var photoList: List<Photo> = emptyList()
            set(value) {
                field = value

                // Note: could add a diff util here to only notify about the specifically changed
                // indexes
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            return PhotoViewHolder(ViewHolderPhotoBinding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.binding.photoName.text = photoList[position].title
            glide
                .load(photoList[position].thumbnailUrl)
                .centerInside()
                .placeholder(R.drawable.ic_placeholder_photo)
                .into(holder.binding.photo)
        }

        override fun getItemCount(): Int = photoList.size

        private inner class PhotoViewHolder(val binding: ViewHolderPhotoBinding) :
            RecyclerView.ViewHolder(binding.root)
    }

    companion object {
        private const val ALBUM_ID_KEY = "album_id_key"
        private const val ALBUM_NAME_KEY = "album_name_key"

        fun newInstance(albumId: Int, albumName: String) = PhotosFragment().apply {
            arguments = Bundle().apply {
                putInt(ALBUM_ID_KEY, albumId)
                putString(ALBUM_NAME_KEY, albumName)
            }
        }
    }
}