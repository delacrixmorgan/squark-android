package com.delacrixmorgan.squark.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;
import com.delacrixmorgan.squark.model.Currency;
import com.delacrixmorgan.squark.network.InterfaceAPI;
import com.delacrixmorgan.squark.network.SquarkAPI;
import com.delacrixmorgan.squark.shared.Helper;
import com.delacrixmorgan.squark.wrapper.APIWrapper;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class TableFragment extends Fragment {
    private static String TAG = "TableFragment";
    private RealmResults<Currency> mRealmResultsCurrency;
    private TextView mBaseCurrency, mQuoteCurrency;
    private FloatingActionButton mSwapButton;
    private CurrencyFragment currencyFragment;
    private TableLayout mTableLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        mRealmResultsCurrency = Realm.getDefaultInstance().where(Currency.class).findAll();

        mBaseCurrency = (TextView) rootView.findViewById(R.id.fragment_table_base_currency);
        mQuoteCurrency = (TextView) rootView.findViewById(R.id.fragment_table_quote_currency);
        mSwapButton = (FloatingActionButton) rootView.findViewById(R.id.fragment_table_swap_button);
        mTableLayout = (TableLayout) rootView.findViewById(R.id.fragment_table_layout);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currencyFragment = new CurrencyFragment();
        SquarkEngine.getInstance().updateTable(getActivity(), mTableLayout);

        if (!Helper.getCurrentDate().equals(getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "01/01/2000"))) {
            Call<APIWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
            call.enqueue(new Callback<APIWrapper>() {
                @Override
                public void onResponse(Call<APIWrapper> call, Response<APIWrapper> response) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                    editor.putString(Helper.DATE_PREFERENCE, Helper.getCurrentDate());
                    editor.putString(Helper.TIME_PREFERENCE, Helper.getCurrentTime());
                    editor.apply();

                    response.body().updateCurrencyList();
                }

                @Override
                public void onFailure(Call<APIWrapper> call, Throwable t) {
                    Log.e(TAG, "onFailure (URL) : " + call.request().url());
                    Log.e(TAG, "onFailure (Message) : " + t.toString());
                }
            });
        }

        mSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(getActivity(), Helper.QUOTE_CURRENCY_PREFERENCE));
                editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, Helper.getCurrencyPreference(getActivity(), Helper.BASE_CURRENCY_PREFERENCE));
                editor.apply();

                updateCurrency();
            }
        });

        mBaseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Helper.TYPE_CONVERT, "BASE");
                currencyFragment.setArguments(bundle);

                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_vg_fragment, currencyFragment)
                        .addToBackStack(TableFragment.TAG)
                        .commit();
            }
        });

        mQuoteCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Helper.TYPE_CONVERT, "QUOTE");
                currencyFragment.setArguments(bundle);

                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_vg_fragment, currencyFragment)
                        .addToBackStack(TableFragment.TAG)
                        .commit();
            }
        });

        if (!mRealmResultsCurrency.isEmpty()) {
            updateCurrency();
        }
    }

    public void updateCurrency() {
        Currency baseCurrency = mRealmResultsCurrency.get(Helper.getCurrencyPreference(getActivity(), Helper.BASE_CURRENCY_PREFERENCE));
        Currency quoteCurrency = mRealmResultsCurrency.get(Helper.getCurrencyPreference(getActivity(), Helper.QUOTE_CURRENCY_PREFERENCE));

        SquarkEngine.getInstance().updateConversionRate(baseCurrency, quoteCurrency);
        SquarkEngine.getInstance().updateTable(getActivity(), mTableLayout);

        mBaseCurrency.setText(baseCurrency.getCode());
        mQuoteCurrency.setText(quoteCurrency.getCode());
    }
}
