package com.example.musicplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.databinding.FragmentSongControllerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var motionBinding: FragmentSongControllerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        motionBinding = FragmentSongControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }





}