package com.pratthamarora.spotifyclone.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.pratthamarora.spotifyclone.data.remote.MusicDatabase
import com.pratthamarora.spotifyclone.utils.State.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: MusicDatabase
) {

    var songs = emptyList<MediaMetadataCompat>()
    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()
    private var state = CREATED
        set(value) {
            if (value == INITIALIZED || value == ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach {
                        it(state == INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun onReady(action: (Boolean) -> Unit): Boolean {
        return if (state == CREATED || state == INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == INITIALIZED)
            true
        }
    }

    suspend fun fetchMediaData() = withContext(IO) {
        state = INITIALIZING
        val allSongs = musicDatabase.getAllSongs()
        songs = allSongs.map {
            Builder()
                .putString(METADATA_KEY_ARTIST, it.subtitle)
                .putString(METADATA_KEY_MEDIA_ID, it.mediaId)
                .putString(METADATA_KEY_TITLE, it.title)
                .putString(METADATA_KEY_DISPLAY_TITLE, it.title)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, it.imageUrl)
                .putString(METADATA_KEY_MEDIA_URI, it.songUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, it.imageUrl)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, it.subtitle)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, it.subtitle)
                .build()
        }
        state = INITIALIZED
    }

    fun mediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it.getString(METADATA_KEY_MEDIA_URI).toUri())
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun mediaItems() = songs.map {
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(it.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(it.description.title)
            .setSubtitle(it.description.subtitle)
            .setMediaId(it.description.mediaId)
            .setIconUri(it.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }

}

