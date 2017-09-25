package com.delacrixmorgan.squark;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Delacrix Morgan on 07/08/2017.
 */

public class AboutActivity extends PreferenceActivity {
    private static String TAG = "AboutActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.view_toolbar, (ViewGroup) findViewById(android.R.id.content));
        findViewById(R.id.view_toolbar_right_button).setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        toolbar.setTitle(R.string.about);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setActionBar(toolbar);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static class SettingFragment extends PreferenceFragment {
        private Preference sCreditsLibrary, sVersion;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            sCreditsLibrary = findPreference("credits_library");
            sVersion = findPreference("version_number");
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            sCreditsLibrary.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return false;
                }
            });

            try {
                sVersion.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
