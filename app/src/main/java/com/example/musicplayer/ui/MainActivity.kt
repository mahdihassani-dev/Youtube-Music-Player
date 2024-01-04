package com.example.musicplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.databinding.FragmentSongControllerBinding
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.ui.fragments.SongControllerFragment
import com.example.musicplayer.ui.fragments.songs.SongFragment
import com.example.musicplayer.ui.fragments.songs.SongViewModel
import com.example.musicplayer.utils.Constants.PREF_NAME
import com.example.musicplayer.utils.MusicPlayerRemote
import com.example.musicplayer.utils.PlayerHelper


private const val TAG = "MainActivityDebug";

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var motionBinding: FragmentSongControllerBinding


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        motionBinding = FragmentSongControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}