package com.example.musicplayer.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class MediaStoreSong(
    val id: Long,
    val displayName: String?,
    val artist: String?,
    val contentUri: Uri?,
    val coverArt: Uri?,
    val path: String,
    val albumName: String?,
    val duration: Long,
) : Parcelable
