package com.example.musicplayer.utils

import android.content.SharedPreferences
import android.media.MediaMetadataRetriever
import com.example.musicplayer.model.MediaStoreSong


object PlayerHelper {
    fun getCurrentSong(sharedPreferences: SharedPreferences): MediaStoreSong? {
        return if (MusicPlayerRemote.songService?.mediaPlayer != null && MusicPlayerRemote.songService?.currentSong != null) {
            MusicPlayerRemote.songService?.currentSong
        } else {
            SharedPreferenceUtil.getCurrentSong(sharedPreferences)
        }
    }

    fun getSongThumbnail(songPath: String): ByteArray? {
        var imgByte: ByteArray?
        MediaMetadataRetriever().also {
            try {
                it.setDataSource(songPath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            imgByte = it.embeddedPicture
            it.release()
        }
        return imgByte
    }
}

