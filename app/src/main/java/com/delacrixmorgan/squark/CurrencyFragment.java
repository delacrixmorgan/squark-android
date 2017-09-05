package com.delacrixmorgan.squark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delacrixmorgan.squark.model.Currency;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<RestWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
        call.enqueue(new Callback<RestWrapper>() {
            @Override
            public void onResponse(Call<RestWrapper> call, Response<RestWrapper> response) {
                Log.i(TAG, "onResponse (URL)  : " + call.request().url());
                Log.i(TAG, "onResponse (Base) : " + response.body().getBase());
                Log.i(TAG, "onResponse (Date) : " + response.body().getDate());

                for (Currency currency : response.body().getCurrencyList()) {
                    Log.i(TAG, "onResponse (" + currency.getName() + ") : " + currency.getRate());
                }
            }

            @Override
            public void onFailure(Call<RestWrapper> call, Throwable t) {
                Log.e(TAG, "onFailure (URL) : " + call.request().url());
                Log.e(TAG, "onFailure (Message) : " + t.toString());
            }
        });
    }
}
