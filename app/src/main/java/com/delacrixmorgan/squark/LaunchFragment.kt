package com.delacrixmorgan.squark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.delacrixmorgan.squark.deprecrated.common.BaseFragment
import com.delacrixmorgan.squark.deprecrated.model.Currency
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class LaunchFragment : BaseFragment() {

    companion object {
        private val TAG = "LaunchFragment"

        fun newInstance(): LaunchFragment {
            return LaunchFragment()
        }
    }

    private lateinit var mRealmResultsCurrency: RealmResults<Currency>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SquarkEngine.newInstance()
        mRealmResultsCurrency = Realm.getDefaultInstance().where(Currency::class.java).findAll()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        currencyFragment = CurrencyFragment()
//        SquarkEngine.instance.updateTable(activity, mTableLayout)
//
//        if (Helper.getCurrentDate() != activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "01/01/2000")) {
//            val call = SquarkAPI.getClient().create(InterfaceAPI::class.java).updateRates("USD")
//            call.enqueue(object : Callback<APIWrapper> {
//                override fun onResponse(call: Call<APIWrapper>, response: Response<APIWrapper>) {
//                    val editor = activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit()
//                    editor.putString(Helper.DATE_PREFERENCE, Helper.getCurrentDate())
//                    editor.putString(Helper.TIME_PREFERENCE, Helper.getCurrentTime())
//
//                    response.body().updateCurrencyList()
//
//                    val m1 = mRealmResultsCurrency!!.where().equalTo("code", "USD").findFirst()
//                    val m2 = mRealmResultsCurrency!!.where().equalTo("code", "MYR").findFirst()
//
//                    editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, mRealmResultsCurrency!!.indexOf(m1))
//                    editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, mRealmResultsCurrency!!.indexOf(m2))
//
//                    editor.apply()
//
//                    activity!!.recreate()
//                }
//
//                override fun onFailure(call: Call<APIWrapper>, t: Throwable) {
//                    if (!Helper.isNetworkConnected(activity) && mRealmResultsCurrency!!.isEmpty()) {
//                        AlertDialog.Builder(activity!!, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth)
//                                .setTitle("Message")
//                                .setMessage("Something wrong with the Internet connection.")
//                                .setPositiveButton("Try Again") { dialog, which ->
//                                    val refresh = Intent(activity, MainActivity::class.java)
//                                    activity!!.finish()
//                                    startActivity(refresh)
//                                    dialog.dismiss()
//                                }
//                                .setNegativeButton("Quit") { dialog, which ->
//                                    activity!!.finish()
//                                    dialog.dismiss()
//                                }
//                                .show()
//                    }
//                }
//            })
//        }
//
//        mSwapButton!!.setOnClickListener {
//            val editor = activity!!.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit()
//            editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(activity, Helper.QUOTE_CURRENCY_PREFERENCE))
//            editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(activity, Helper.BASE_CURRENCY_PREFERENCE))
//            editor.apply()
//
//            updateCurrency()
//        }
//
//        mBaseCurrency!!.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(Helper.TYPE_CONVERT, "BASE")
//            currencyFragment!!.arguments = bundle
//
//            activity!!.fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.mainContainer, currencyFragment)
//                    .addToBackStack(TAG)
//                    .commit()
//        }
//
//        mQuoteCurrency!!.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(Helper.TYPE_CONVERT, "QUOTE")
//            currencyFragment!!.arguments = bundle
//
//            activity!!.fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.mainContainer, currencyFragment)
//                    .addToBackStack(TAG)
//                    .commit()
//        }
//
//        if (!mRealmResultsCurrency!!.isEmpty()) {
//            updateCurrency()
//        }
//    }
//
//    fun updateCurrency() {
//        val baseCurrency = mRealmResultsCurrency!![Helper.getCurrencyPreference(activity, Helper.BASE_CURRENCY_PREFERENCE)]
//        val quoteCurrency = mRealmResultsCurrency!![Helper.getCurrencyPreference(activity, Helper.QUOTE_CURRENCY_PREFERENCE)]
//
//        SquarkEngine.instance.updateConversionRate(baseCurrency, quoteCurrency)
//        SquarkEngine.instance.updateTable(activity, mTableLayout)
//
//        mBaseCurrency!!.text = baseCurrency.code
//        mQuoteCurrency!!.text = quoteCurrency.code
//    }
}
