package com.delacrixmorgan.squark.ui.preference.currencyunit

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.Keys
import com.delacrixmorgan.squark.common.compatColor
import com.delacrixmorgan.squark.common.performHapticContextClick
import com.delacrixmorgan.squark.databinding.FragmentCurrencyUnitBinding
import com.delacrixmorgan.squark.model.currency.Currency
import com.delacrixmorgan.squark.model.currency.toCurrency
import com.delacrixmorgan.squark.ui.currency.CurrencyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder

@AndroidEntryPoint
class CurrencyUnitFragment : Fragment(R.layout.fragment_currency_unit), CurrencyUnitAdapter.Listener,
    MenuItem.OnActionExpandListener {
    companion object {
        fun create(currency: Currency) = CurrencyUnitFragment().apply {
            arguments = bundleOf(Keys.CurrencyUnit.Currency.name to currency.toString())
        }
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCurrencyUnitBinding? = null

    private val viewModel: CurrencyUnitViewModel by viewModels()
    private val adapter: CurrencyUnitAdapter by lazy { CurrencyUnitAdapter(listener = this) }

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.selectedCurrency = requireNotNull(arguments?.getString(Keys.CurrencyUnit.Currency.name)).toCurrency()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = ""
        }

        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemReselectedListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setColorSchemeColors(
            R.color.colorAccent.compatColor(context),
            R.color.colorPrimary.compatColor(context)
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.performHapticContextClick()
            viewModel.fetchCurrencies()
            Snackbar.make(binding.mainContainer, getString(R.string.fragment_country_list_title_everything_already_updated), Snackbar.LENGTH_SHORT).show()
        }

        FastScrollerBuilder(binding.recyclerView)
            .build()

        lifecycleScope.launch {
            viewModel.onStart()
            viewModel.uiState.collect {
                when (it) {
                    CurrencyUnitUiState.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is CurrencyUnitUiState.Success -> {
                        updateCurrencyUnitList(it.currencies)
                    }
                    is CurrencyUnitUiState.OnCurrencyFiltered -> {
                        updateCurrencyUnitList(it.filteredCurrencies, isSearchMode = true)
                    }
                    is CurrencyUnitUiState.Failure -> {
                        showErrorMessage(it.exception)
                    }
                }
            }
        }
    }

    private fun updateCurrencyUnitList(currencies: List<Currency>, isSearchMode: Boolean = false) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.emptyStateViewGroup.isVisible = currencies.isEmpty()
        adapter.updateDataSet(viewModel.selectedCurrency, currencies, isSearchMode)
    }

    private fun showErrorMessage(exception: Exception) {
        binding.swipeRefreshLayout.isRefreshing = false
        Snackbar.make(binding.mainContainer, exception.message ?: getString(R.string.error_api_countries), Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        searchMenuItem = menu.findItem(R.id.actionSearch)
        searchMenuItem?.setOnActionExpandListener(this)

        searchView = searchMenuItem?.actionView as? SearchView
        searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterCurrencies(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterCurrencies(newText)
                return true
            }
        })
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = requireActivity()

        return when (item.itemId) {
            android.R.id.home -> {
                if (activity.supportFragmentManager.backStackEntryCount > 1) {
                    activity.supportFragmentManager.popBackStack()
                } else {
                    activity.finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCurrencySelected(currency: Currency) {
        val activity = requireActivity()
        val intent = activity.intent

        intent.putExtra(CurrencyFragment.EXTRA_CURRENCY, currency.code)

        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }
}