package com.delacrixmorgan.squark.deprecrated.adapter;

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
import com.delacrixmorgan.squark.deprecrated.model.Currency;
import com.delacrixmorgan.squark.deprecrated.shared.Helper;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Delacrix Morgan on 14/09/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private RealmResults<Currency> mRealmResultCurrency;
    private Context mContext;
    private String mTypeConvert;

    public CurrencyAdapter(Context context, String typeConvert) {
        mRealmResultCurrency = Realm.getDefaultInstance().where(Currency.class).findAll();
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
    public void onBindViewHolder(final CurrencyViewHolder holder, int position) {
        Currency currency = mRealmResultCurrency.get(holder.getAdapterPosition());
        int selectedCurrencyPosition = mContext.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE)
                .getInt(mTypeConvert.equals("BASE") ? Helper.BASE_CURRENCY_PREFERENCE : Helper.QUOTE_CURRENCY_PREFERENCE, 0);

        holder.countryFlag.setBackgroundResource(mContext.getResources().getIdentifier(currency.getCountry().toLowerCase(), "drawable", mContext.getPackageName()));
        holder.countrySelected.setVisibility(holder.getAdapterPosition() == selectedCurrencyPosition ? View.VISIBLE : View.GONE);
        holder.countryCode.setText(currency.getCode());
        holder.countryDescription.setText(currency.getDescription());
        holder.countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                editor.putInt(mTypeConvert.equals("BASE") ? Helper.BASE_CURRENCY_PREFERENCE : Helper.QUOTE_CURRENCY_PREFERENCE, holder.getAdapterPosition());
                editor.apply();
                ((MainActivity) mContext).getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRealmResultCurrency.size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout countryLayout;
        ImageView countryFlag, countrySelected;
        TextView countryCode, countryDescription;

        private CurrencyViewHolder(View itemView) {
            super(itemView);

            countryLayout = (RelativeLayout) itemView.findViewById(R.id.view_country_layout);
            countryFlag = (ImageView) itemView.findViewById(R.id.view_country_flag);
            countrySelected = (ImageView) itemView.findViewById(R.id.view_country_selected);
            countryCode = (TextView) itemView.findViewById(R.id.view_country_code);
            countryDescription = (TextView) itemView.findViewById(R.id.view_country_description);
        }
    }
}