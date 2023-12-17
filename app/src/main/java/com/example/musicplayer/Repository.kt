package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.musicplayer.model.MediaStoreSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit


class Repository(private val application: Application) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun querySongs(): List<MediaStoreSong> {
        val songs = mutableListOf<MediaStoreSong>()

        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION
            )

            val selection = "${MediaStore.Audio.Media.DATE_ADDED} >= ?"
            val selectionArgs = arrayOf(
                // Release day of the G1. :)
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val artistColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val pathColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)


                while (cursor.moveToNext()) {

                    // Here we'll use the column indexs that we found above.
                    val id = cursor.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)
                    val artist = cursor.getString(artistColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val path = cursor.getString(pathColumn)
                    val album = cursor.getString(albumColumn)
                    val duration = cursor.getLong(durationColumn)


                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val sArt = Uri.parse("content://media/external/audio/albumart")
                    val uri = ContentUris.withAppendedId(sArt, albumId)


                    val song =
                        MediaStoreSong(
                            id,
                            displayName,
                            artist,
                            contentUri,
                            uri,
                            path,
                            album,
                            duration
                        )
                    songs += song

                    // For debugging, we'll output the image objects we create to logcat.
                }
            }
        }

        return songs
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }



}