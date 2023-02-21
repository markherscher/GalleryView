package com.herscher.galleryviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.herscher.galleryviewer.databinding.FragmentAlbumsBinding
import com.herscher.galleryviewer.databinding.ViewHolderAlbumBinding
import com.herscher.galleryviewer.util.showOnlyChildView
import com.herscher.galleryviewer.viewmodel.AlbumsViewModel
import androidx.fragment.app.viewModels
import com.herscher.galleryviewer.R
import com.herscher.galleryviewer.domain.Album
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumsFragment : Fragment() {
    private var _binding: FragmentAlbumsBinding? = null
    private val binding: FragmentAlbumsBinding get() = _binding!!
    private val viewModel: AlbumsViewModel by viewModels()

    // Do not hold a reference to the adapter as that could leak memory if the view is destroyed
    private val albumAdapter get() = binding.albumRecyclerView.adapter as AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)

        binding.apply {
            albumRecyclerView.adapter = AlbumAdapter(inflater) { albumId, albumName ->
                // This is where we should use a nav framework, by either rolling our own or using
                // something like Jetpack Navigation. This is not a normal way to navigate.
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        PhotosFragment.newInstance(albumId = albumId, albumName = albumName)
                    )
                    .addToBackStack(null)
                    .commit()
            }
            albumRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            retryButton.setOnClickListener { viewModel.refresh() }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::renderUiState)
            }
        }

        viewModel.viewCreated()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Avoid memory leaks; could lifecycle observer here as well
        _binding = null
    }

    private fun renderUiState(uiState: AlbumsViewModel.UiState) {
        val viewToShow = when (uiState) {
            is AlbumsViewModel.UiState.Content -> {
                albumAdapter.albumList = uiState.albums
                binding.contentLayout
            }
            AlbumsViewModel.UiState.Error -> {
                binding.errorLayout
            }
            AlbumsViewModel.UiState.Loading -> {
                binding.loadingLayout
            }
        }

        binding.container.showOnlyChildView(viewToShow)
    }

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    private class AlbumAdapter(
        private val inflater: LayoutInflater,
        private val clickHandler: (Int, String) -> Unit
    ) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

        var albumList: List<Album> = emptyList()
            set(value) {
                field = value

                // Note: could add a diff util here to only notify about the specifically changed
                // indexes
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            return AlbumViewHolder(ViewHolderAlbumBinding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            val album = albumList[position]
            holder.binding.albumTitle.text = album.title
            holder.binding.root.setOnClickListener {
                clickHandler(album.id, album.title)
            }
        }

        override fun getItemCount(): Int = albumList.size

        private inner class AlbumViewHolder(val binding: ViewHolderAlbumBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}