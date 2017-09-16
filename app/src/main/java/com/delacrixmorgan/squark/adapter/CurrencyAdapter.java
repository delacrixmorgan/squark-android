package com.delacrixmorgan.squark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;
import com.delacrixmorgan.squark.model.Currency;

/**
 * Created by Delacrix Morgan on 14/09/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private Context mContext;

    public CurrencyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_country, parent, false);

        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        Currency currency = SquarkEngine.getInstance().getmCurrencyList().get(position);

        holder.countryFlag.setBackgroundResource(mContext.getResources().getIdentifier(currency.getCountry().toLowerCase(), "drawable", mContext.getPackageName()));
        holder.countryCode.setText(currency.getCode());
        holder.countryDescription.setText(currency.getDescription());
    }

    @Override
    public int getItemCount() {
        return SquarkEngine.getInstance().getmCurrencyList().size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        ImageView countryFlag;
        TextView countryCode, countryDescription;

        private CurrencyViewHolder(View itemView) {
            super(itemView);

            countryFlag = (ImageView) itemView.findViewById(R.id.view_country_flag);
            countryCode = (TextView) itemView.findViewById(R.id.view_country_code);
            countryDescription = (TextView) itemView.findViewById(R.id.view_country_description);
        }
    }
}
