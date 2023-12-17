package com.example.musicplayer.ui.fragments.songs

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.adapters.SongAdapter
import com.example.musicplayer.databinding.FragmentSongBinding
import com.example.musicplayer.interfaces.PlayPauseStateNotifier
import com.example.musicplayer.interfaces.SeekCompletionNotifier
import com.example.musicplayer.interfaces.SongChangeNotifier
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.ui.fragments.SongControllerFragment


class SongFragment : Fragment(), SongAdapter.OnItemClick, SongChangeNotifier,
    PlayPauseStateNotifier, SeekCompletionNotifier {

    private lateinit var binding: FragmentSongBinding
    private val viewModel: SongViewModel by viewModels()
    private lateinit var songList: List<MediaStoreSong>
    private var controllerFragment = SongControllerFragment()

    companion object {
        const val EXTRA_BUNDLE_NAME = "send_bundle_name"
        const val EXTRA_BUNDLE_ARTIST = "send_bundle_artist"
        const val EXTRA_BUNDLE_COVER = "send_bundle_cover"
        const val EXTRA_BUNDLE_ADDED = "checkAddedFragment"
        const val EXTRA_BUNDLE_HASCOVER = "checkCoverIsExisted"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSongBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.backToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.songs.observe(
            viewLifecycleOwner,
            Observer<List<MediaStoreSong>> { songs ->
                if (songs.isNotEmpty()) {
                    showSongsByAdapter(songs)
                    songList = songs
                }
            })

        viewModel.loadSongs()

    }


    private fun showSongsByAdapter(songs: List<MediaStoreSong>) {
        binding.iconFragmentSong.visibility = View.INVISIBLE
        binding.txtFragmentSong.visibility = View.INVISIBLE
        binding.recyclerSongs.visibility = View.VISIBLE
        binding.recyclerSongs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSongs.adapter = SongAdapter(songs, this)

    }

    override fun onItemClickListener(song: MediaStoreSong, pos: Int) {


        val bundle = Bundle()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()

        if (controllerFragment.isAdded) {
            controllerFragment = SongControllerFragment()
            bundle.putBoolean(EXTRA_BUNDLE_ADDED, true)
        } else {
            transaction.setCustomAnimations(R.anim.fragment_slide_up, R.anim.wait_anim)
        }

        val retriver = MediaMetadataRetriever()
        retriver.setDataSource(song.path)
        val cover = retriver.embeddedPicture
        val isExisted : Boolean = cover != null

        bundle.putString(EXTRA_BUNDLE_NAME, song.displayName)
        bundle.putString(EXTRA_BUNDLE_ARTIST, song.artist)
        bundle.putString(EXTRA_BUNDLE_COVER, song.coverArt.toString())
        bundle.putBoolean(EXTRA_BUNDLE_HASCOVER, isExisted)

        controllerFragment.arguments = bundle

        transaction.replace(R.id.container, controllerFragment).commit()


    }


    override fun onCurrentSongChange() {
    }

    override fun onPlayPauseStateChange() {
    }

    override fun onSeekComplete() {
    }


}