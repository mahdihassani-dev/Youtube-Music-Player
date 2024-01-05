package com.example.musicplayer.ui.fragments.songs

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.databinding.FragmentSongBinding
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.ui.fragments.SongControllerFragment
import com.example.musicplayer.utils.Constants.PREF_NAME
import com.example.musicplayer.utils.MusicPlayerRemote
import com.example.musicplayer.utils.PlayerHelper


class SongFragment : Fragment(), SongAdapter.OnItemClick {

    private lateinit var binding: FragmentSongBinding
    private val viewModel: SongViewModel by viewModels()
    private var controllerFragment = SongControllerFragment()
    private var serviceToken: MusicPlayerRemote.ServiceToken? = null
    private lateinit var sharedPreferences: SharedPreferences

    private val currentSong get() = PlayerHelper.getCurrentSong(sharedPreferences)

    companion object {
        const val EXTRA_BUNDLE_NAME = "send_bundle_name"
        const val EXTRA_BUNDLE_ARTIST = "send_bundle_artist"
        const val EXTRA_BUNDLE_COVER = "send_bundle_cover"
        const val EXTRA_BUNDLE_ADDED = "checkAddedFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSongBinding.inflate(layoutInflater)


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.backToHomeBtn.setOnClickListener {
            backToHome()
        }


        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        viewModel.loadSongs()

        serviceToken =
            MusicPlayerRemote.bindToService(requireContext(), object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {

                    currentSong?.let { reloadControllerFragment(it, true) }

                    if (MusicPlayerRemote.songService?.mediaPlayer == null && currentSong != null) {
//                        MusicPlayerRemote.songService?.initMediaPlayer(currentSong!!.path)
                        viewModel.songs.observe(
                            viewLifecycleOwner,
                            Observer<List<MediaStoreSong>> { songs ->
                                if (songs.isNotEmpty()) {
                                    MusicPlayerRemote.sendAllSong(
                                        songs as MutableList<MediaStoreSong>,
                                        -1
                                    )
                                }
                            })


                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                }
            })


        viewModel.songs.observe(
            viewLifecycleOwner,
            Observer<List<MediaStoreSong>> { songs ->
                if (songs.isNotEmpty()) {
                    binding.recyclerSongs.visibility = View.VISIBLE
                    binding.emptyScreen.visibility = View.INVISIBLE
                    showSongsByAdapter(songs)
                } else {
                    binding.recyclerSongs.visibility = View.INVISIBLE
                    binding.emptyScreen.visibility = View.VISIBLE
                }
            })


    }


    private fun showSongsByAdapter(songs: List<MediaStoreSong>) {
        binding.iconFragmentSong.visibility = View.INVISIBLE
        binding.txtFragmentSong.visibility = View.INVISIBLE
        binding.recyclerSongs.visibility = View.VISIBLE
        binding.recyclerSongs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSongs.adapter = SongAdapter(songs, this)

    }

    override fun onItemClickListener(song: MediaStoreSong, pos: Int) {

        reloadControllerFragment(song, false)

    }

    private fun reloadControllerFragment(song: MediaStoreSong, isFromService: Boolean) {


        val bundle = Bundle()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        if (controllerFragment.isAdded || isFromService) {
            controllerFragment = SongControllerFragment()
            bundle.putBoolean(EXTRA_BUNDLE_ADDED, true)
        } else {
            transaction.setCustomAnimations(R.anim.fragment_slide_up, R.anim.wait_anim)
        }

        if (song.id.toInt() != -1) {


            bundle.putString(EXTRA_BUNDLE_NAME, song.displayName)
            bundle.putString(EXTRA_BUNDLE_ARTIST, song.artist)
            bundle.putString(EXTRA_BUNDLE_COVER, song.coverArt.toString())

        }

        controllerFragment.arguments = bundle

        transaction.replace(R.id.container, controllerFragment).commit()

    }

    fun backToHome() {
        findNavController().popBackStack()
    }


}