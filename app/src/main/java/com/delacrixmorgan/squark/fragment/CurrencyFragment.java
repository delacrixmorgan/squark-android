package com.delacrixmorgan.squark.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.adapter.CurrencyAdapter;
import com.delacrixmorgan.squark.shared.Helper;

/**
 * Created by Delacrix Morgan on 07/08/2017.
 */

public class CurrencyFragment extends Fragment {
    private static String TAG = "CurrencyFragment";

    private Toolbar mToolbar;
    private RecyclerView mCurrencyRecyclerView;
    private CurrencyAdapter mCurrencyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_currency_toolbar);
        mCurrencyRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_currency_recycler_view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCurrencyAdapter = new CurrencyAdapter(getActivity(), bundle.getString(Helper.TYPE_CONVERT, "BASE"));
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();

        mCurrencyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCurrencyRecyclerView.setAdapter(mCurrencyAdapter);
        mCurrencyRecyclerView.scrollToPosition(0);
    }

    private void setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
}
