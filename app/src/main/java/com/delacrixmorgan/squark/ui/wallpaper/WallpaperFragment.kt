package com.delacrixmorgan.squark.ui.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.FragmentWallpaperBinding

class WallpaperFragment : Fragment(R.layout.fragment_wallpaper) {
    companion object {
        fun create() = WallpaperFragment()
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentWallpaperBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wallpaperImageView.load("https://images.unsplash.com/photo-1576675466969-38eeae4b41f6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80")

        binding.changeWallpaperButton.setOnClickListener {

        }

        binding.setWallpaperButton.setOnClickListener {

        }
    }
}