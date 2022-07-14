package com.delacrixmorgan.squark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.FragmentLaunchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchFragment : Fragment(R.layout.fragment_launch) {
    private var _binding: FragmentLaunchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LaunchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    LaunchUiState.Start -> Unit
                    LaunchUiState.Success -> launchCurrencyNavigationFragment()
                    is LaunchUiState.Failure -> showError(it.exception)
                }
            }
        }
    }

    private fun launchCurrencyNavigationFragment() {
        findNavController().navigate(
            LaunchFragmentDirections.actionLaunchFragmentToCurrencyNavigationFragment()
        )
    }

    private fun showError(exception: Exception) {
        Snackbar.make(
            binding.rootView, exception.message ?: "Oops, something went wrong..", Snackbar.LENGTH_SHORT
        ).show()
    }
}