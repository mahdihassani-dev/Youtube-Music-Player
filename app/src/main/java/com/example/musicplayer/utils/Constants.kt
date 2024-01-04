package com.example.musicplayer.utils

import android.provider.MediaStore

object Constants {

    private const val packageName = "com.zakariya.mymusicplayer"
    const val PREF_NAME = "$packageName.SHARED_PREF"
    const val POSITION_KEY = "${packageName}.position"
    const val CURRENT_SONG_DURATION_KEY = "$packageName.currentSongDurationKey"
    const val SAVE_CURRENT_SONG_KEY = "Save currently playing song"

    const val SAVE_SONG_ID = "song save id"
    const val SAVE_SONG_NAME = "song save name"
    const val SAVE_SONG_ARTIST = "song save artist"
    const val SAVE_SONG_CONTENT_URI = "song save content uri"
    const val SAVE_SONG_COVER_ART = "song save cover art"
    const val SAVE_SONG_PATH = "song save path"
    const val SAVE_SONG_ALBUM = "song save album"
    const val SAVE_SONG_DURATION = "song save duration"

    const val REQ_CODE = 0
    const val NOTIFICATION_CHANNEL_ID = "${packageName}.Music Player"
    const val NOTIFICATION_CHANNEL_NAME = "${packageName}.Music"
    const val NOTIFICATION_ID = 1

    @Suppress("DEPRECATION")
    val baseProjection = arrayOf(
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.ArtistColumns.ARTIST,
        MediaStore.Audio.AlbumColumns.ALBUM,
        MediaStore.Audio.AudioColumns.DURATION
    )
}


