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
import android.view.animation.AnimationUtils;
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

    private RealmResults<Currency> mRealmResultsCurrency;
    private ArrayList<TextView> mQuantifiers, mResult;

    private TextView mBaseCurrency, mQuoteCurrency;
    private FloatingActionButton mSwapButton;
    private CurrencyFragment currencyFragment;
    private Boolean mTableExpanded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        mRealmResultsCurrency = Realm.getDefaultInstance().where(Currency.class).findAll();

        mBaseCurrency = (TextView) rootView.findViewById(R.id.fragment_table_base_currency);
        mQuoteCurrency = (TextView) rootView.findViewById(R.id.fragment_table_quote_currency);
        mSwapButton = (FloatingActionButton) rootView.findViewById(R.id.fragment_table_swap_button);

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
            mQuantifiers.get(i).setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSingleTap() {
                    if (mTableExpanded && (expandQuantifier == 1)) {
                        mTableExpanded = false;

                        mQuantifiers.get(0).setBackgroundColor(getResources().getColor(R.color.black));
                        mQuantifiers.get(9).setBackgroundColor(getResources().getColor(R.color.black));
                        mResult.get(0).setBackgroundColor(getResources().getColor(R.color.black));
                        mResult.get(9).setBackgroundColor(getResources().getColor(R.color.black));

                        mQuantifiers.get(0).setTextColor(getResources().getColor(R.color.white));
                        mQuantifiers.get(9).setTextColor(getResources().getColor(R.color.white));
                        mResult.get(0).setTextColor(getResources().getColor(R.color.white));
                        mResult.get(9).setTextColor(getResources().getColor(R.color.white));

                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);

                    } else {
                        hintButton();

                        if (!mTableExpanded) {
                            mTableExpanded = true;

                            mQuantifiers.get(0).setBackgroundColor(getResources().getColor(R.color.amber));
                            mResult.get(0).setBackgroundColor(getResources().getColor(R.color.amber));
//                            mQuantifiers.get(9).setBackgroundColor(getResources().getColor(R.color.amber));
//                            mResult.get(9).setBackgroundColor(getResources().getColor(R.color.amber));

                            mQuantifiers.get(0).setTextColor(getResources().getColor(R.color.black));
                            mResult.get(0).setTextColor(getResources().getColor(R.color.black));
//                            mQuantifiers.get(9).setTextColor(getResources().getColor(R.color.black));
//                            mResult.get(9).setTextColor(getResources().getColor(R.color.black));

                            SquarkEngine.getInstance().expandTable(getActivity(), mQuantifiers, mResult, expandQuantifier);
                        }
                    }
                }

                @Override
                public void onSwipeLeft() {
                    if (!mTableExpanded) {
                        SquarkEngine.getInstance().swipeLeft();
                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                    } else {
                        hintButton();
                    }
                }

                @Override
                public void onSwipeRight() {
                    if (!mTableExpanded) {
                        SquarkEngine.getInstance().swipeRight();
                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                    } else {
                        hintButton();
                    }
                }
            });

            mResult.get(i).setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSingleTap() {
                    if (mTableExpanded) {
                        hintButton();
                    }
                }

                @Override
                public void onSwipeLeft() {
                    if (!mTableExpanded) {
                        SquarkEngine.getInstance().swipeLeft();
                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                    } else {
                        hintButton();
                    }
                }

                @Override
                public void onSwipeRight() {
                    if (!mTableExpanded) {
                        SquarkEngine.getInstance().swipeRight();
                        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);
                    } else {
                        hintButton();
                    }
                }
            });
        }

        return rootView;
    }

    private void hintButton() {
        mQuantifiers.get(0).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.wobble));
        mResult.get(0).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.wobble));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

                mTableExpanded = false;

                mQuantifiers.get(0).setBackgroundColor(getResources().getColor(R.color.black));
                mQuantifiers.get(9).setBackgroundColor(getResources().getColor(R.color.black));
                mResult.get(0).setBackgroundColor(getResources().getColor(R.color.black));
                mResult.get(9).setBackgroundColor(getResources().getColor(R.color.black));

                mQuantifiers.get(0).setTextColor(getResources().getColor(R.color.white));
                mQuantifiers.get(9).setTextColor(getResources().getColor(R.color.white));
                mResult.get(0).setTextColor(getResources().getColor(R.color.white));
                mResult.get(9).setTextColor(getResources().getColor(R.color.white));

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
        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);

        mBaseCurrency.setText(baseCurrency.getCode());
        mQuoteCurrency.setText(quoteCurrency.getCode());
    }
}
