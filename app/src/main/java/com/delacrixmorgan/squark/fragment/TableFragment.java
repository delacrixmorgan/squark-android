package com.delacrixmorgan.squark.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.delacrixmorgan.squark.MainActivity;
import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;
import com.delacrixmorgan.squark.common.BaseFragment;
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

public class TableFragment extends BaseFragment {
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

                    response.body().updateCurrencyList();

                    Currency m1 = mRealmResultsCurrency.where().equalTo("code", "USD").findFirst();
                    Currency m2 = mRealmResultsCurrency.where().equalTo("code", "MYR").findFirst();

                    editor.putInt(Helper.BASE_CURRENCY_PREFERENCE, mRealmResultsCurrency.indexOf(m1));
                    editor.putInt(Helper.QUOTE_CURRENCY_PREFERENCE, mRealmResultsCurrency.indexOf(m2));

                    editor.apply();

                    getActivity().recreate();
                }

                @Override
                public void onFailure(Call<APIWrapper> call, Throwable t) {
                    if (!Helper.isNetworkConnected(getActivity()) && mRealmResultsCurrency.isEmpty()) {
                        Log.e(TAG, "onFailure (URL) : " + call.request().url());
                        Log.e(TAG, "onFailure (Message) : " + t.toString());

                        new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth)
                                .setTitle("Message")
                                .setMessage("Something wrong with the Internet connection.")
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent refresh = new Intent(getActivity(), MainActivity.class);
                                        getActivity().finish();
                                        startActivity(refresh);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
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
                        .replace(R.id.mainContainer, currencyFragment)
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
                        .replace(R.id.mainContainer, currencyFragment)
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
