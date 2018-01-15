package com.delacrixmorgan.squark.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import com.delacrixmorgan.squark.MainActivity
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.SquarkEngine
import com.delacrixmorgan.squark.common.BaseFragment
import com.delacrixmorgan.squark.model.Currency
import com.delacrixmorgan.squark.network.InterfaceAPI
import com.delacrixmorgan.squark.network.SquarkAPI
import com.delacrixmorgan.squark.shared.Helper
import com.delacrixmorgan.squark.wrapper.APIWrapper
import io.realm.Realm
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class TableFragment : BaseFragment() {

    companion object {
        private val TAG = "TableFragment"

        fun newInstance(): TableFragment {
            return TableFragment()
        }
    }

    private var mRealmResultsCurrency: RealmResults<Currency>? = null
    private var mBaseCurrency: TextView? = null
    private var mQuoteCurrency: TextView? = null
    private var mSwapButton: FloatingActionButton? = null
    private var currencyFragment: CurrencyFragment? = null
    private var mTableLayout: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRealmResultsCurrency = Realm.getDefaultInstance().where(Currency::class.java).findAll()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_table, container, false)

        mBaseCurrency = rootView.findViewById<View>(R.id.baseCurrencyTextView) as TextView
        mQuoteCurrency = rootView.findViewById<View>(R.id.quoteCurrencyTextView) as TextView
        mSwapButton = rootView.findViewById<View>(R.id.swapButton) as FloatingActionButton
        mTableLayout = rootView.findViewById<View>(R.id.tableLayout) as TableLayout

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyFragment = CurrencyFragment()
        SquarkEngine.getInstance().updateTable(activity, mTableLayout)

        if (Helper.getCurrentDate() != activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "01/01/2000")) {
            val call = SquarkAPI.getClient().create(InterfaceAPI::class.java).updateRates("USD")
            call.enqueue(object : Callback<APIWrapper> {
                override fun onResponse(call: Call<APIWrapper>, response: Response<APIWrapper>) {
                    val editor = activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit()
                    editor.putString(Helper.DATE_PREFERENCE, Helper.getCurrentDate())
                    editor.putString(Helper.TIME_PREFERENCE, Helper.getCurrentTime())

                    response.body().updateCurrencyList()

                    val m1 = mRealmResultsCurrency!!.where().equalTo("code", "USD").findFirst()
                    val m2 = mRealmResultsCurrency!!.where().equalTo("code", "MYR").findFirst()

                    editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, mRealmResultsCurrency!!.indexOf(m1))
                    editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, mRealmResultsCurrency!!.indexOf(m2))

                    editor.apply()

                    activity!!.recreate()
                }

                override fun onFailure(call: Call<APIWrapper>, t: Throwable) {
                    if (!Helper.isNetworkConnected(activity) && mRealmResultsCurrency!!.isEmpty()) {
                        AlertDialog.Builder(activity!!, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth)
                                .setTitle("Message")
                                .setMessage("Something wrong with the Internet connection.")
                                .setPositiveButton("Try Again") { dialog, which ->
                                    val refresh = Intent(activity, MainActivity::class.java)
                                    activity!!.finish()
                                    startActivity(refresh)
                                    dialog.dismiss()
                                }
                                .setNegativeButton("Quit") { dialog, which ->
                                    activity!!.finish()
                                    dialog.dismiss()
                                }
                                .show()
                    }
                }
            })
        }

        mSwapButton!!.setOnClickListener {
            val editor = activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit()
            editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(activity, Helper.QUOTE_CURRENCY_PREFERENCE))
            editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(activity, Helper.BASE_CURRENCY_PREFERENCE))
            editor.apply()

            updateCurrency()
        }

        mBaseCurrency!!.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Helper.TYPE_CONVERT, "BASE")
            currencyFragment!!.arguments = bundle

            activity!!.fragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, currencyFragment)
                    .addToBackStack(TableFragment.TAG)
                    .commit()
        }

        mQuoteCurrency!!.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Helper.TYPE_CONVERT, "QUOTE")
            currencyFragment!!.arguments = bundle

            activity!!.fragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, currencyFragment)
                    .addToBackStack(TableFragment.TAG)
                    .commit()
        }

        if (!mRealmResultsCurrency!!.isEmpty()) {
            updateCurrency()
        }
    }

    fun updateCurrency() {
        val baseCurrency = mRealmResultsCurrency!![Helper.getCurrencyPreference(activity, Helper.BASE_CURRENCY_PREFERENCE)]
        val quoteCurrency = mRealmResultsCurrency!![Helper.getCurrencyPreference(activity, Helper.QUOTE_CURRENCY_PREFERENCE)]

        SquarkEngine.getInstance().updateConversionRate(baseCurrency, quoteCurrency)
        SquarkEngine.getInstance().updateTable(activity, mTableLayout)

        mBaseCurrency!!.text = baseCurrency.code
        mQuoteCurrency!!.text = quoteCurrency.code
    }
}
