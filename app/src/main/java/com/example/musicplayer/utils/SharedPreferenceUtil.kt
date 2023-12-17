package com.example.musicplayer.utils

import android.content.SharedPreferences
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.utils.Constants.SAVE_CURRENT_SONG_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object SharedPreferenceUtil {

    fun saveCurrentSong(currentSong: MediaStoreSong, sharedPreferences: SharedPreferences) {
        Gson().apply {
            val songJson = toJson(currentSong)
            with(sharedPreferences.edit()) {
                putString(SAVE_CURRENT_SONG_KEY, songJson)
                apply()
            }
        }
    }

    fun getCurrentSong(sharedPreferences: SharedPreferences): MediaStoreSong? {
        val songJson = sharedPreferences.getString(SAVE_CURRENT_SONG_KEY, null)
        val type = object : TypeToken<MediaStoreSong?>() {}.type
        Gson().apply {
            return fromJson(songJson, type)
        }
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