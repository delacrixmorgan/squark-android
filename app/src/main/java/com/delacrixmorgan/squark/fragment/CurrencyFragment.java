package com.delacrixmorgan.squark.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SettingActivity;
import com.delacrixmorgan.squark.adapter.CurrencyAdapter;
import com.delacrixmorgan.squark.network.InterfaceAPI;
import com.delacrixmorgan.squark.network.SquarkAPI;
import com.delacrixmorgan.squark.shared.Helper;
import com.delacrixmorgan.squark.wrapper.APIWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Delacrix Morgan on 07/08/2017.
 */

public class CurrencyFragment extends Fragment {
    private static String TAG = "CurrencyFragment";

    private Toolbar mToolbar;
    private RecyclerView mCurrencyRecyclerView;
    private TextView mUpdateBarText;
    private ImageView mUpdateBarButton;
    private RelativeLayout mSettingButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.view_toolbar);
        mUpdateBarText = (TextView) rootView.findViewById(R.id.fragment_currency_update_bar_text);
        mUpdateBarButton = (ImageView) rootView.findViewById(R.id.fragment_currency_update_bar_button);
        mSettingButton = (RelativeLayout) rootView.findViewById(R.id.view_toolbar_layout_right_button);
        mCurrencyRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_currency_recycler_view);

        getActivity().setActionBar(mToolbar);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String updateDateText = "Last Updated "
                + getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "2000-01-01") + " at "
                + getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.TIME_PREFERENCE, "12:00 am");

        mUpdateBarText.setText(updateDateText);
        mUpdateBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateBarText.setText("Updating..");

                Call<APIWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
                call.enqueue(new Callback<APIWrapper>() {
                    @Override
                    public void onResponse(Call<APIWrapper> call, Response<APIWrapper> response) {
                        String updatedDate = Helper.getCurrentDate();
                        String updatedTime = Helper.getCurrentTime();

                        final String updatedDateTime = "Last Updated " + updatedDate + " at " + updatedTime;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mUpdateBarText.setText(updatedDateTime);
                            }
                        }, 2000);

                        response.body().updateCurrencyList();
                        mUpdateBarText.setText("Successfully Updated!");

                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                        editor.putString(Helper.DATE_PREFERENCE, updatedDate);
                        editor.putString(Helper.TIME_PREFERENCE, updatedTime);
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Call<APIWrapper> call, Throwable t) {
                        Log.e(TAG, "onFailure (URL) : " + call.request().url());
                        Log.e(TAG, "onFailure (Message) : " + t.toString());
                    }
                });
            }
        });

        CurrencyAdapter currencyAdapter = this.getArguments() != null ? new CurrencyAdapter(getActivity(), this.getArguments().getString(Helper.TYPE_CONVERT, "BASE")) : null;

        if (currencyAdapter == null) {
            getActivity().onBackPressed();
        }

        mCurrencyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCurrencyRecyclerView.setAdapter(currencyAdapter);
        mCurrencyRecyclerView.scrollToPosition(0);

        mToolbar.setTitle(R.string.choose_currency);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        mSettingButton.setVisibility(View.VISIBLE);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }
}
