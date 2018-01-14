package com.delacrixmorgan.squark;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.delacrixmorgan.squark.shared.Helper;

/**
 * Created by Delacrix Morgan on 07/08/2017.
 */

public class SettingActivity extends PreferenceActivity {
    private static String TAG = "SettingActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference sQuickGuide, sCreditsLibrary, sVersion;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            sQuickGuide = findPreference("quick_guide");
            sCreditsLibrary = findPreference("credits_library");
            sVersion = findPreference("version_number");
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            sQuickGuide.setOnPreferenceClickListener(this);
            sCreditsLibrary.setOnPreferenceClickListener(this);

            try {
                sVersion.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "quick_guide":
                    getActivity().startActivity(new Intent(getActivity(), GuideActivity.class));
                    break;

                case "credits_library":
                    populateCreditsDialog();
                    break;
            }
            return true;
        }

        private void populateCreditsDialog() {
            final Dialog creditDialog = new Dialog(getActivity());

            creditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            creditDialog.setContentView(R.layout.view_credit);

            creditDialog.findViewById(R.id.iv_spartan_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "https://github.com/theleagueof/league-spartan");
                }
            });

            creditDialog.findViewById(R.id.iv_jenkins_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "https://github.com/chrisjenx/Calligraphy");
                }
            });

            creditDialog.findViewById(R.id.iv_realm_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "https://realm.io/");
                }
            });

            creditDialog.findViewById(R.id.iv_retrofit_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "http://square.github.io/retrofit/");
                }
            });

            creditDialog.findViewById(R.id.iv_parceler_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "https://github.com/johncarl81/parceler");
                }
            });

            creditDialog.findViewById(R.id.iv_gosquared_github).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.launchPage(getActivity(), "https://github.com/gosquared/flags");
                }
            });

            Button doneButton = (Button) creditDialog.findViewById(R.id.button_done);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    creditDialog.dismiss();
                }
            });
            creditDialog.show();
        }
    }
}
