package com.example.musicplayer.ui.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.utils.ModalBottomSheet
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.ui.fragments.songs.SongViewModel
import com.example.musicplayer.utils.Constants.PREF_NAME
import com.example.musicplayer.utils.MusicPlayerRemote
import com.example.musicplayer.utils.PlayerHelper

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    companion object {
        var isFirstPermission = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet = ModalBottomSheet()

        binding.songsChoice.setOnClickListener(View.OnClickListener {

            if (isFirstPermission) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)

                } else {
                    findNavController().navigate(R.id.action_homeFragment_to_songFragment)
                }
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_songFragment)
            }
        })


    }
}