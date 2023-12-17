package com.example.musicplayer.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.utils.ModalBottomSheet
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentHomeBinding

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