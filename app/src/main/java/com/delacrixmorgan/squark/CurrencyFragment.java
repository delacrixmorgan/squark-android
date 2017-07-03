package com.delacrixmorgan.squark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class CurrencyFragment extends Fragment {
    private TableLayout mTableLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency, container, false);
        mTableLayout = (TableLayout) rootView.findViewById(R.id.currency_table);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SquarkEngine.getInstance().updateTable(getActivity(), mTableLayout, 1);
    }
}
