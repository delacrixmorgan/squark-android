package com.delacrixmorgan.squark.shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Delacrix Morgan on 08/07/2017.
 */

public class Helper {

    final public static String SHARED_PREFERENCE = "SHARED_PREFERENCE";
    final public static String DATE_PREFERENCE = "DATE_PREFERENCE";
    final public static String TIME_PREFERENCE = "TIME_PREFERENCE";
    final public static String QUICK_GUIDE_PREFERENCE = "QUICK_GUIDE_PREFERENCE";
    final public static String BASE_CURRENCY_PREFERENCE = "BASE_CURRENCY_PREFERENCE";
    final public static String QUOTE_CURRENCY_PREFERENCE = "QUOTE_CURRENCY_PREFERENCE";

    final public static String TYPE_CONVERT = "TYPE_CONVERT";

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
    }

    public static int getCurrencyPreference(Context context, String typeCurrency) {
        return context.getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE).getInt(typeCurrency, 0);
    }

    public static void launchPage(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
