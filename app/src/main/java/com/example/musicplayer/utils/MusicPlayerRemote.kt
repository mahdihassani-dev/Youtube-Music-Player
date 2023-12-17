package com.example.musicplayer.utils

import android.app.Activity
import android.content.*
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.services.SongService
import java.util.*

object MusicPlayerRemote {

    var songService: SongService? = null
    private val mConnectionMap = WeakHashMap<Context, ServiceBinder>()

    fun sendAllSong(songList: MutableList<MediaStoreSong>, position: Int) {
        if (songService != null) {
            songService?.getAllSongs(songList, position)
        }
    }

    fun playPause() {
        if (songService != null)
            songService?.playPause()
    }

    fun playNextSong() {
        if (songService != null) {
            songService?.playNext()
        }
    }

    fun playPreviousSong() {
        if (songService != null) {
            songService?.playPrevious()
        }
    }

    fun seekTo(currentPosition: Int) {
        songService?.let {
            songService?.seekTo(currentPosition)
        }
    }

    fun isPlaying(): Boolean {
        songService?.let {
            return it.isPlaying()
        }
        return false
    }

    val songDurationMillis: Int
        get() = if (songService != null) {
            songService!!.getSongDurationMillis()
        } else -1

    val currentSongPositionMillis: Int
        get() = if (songService != null) {
            songService!!.getCurrentPosition()
        } else {
            0
        }

    fun bindToService(context: Context, callback: ServiceConnection): ServiceToken? {

        var realActivity: Activity? = (context as Activity).parent
        if (realActivity == null) {
            realActivity = context
        }

        val contextWrapper = ContextWrapper(realActivity)
        val intent = Intent(contextWrapper, SongService::class.java)
        try {
            contextWrapper.startService(intent)
        } catch (ignored: IllegalStateException) {
            ContextCompat.startForegroundService(context, intent)
        }
        val binder = ServiceBinder(callback)

        if (contextWrapper.bindService(
                Intent().setClass(contextWrapper, SongService::class.java),
                binder,
                Context.BIND_AUTO_CREATE
            )
        ) {
            mConnectionMap[contextWrapper] = binder
            return ServiceToken(contextWrapper)
        }
        return null
    }

    fun unbindFromService(token: ServiceToken?) {
        if (token == null) {
            return
        }
        val mContextWrapper = token.mWrappedContext
        val mBinder = mConnectionMap.remove(mContextWrapper) ?: return
        mContextWrapper.unbindService(mBinder)
        if (mConnectionMap.isEmpty()) {
            songService = null
        }
    }

    class ServiceBinder internal constructor(private val mCallback: ServiceConnection?) :
        ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SongService.LocalBinder
            songService = binder.getService()
            mCallback?.onServiceConnected(className, service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mCallback?.onServiceDisconnected(className)
            songService = null
        }
    }

    class ServiceToken internal constructor(internal var mWrappedContext: ContextWrapper)
}