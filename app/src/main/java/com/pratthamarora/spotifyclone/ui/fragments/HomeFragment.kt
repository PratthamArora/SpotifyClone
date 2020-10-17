package com.pratthamarora.spotifyclone.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratthamarora.spotifyclone.R
import com.pratthamarora.spotifyclone.ui.adapters.SongAdapter
import com.pratthamarora.spotifyclone.ui.viewmodels.MainViewModel
import com.pratthamarora.spotifyclone.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeObservers()

        songAdapter.setItemClickListener {
            viewModel.playOrToggleSong(it)
        }
    }

    private fun subscribeObservers() {
        viewModel.mediaItems.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    allSongsProgressBar.isVisible = false
                    it.data?.let { songs ->
                        songAdapter.songs = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> allSongsProgressBar.isVisible = true
            }
        }
    }

    private fun setupRecyclerView() = rvAllSongs.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = songAdapter
    }
}