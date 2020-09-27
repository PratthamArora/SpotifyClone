package com.pratthamarora.spotifyclone.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pratthamarora.spotifyclone.data.model.Song
import com.pratthamarora.spotifyclone.utils.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await

class MusicDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> =
        try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
}