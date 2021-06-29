package com.delacrixmorgan.squark.ui.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchPlayStore
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.databinding.FragmentSupportListBinding

class SupportListFragment : Fragment(R.layout.fragment_support_list) {
    companion object {
        private const val KINGS_CUP_PACKAGE_NAME = "com.delacrixmorgan.kingscup"
        private const val MAMIKA_PACKAGE_NAME = "com.delacrixmorgan.mamika"

        fun create() = SupportListFragment()
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentSupportListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val packageName = view.context.packageName

        binding.starImageView.setOnClickListener {
            binding.starImageView.performHapticContextClick()
            binding.personImageView.setImageResource(R.drawable.ic_human_happy)
            binding.starImageView.setColorFilter(ContextCompat.getColor(view.context, R.color.colorAccent))

            view.context.launchPlayStore(packageName)
        }

        binding.rateButton.setOnClickListener {
            binding.rateButton.performHapticContextClick()
            binding.personImageView.setImageResource(R.drawable.ic_human_happy)
            view.context.launchPlayStore(packageName)
        }

        binding.kingscupViewGroup.setOnClickListener {
            binding.kingscupViewGroup.performHapticContextClick()
            view.context.launchPlayStore(KINGS_CUP_PACKAGE_NAME)
        }

        binding.mamikaViewGroup.setOnClickListener {
            binding.kingscupViewGroup.performHapticContextClick()
            view.context.launchPlayStore(MAMIKA_PACKAGE_NAME)
        }
    }
}