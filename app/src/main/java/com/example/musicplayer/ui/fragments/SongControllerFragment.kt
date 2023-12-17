package com.example.musicplayer.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentSongControllerBinding
import com.example.musicplayer.ui.MainActivity
import com.example.musicplayer.ui.fragments.songs.SongFragment


class SongControllerFragment : Fragment() {

    private lateinit var binding: FragmentSongControllerBinding

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

                    if (requireArguments().getBoolean(SongFragment.EXTRA_BUNDLE_HASCOVER)) {

                        binding.imgCoverController.setImageURI(
                            requireArguments().getString(
                                SongFragment.EXTRA_BUNDLE_COVER
                            )?.toUri()
                        )
                    } else {
                        binding.imgCoverController.setImageResource(R.drawable.place_holder)
                    }

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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (binding.motionLayout.currentState == binding.motionLayout.startState) {
                        binding.motionLayout.transitionToEnd()
                    } else {
                        if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }


                }
            }
            )

    }


}