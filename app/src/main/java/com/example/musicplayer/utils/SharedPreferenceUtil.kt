package com.example.musicplayer.utils

import android.content.SharedPreferences
import androidx.core.net.toUri
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.utils.Constants.SAVE_CURRENT_SONG_KEY
import com.example.musicplayer.utils.Constants.SAVE_SONG_ALBUM
import com.example.musicplayer.utils.Constants.SAVE_SONG_ARTIST
import com.example.musicplayer.utils.Constants.SAVE_SONG_CONTENT_URI
import com.example.musicplayer.utils.Constants.SAVE_SONG_COVER_ART
import com.example.musicplayer.utils.Constants.SAVE_SONG_DURATION
import com.example.musicplayer.utils.Constants.SAVE_SONG_ID
import com.example.musicplayer.utils.Constants.SAVE_SONG_NAME
import com.example.musicplayer.utils.Constants.SAVE_SONG_PATH
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object SharedPreferenceUtil {

    fun saveCurrentSong(currentSong: MediaStoreSong, sharedPreferences: SharedPreferences) {
//        Gson().apply {
//            val songJson = toJson(currentSong)
//            with(sharedPreferences.edit()) {
//                putString(SAVE_CURRENT_SONG_KEY, songJson)
//                apply()
//            }
//        }

        with(sharedPreferences.edit()) {
            putLong(SAVE_SONG_ID, currentSong.id)
            putString(SAVE_SONG_NAME, currentSong.displayName)
            putString(SAVE_SONG_ARTIST, currentSong.artist)
            putString(SAVE_SONG_CONTENT_URI, currentSong.contentUri.toString())
            putString(SAVE_SONG_COVER_ART, currentSong.coverArt.toString())
            putString(SAVE_SONG_PATH, currentSong.path)
            putString(SAVE_SONG_ALBUM, currentSong.albumName)
            putLong(SAVE_SONG_DURATION, currentSong.duration)
            apply()
        }

    }

    fun getCurrentSong(sharedPreferences: SharedPreferences): MediaStoreSong? {
//        val songJson = sharedPreferences.getString(SAVE_CURRENT_SONG_KEY, null)
//        val type = object : TypeToken<MediaStoreSong?>() {}.type
//        Gson().apply {
//            return fromJson(songJson, type)
//        }

        return MediaStoreSong(sharedPreferences.getLong(SAVE_SONG_ID, -1),
            sharedPreferences.getString(SAVE_SONG_NAME, null),
            sharedPreferences.getString(SAVE_SONG_ARTIST, null),
            sharedPreferences.getString(SAVE_SONG_CONTENT_URI, null)?.toUri(),
            sharedPreferences.getString(SAVE_SONG_COVER_ART, null)?.toUri(),
            sharedPreferences.getString(SAVE_SONG_PATH, null).toString(),
            sharedPreferences.getString(SAVE_SONG_ALBUM, null),
            sharedPreferences.getLong(SAVE_SONG_DURATION, -1))
    }

    fun saveCurrentPosition(sharedPreferences: SharedPreferences, currentPosition: Int) {
        with(sharedPreferences.edit()) {
            putInt(Constants.CURRENT_SONG_DURATION_KEY, currentPosition)
            apply()
        }
    }

    fun getPosition(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(Constants.POSITION_KEY, 0)
    }
}