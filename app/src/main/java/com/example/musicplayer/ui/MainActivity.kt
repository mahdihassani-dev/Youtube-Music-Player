package com.example.musicplayer.ui


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.databinding.FragmentSongControllerBinding
import com.example.musicplayer.ui.fragments.SongControllerFragment
import com.example.musicplayer.ui.fragments.songs.SongFragment


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