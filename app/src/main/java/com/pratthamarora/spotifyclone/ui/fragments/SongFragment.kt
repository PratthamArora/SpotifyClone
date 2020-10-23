package com.pratthamarora.spotifyclone.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.pratthamarora.spotifyclone.R
import com.pratthamarora.spotifyclone.data.model.Song
import com.pratthamarora.spotifyclone.exoplayer.toSong
import com.pratthamarora.spotifyclone.ui.viewmodels.MainViewModel
import com.pratthamarora.spotifyclone.ui.viewmodels.SongViewModel
import com.pratthamarora.spotifyclone.utils.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song.*
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    @Inject
    lateinit var glide: RequestManager

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val songViewModel by viewModels<SongViewModel>()
    private var curPlayingSong: Song? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

    }

    private fun updateData(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
        tvSongName.text = title
        glide.load(song.imageUrl).into(ivSongImage)
    }

    private fun subscribeObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if (curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateData(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }

            }
        }

        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateData(curPlayingSong!!)
        }

    }
}