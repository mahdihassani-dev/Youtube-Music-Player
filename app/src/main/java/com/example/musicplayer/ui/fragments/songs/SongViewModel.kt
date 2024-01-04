package com.example.musicplayer.ui.fragments.songs

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.IntentSender
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.Repository
import com.example.musicplayer.model.MediaStoreSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel(application: Application) : AndroidViewModel(application) {


    private val _songs = MutableLiveData<List<MediaStoreSong>>()

    val songs: LiveData<List<MediaStoreSong>> get() = _songs

    private var contentObserver: ContentObserver? = null

    private var pendingDeleteSong: MediaStoreSong? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete

    @RequiresApi(Build.VERSION_CODES.Q)
    fun loadSongs() {
        viewModelScope.launch {
            val songList = Repository(getApplication()).querySongs()
            _songs.postValue(songList)

            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadSongs()
                }
            }
        }
    }

    fun deleteSong(image: MediaStoreSong) {
        viewModelScope.launch {
            performDeleteImage(image)
        }
    }

    fun deletePendingImage() {
        pendingDeleteSong?.let { song ->
            pendingDeleteSong = null
            deleteSong(song)
        }
    }


    private suspend fun performDeleteImage(song: MediaStoreSong) {
        withContext(Dispatchers.IO) {
            try {
                song.contentUri?.let {
                    getApplication<Application>().contentResolver.delete(
                        it,
                        "${MediaStore.Images.Media._ID} = ?",
                        arrayOf(song.id.toString())
                    )
                }
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException

                    // Signal to the Activity that it needs to request permission and
                    // try the delete again if it succeeds.
                    pendingDeleteSong = song
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
        }
    }

    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

    private fun ContentResolver.registerObserver(
        uri: Uri,
        observer: (selfChange: Boolean) -> Unit
    ): ContentObserver {
        val contentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                observer(selfChange)
            }
        }
        registerContentObserver(uri, true, contentObserver)
        return contentObserver
    }

}