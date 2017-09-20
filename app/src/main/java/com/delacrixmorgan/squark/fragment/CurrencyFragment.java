package com.delacrixmorgan.squark.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.delacrixmorgan.squark.R;
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
    private CurrencyAdapter mCurrencyAdapter;
    private TextView mUpdateBarText;
    private ImageView mUpdateBarButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_currency_toolbar);
        mCurrencyRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_currency_recycler_view);
        mUpdateBarText = (TextView) rootView.findViewById(R.id.fragment_currency_update_bar_text);
        mUpdateBarButton = (ImageView) rootView.findViewById(R.id.fragment_currency_update_bar_button);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCurrencyAdapter = new CurrencyAdapter(getActivity(), bundle.getString(Helper.TYPE_CONVERT, "BASE"));
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String updateDateText = "Last Updated " + getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).getString(Helper.DATE_PREFERENCE, "2000-01-01");

        mUpdateBarText.setText(updateDateText);
        mUpdateBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<APIWrapper> call = SquarkAPI.getClient().create(InterfaceAPI.class).updateRates("USD");
                call.enqueue(new Callback<APIWrapper>() {
                    @Override
                    public void onResponse(Call<APIWrapper> call, Response<APIWrapper> response) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                        editor.putString(Helper.DATE_PREFERENCE, response.body().getDate());
                        editor.apply();

                        response.body().updateCurrencyList();

                        mUpdateBarText.setText("Updated");
                    }

                    @Override
                    public void onFailure(Call<APIWrapper> call, Throwable t) {
                        Log.e(TAG, "onFailure (URL) : " + call.request().url());
                        Log.e(TAG, "onFailure (Message) : " + t.toString());
                    }
                });
            }
        });

        mCurrencyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCurrencyRecyclerView.setAdapter(mCurrencyAdapter);
        mCurrencyRecyclerView.scrollToPosition(0);

        setupToolbar();
    }

    private void setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().setActionBar(mToolbar);
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back_white_24dp));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().popBackStack();
                }
            });
        }
    }
}
