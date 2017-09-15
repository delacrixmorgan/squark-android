package com.delacrixmorgan.squark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delacrixmorgan.squark.listener.OnSwipeTouchListener;
import com.delacrixmorgan.squark.shared.Helper;

import java.util.ArrayList;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class TableFragment extends Fragment {
    private static String TAG = "TableFragment";

    private ArrayList<TextView> mQuantifiers, mResult;
    private TextView mBaseCurrency, mQuoteCurrency;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        mBaseCurrency = (TextView) rootView.findViewById(R.id.row_base_currency);
        mQuoteCurrency = (TextView) rootView.findViewById(R.id.row_quote_currency);

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

        mQuantifiers = new ArrayList<>();
        mResult = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String rowQuantifierID = "tv_row_" + Helper.numberNames[i] + "_quantifier";
            String rowResultID = "tv_row_" + Helper.numberNames[i] + "_result";

            mQuantifiers.add((TextView) rootView.findViewById(getResources().getIdentifier(rowQuantifierID, "id", getActivity().getPackageName())));
            mResult.add((TextView) rootView.findViewById(getResources().getIdentifier(rowResultID, "id", getActivity().getPackageName())));
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SquarkEngine.getInstance().updateTable(getActivity(), mQuantifiers, mResult);

        mBaseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_vg_fragment, new CurrencyFragment())
                        .addToBackStack(TableFragment.TAG)
                        .commit();
            }
        });

        mQuoteCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_vg_fragment, new CurrencyFragment())
                        .addToBackStack(TableFragment.TAG)
                        .commit();
            }
        });
    }
}
