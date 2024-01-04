package com.example.musicplayer.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentSongControllerBinding
import com.example.musicplayer.interfaces.PlayPauseStateNotifier
import com.example.musicplayer.interfaces.SeekCompletionNotifier
import com.example.musicplayer.interfaces.SongChangeNotifier
import com.example.musicplayer.model.MediaStoreSong
import com.example.musicplayer.services.SongService
import com.example.musicplayer.ui.fragments.songs.SongFragment
import com.example.musicplayer.utils.Constants
import com.example.musicplayer.utils.Constants.PREF_NAME
import com.example.musicplayer.utils.MusicPlayerRemote
import com.example.musicplayer.utils.PlayerHelper


class SongControllerFragment : Fragment(), SongChangeNotifier, PlayPauseStateNotifier,
    SeekCompletionNotifier {

    private lateinit var binding: FragmentSongControllerBinding

    private lateinit var sharedPreferences: SharedPreferences

    private val playerService: SongService?
        get() = MusicPlayerRemote.songService
    private val currentSong: MediaStoreSong?
        get() = PlayerHelper.getCurrentSong(sharedPreferences)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        if (playerService != null) {
            playerService!!.setSongChangeCallback(this)
            playerService!!.setPlayPauseStateCallback(this)
            playerService!!.setSeekCompleteNotifierCallback(this)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSongControllerBinding.inflate(layoutInflater)

        if (requireArguments().getBoolean(SongFragment.EXTRA_BUNDLE_ADDED) != null && requireArguments().getBoolean(
                SongFragment.EXTRA_BUNDLE_ADDED
            )
        ) {

            binding.motionLayout.jumpToState(binding.motionLayout.endState)

        }

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (currentSong!!.id.toInt() == -1) {
            binding.motionLayout.visibility = View.GONE
        } else {
            binding.motionLayout.visibility = View.VISIBLE


            binding.txtNameController.text =
                requireArguments().getString(SongFragment.EXTRA_BUNDLE_NAME)
            binding.txtArtistController.text =
                requireArguments().getString(SongFragment.EXTRA_BUNDLE_ARTIST)
            binding.itemTxtTitle.text =
                requireArguments().getString(SongFragment.EXTRA_BUNDLE_NAME)
            binding.itemTxtArtist.text =
                requireArguments().getString(SongFragment.EXTRA_BUNDLE_ARTIST)
            Glide.with(requireContext())
                .load(requireArguments().getString(SongFragment.EXTRA_BUNDLE_COVER))
                .placeholder(R.drawable.place_holder)
                .into(binding.imgCoverController)


            binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {

                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {


                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                    if (currentId == motionLayout?.startState) {


                        binding.imgCoverController.setImageURI(
                            requireArguments().getString(
                                SongFragment.EXTRA_BUNDLE_COVER
                            )?.toUri()
                        )


                    }

                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }
            })

            binding.arrowDownPage.setOnClickListener {
                binding.motionLayout.transitionToEnd()
            }


        }

    }


    override fun onCurrentSongChange() {
    }

    override fun onPlayPauseStateChange() {
    }

    override fun onSeekComplete() {

    }


}