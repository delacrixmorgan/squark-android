package com.delacrixmorgan.squark.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;
import com.delacrixmorgan.squark.listener.OnSwipeTouchListener;
import com.delacrixmorgan.squark.model.Currency;
import com.delacrixmorgan.squark.network.InterfaceAPI;
import com.delacrixmorgan.squark.network.SquarkAPI;
import com.delacrixmorgan.squark.shared.Helper;
import com.delacrixmorgan.squark.wrapper.APIWrapper;

import java.util.ArrayList;

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

    private Realm mRealm;
    private RealmResults<Currency> mRealmResultsCurrency;

    private ArrayList<TextView> mQuantifiers, mResult;
    private TextView mBaseCurrency, mQuoteCurrency;
    private CurrencyFragment currencyFragment = new CurrencyFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        mRealm = Realm.getDefaultInstance();
        mRealmResultsCurrency = mRealm.where(Currency.class).findAll();

        mBaseCurrency = (TextView) rootView.findViewById(R.id.fragment_table_base_currency);
        mQuoteCurrency = (TextView) rootView.findViewById(R.id.fragment_table_quote_currency);

        mQuantifiers = new ArrayList<>();
        mResult = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String rowQuantifierID = "tv_row_" + Helper.numberNames[i] + "_quantifier";
            String rowResultID = "tv_row_" + Helper.numberNames[i] + "_result";

            mQuantifiers.add((TextView) rootView.findViewById(getResources().getIdentifier(rowQuantifierID, "id", getActivity().getPackageName())));
            mResult.add((TextView) rootView.findViewById(getResources().getIdentifier(rowResultID, "id", getActivity().getPackageName())));
        }

        rootView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                SquarkEngine.getInstance().swipeLeft();
                SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
            }

            public void onSwipeRight() {
                SquarkEngine.getInstance().swipeRight();
                SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
            }

            public void onSwipeTop() {
//                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
//                Toast.makeText(getActivity(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<APIWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
        call.enqueue(new Callback<APIWrapper>() {
            @Override
            public void onResponse(Call<APIWrapper> call, Response<APIWrapper> response) {
                Log.i(TAG, "onResponse (URL)  : " + call.request().url());
                Log.i(TAG, "onResponse (Base) : " + response.body().getBase());
                Log.i(TAG, "onResponse (Date) : " + response.body().getDate());

                response.body().updateCurrencyList();
            }

            @Override
            public void onFailure(Call<APIWrapper> call, Throwable t) {
                Log.e(TAG, "onFailure (URL) : " + call.request().url());
                Log.e(TAG, "onFailure (Message) : " + t.toString());
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
        Currency baseCurrency = mRealmResultsCurrency.get(getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getInt(Helper.BASE_CURRENCY_PREFERENCE, 29));
        Currency quoteCurrency = mRealmResultsCurrency.get(getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getInt(Helper.QUOTE_CURRENCY_PREFERENCE, 18));

        mBaseCurrency.setText(baseCurrency.getCode());
        mQuoteCurrency.setText(quoteCurrency.getCode());

        SquarkEngine.getInstance().updateConversionRate(baseCurrency, quoteCurrency);
        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
    }
}
