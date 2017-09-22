package com.delacrixmorgan.squark.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private ArrayList<TextView> mQuantifiers, mResult;
    private TextView mBaseCurrency, mQuoteCurrency;
    private CurrencyFragment currencyFragment;
    private Boolean mTableExpanded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        mRealmResultsCurrency = Realm.getDefaultInstance().where(Currency.class).findAll();

        mBaseCurrency = (TextView) rootView.findViewById(R.id.fragment_table_base_currency);
        mQuoteCurrency = (TextView) rootView.findViewById(R.id.fragment_table_quote_currency);

        currencyFragment = new CurrencyFragment();
        mQuantifiers = new ArrayList<>();
        mResult = new ArrayList<>();

        mTableExpanded = false;

        for (int i = 0; i < 10; i++) {
            String rowQuantifierID = "tv_row_" + Helper.numberNames[i] + "_quantifier";
            String rowResultID = "tv_row_" + Helper.numberNames[i] + "_result";

            mQuantifiers.add((TextView) rootView.findViewById(getResources().getIdentifier(rowQuantifierID, "id", getActivity().getPackageName())));
            mResult.add((TextView) rootView.findViewById(getResources().getIdentifier(rowResultID, "id", getActivity().getPackageName())));

            final int expandQuantifier = i + 1;
            mQuantifiers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTableExpanded && (expandQuantifier == 1 || expandQuantifier == 10)) {
                        mTableExpanded = false;
                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                    } else {
                        if (!mTableExpanded) {
                            mTableExpanded = true;
                            SquarkEngine.getInstance().expandTable(getActivity(), mQuantifiers, mResult, expandQuantifier);
                        }
                    }
                }
            });
        }

        rootView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                if (!mTableExpanded) {
                    SquarkEngine.getInstance().swipeLeft();
                    SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                }
            }

            public void onSwipeRight() {
                if (!mTableExpanded) {
                    SquarkEngine.getInstance().swipeRight();
                    SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                }
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

        if (!new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH).format(new Date()).equals(getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "01/01/2000"))) {
            Call<APIWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
            call.enqueue(new Callback<APIWrapper>() {
                @Override
                public void onResponse(Call<APIWrapper> call, Response<APIWrapper> response) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                    editor.putString(Helper.DATE_PREFERENCE, response.body().getDate());
                    editor.putString(Helper.TIME_PREFERENCE, new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date()));
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
