package com.delacrixmorgan.squark.ui.preference.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.launchWebsite
import kotlinx.android.synthetic.main.fragment_credit_list.*

class CreditListFragment : DialogFragment() {
    companion object {
        fun create() = CreditListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_credit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spartanImageView.setOnClickListener {
            launchWebsite("https://github.com/theleagueof/league-spartan")
        }

        currencyLayerImageView.setOnClickListener {
            launchWebsite("https://github.com/apilayer/currencylayer-API")
        }

        fastScrollImageView.setOnClickListener {
            launchWebsite("https://github.com/zhanghai/AndroidFastScroll")
        }

        dateTimeWhartonImageView.setOnClickListener {
            launchWebsite("https://github.com/JakeWharton/ThreeTenABP")
        }

        doneButton.setOnClickListener {
            activity?.finish()
        }
    }
}