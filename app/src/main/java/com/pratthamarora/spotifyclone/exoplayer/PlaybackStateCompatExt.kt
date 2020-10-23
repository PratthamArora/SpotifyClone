package com.pratthamarora.spotifyclone.exoplayer

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*

inline val PlaybackStateCompat.isPrepared
    get() = state == STATE_BUFFERING || state == STATE_PLAYING ||
            state == STATE_PAUSED

inline val PlaybackStateCompat.isPlaying
    get() = state == STATE_BUFFERING || state == STATE_PLAYING

inline val PlaybackStateCompat.isPlayEnabled
    get() = actions and ACTION_PLAY != 0L ||
            (actions and ACTION_PLAY_PAUSE != 0L &&
                    state == STATE_PAUSED)

inline val PlaybackStateCompat.currentPlaybackPosition: Long
    get() = if (state == STATE_PLAYING) {
        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
        (position + (timeDelta * playbackSpeed)).toLong()
    } else position