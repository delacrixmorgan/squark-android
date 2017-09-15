package com.delacrixmorgan.squark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.delacrixmorgan.squark.adapter.CurrencyAdapter;
import com.delacrixmorgan.squark.network.InterfaceAPI;
import com.delacrixmorgan.squark.network.SquarkAPI;
import com.delacrixmorgan.squark.wrapper.RestWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Delacrix Morgan on 07/08/2017.
 */

public class CurrencyFragment extends Fragment {
    private static String TAG = "CurrencyFragment";

    private android.widget.Toolbar mToolbar;
    private RecyclerView mCurrencyRecyclerView;
    private CurrencyAdapter mCurrencyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_currency_toolbar);
        mCurrencyRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_currency_recycler_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();

        Call<RestWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
        call.enqueue(new Callback<RestWrapper>() {
            @Override
            public void onResponse(Call<RestWrapper> call, Response<RestWrapper> response) {
                Log.i(TAG, "onResponse (URL)  : " + call.request().url());
                Log.i(TAG, "onResponse (Base) : " + response.body().getBase());
                Log.i(TAG, "onResponse (Date) : " + response.body().getDate());

                SquarkEngine.getInstance().setmCurrencyList(response.body().getCurrencyList());

                mCurrencyAdapter = new CurrencyAdapter(getActivity());

                mCurrencyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                mCurrencyRecyclerView.setAdapter(mCurrencyAdapter);
                mCurrencyRecyclerView.scrollToPosition(0);
            }

            @Override
            public void onFailure(Call<RestWrapper> call, Throwable t) {
                Log.e(TAG, "onFailure (URL) : " + call.request().url());
                Log.e(TAG, "onFailure (Message) : " + t.toString());
            }
        });
    }

    private void setupToolbar() {
        getActivity().setActionBar(mToolbar);

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back_white_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
