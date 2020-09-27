package com.pratthamarora.spotifyclone.exoplayer

import com.pratthamarora.spotifyclone.utils.State.*

class FirebaseMusicSource {

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
}

