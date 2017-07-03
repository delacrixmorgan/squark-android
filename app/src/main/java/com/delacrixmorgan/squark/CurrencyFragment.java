package com.delacrixmorgan.squark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

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

        for (int i = 1; i <= 10; i++) {
            View subRow = LayoutInflater.from(getActivity()).inflate(R.layout.view_row, null);
            ((TextView) subRow.findViewById(R.id.row_quantifier)).setText(String.valueOf(i));
            mTableLayout.addView(subRow);
        }
    }
}
