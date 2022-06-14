package com.delacrixmorgan.squark.ui.preference.country

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.*
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.dao.CountryDataDao
import com.delacrixmorgan.squark.databinding.FragmentCountryBinding
import com.delacrixmorgan.squark.models.Country
import com.delacrixmorgan.squark.models.Currency
import com.delacrixmorgan.squark.services.network.Result
import com.delacrixmorgan.squark.ui.currency.CurrencyFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CountryFragment : Fragment(R.layout.fragment_country), CountryRecyclerViewAdapter.Listener,
    MenuItem.OnActionExpandListener {
    companion object {
        fun create(countryCode: String? = null) = CountryFragment().apply {
            arguments = bundleOf(Keys.Country.Code.name to countryCode)
        }
    }

    @Inject
    lateinit var viewModel: CountryViewModel

    @Inject
    lateinit var countryDatabaseDao: CountryDataDao

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    private lateinit var countryCode: String

    private val countryAdapter: CountryRecyclerViewAdapter by lazy {
        CountryRecyclerViewAdapter(listener = this)
    }

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentCountryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        countryCode = requireNotNull(arguments?.getString(Keys.Country.Code.name))
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

        binding.countryRecyclerView.adapter = countryAdapter

        FastScrollerBuilder(binding.countryRecyclerView)
            .build()

        binding.swipeRefreshLayout.setColorSchemeColors(
            R.color.colorAccent.compatColor(context),
            R.color.colorPrimary.compatColor(context)
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.performHapticContextClick()
            checkIsDataUpdated()
        }

        updateDataSet()
    }

    private fun checkIsDataUpdated() {
        val lastUpdatedDateTime = SharedPreferenceHelper.lastUpdatedDate.time
        val currentDateTime = Date().time

        if (TimeUnit.MILLISECONDS.toDays(currentDateTime - lastUpdatedDateTime) >= 1) {
            updateCurrencyRates()
        } else {
            Snackbar.make(
                binding.mainContainer,
                getString(R.string.fragment_country_list_title_everything_already_updated),
                Snackbar.LENGTH_SHORT
            ).show()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateCurrencyRates() {
        lifecycleScope.launch {
            when (val result = viewModel.fetchCurrencies()) {
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    updateCurrencies(result.value.currencies)
                }
                is Result.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Snackbar.make(
                        binding.mainContainer,
                        getString(R.string.error_api_countries),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateCurrencies(currencies: List<Currency>) {
        CoroutineScope(Dispatchers.IO).launch {
            countryDatabaseDao.getCountries().collect { countries ->
                countries.forEach { country ->
                    val updateCurrency = currencies.find {
                        it.code.contains(country.code)
                    }

                    updateCurrency?.let {
                        country.rate = it.rate
                        countryDatabaseDao.updateCountry(country)
                    }
                }
                countries.let {
                    CountryDataController.updateDataSet(it)
                    SharedPreferenceHelper.lastUpdatedDate = Date()
                }

                if (isVisible) {
                    Snackbar.make(
                        binding.mainContainer,
                        getString(R.string.fragment_country_list_title_updated),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                coroutineContext.job.cancel()
            }
        }
    }

    private fun updateDataSet(searchText: String? = null, searchMode: Boolean = false) {
        val filterCountries = CountryDataController.getFilteredCountries(
            searchText
        ) as MutableList<Country>
        val selectedCountry = filterCountries.firstOrNull { it.code == this.countryCode }

        selectedCountry?.let { country ->
            filterCountries.remove(country)
            filterCountries.sortBy { it.code }
            filterCountries.add(0, country)
        }

        countryAdapter.updateDataSet(filterCountries, searchMode)

        if (isVisible) {
            binding.emptyStateViewGroup.isVisible = filterCountries.isEmpty()
        }
    }

    override fun onCountrySelected(country: Country) {
        val activity = requireActivity()
        val intent = activity.intent

        intent.putExtra(CurrencyFragment.EXTRA_COUNTRY_CODE, country.code)

        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
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
                updateDataSet(query, true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                updateDataSet(newText, true)
                return true
            }
        })
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        searchView?.setQuery("", false)
        updateDataSet()
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
}