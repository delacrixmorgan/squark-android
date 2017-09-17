package com.delacrixmorgan.squark.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delacrixmorgan.squark.MainActivity;
import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.SquarkEngine;
import com.delacrixmorgan.squark.fragment.TableFragment;
import com.delacrixmorgan.squark.model.Currency;
import com.delacrixmorgan.squark.shared.Helper;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Delacrix Morgan on 14/09/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private Context mContext;
    private String mTypeConvert;

    public CurrencyAdapter(Context context, String typeConvert) {
        mContext = context;
        mTypeConvert = typeConvert;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_country, parent, false);

        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, final int position) {
        Currency currency = SquarkEngine.getInstance().getmCurrencyList().get(position);

        holder.countryFlag.setBackgroundResource(mContext.getResources().getIdentifier(currency.getCountry().toLowerCase(), "drawable", mContext.getPackageName()));
        holder.countryCode.setText(currency.getCode());
        holder.countryDescription.setText(currency.getDescription());
        holder.countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                editor.putInt(mTypeConvert.equals("BASE") ? Helper.BASE_CURRENCY_PREFERENCE : Helper.QUOTE_CURRENCY_PREFERENCE, position);
                editor.apply();

                ((MainActivity) mContext).onBackPressed();
            }
        });
    }

    @Override
    public int getItemCount() {
        return SquarkEngine.getInstance().getmCurrencyList().size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout countryLayout;
        ImageView countryFlag;
        TextView countryCode, countryDescription;

        private CurrencyViewHolder(View itemView) {
            super(itemView);

            countryLayout = (RelativeLayout) itemView.findViewById(R.id.view_country_layout);
            countryFlag = (ImageView) itemView.findViewById(R.id.view_country_flag);
            countryCode = (TextView) itemView.findViewById(R.id.view_country_code);
            countryDescription = (TextView) itemView.findViewById(R.id.view_country_description);
        }
    }
}
