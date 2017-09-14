package com.delacrixmorgan.squark.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;

/**
 * Created by Delacrix Morgan on 14/09/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {
    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_country, parent, false);

        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        holder.countryCode.setText(SquarkEngine.getInstance().getmCurrencyList().get(position).getCode());
        holder.countryDescription.setText(SquarkEngine.getInstance().getmCurrencyList().get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return SquarkEngine.getInstance().getmCurrencyList().size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView countryCode, countryDescription;

        private CurrencyViewHolder(View itemView) {
            super(itemView);
            countryCode = (TextView) itemView.findViewById(R.id.tv_country_code);
            countryDescription = (TextView) itemView.findViewById(R.id.tv_country_description);
        }
    }
}