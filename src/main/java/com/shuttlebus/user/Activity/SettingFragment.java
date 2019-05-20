package com.shuttlebus.user.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.shuttlebus.user.R;

public class SettingFragment extends PreferenceFragmentCompat{
    private ListPreference progressPreference;
    private Preference openSourcePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preference);
        findPreference();


        openSourcePreference.setIcon(null);
        /*
         * Set summary to current value
         * */
        progressPreference.setSummary(progressPreference.getValue() + " progressBar");

        /*
         *
         * */
        progressPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });


        /*
         *
         * */
        progressPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue + " progressBar");
                progressPreference.setValueIndex(progressPreference.findIndexOfValue(newValue.toString()));

                return false;
            }
        });


        /*
         *
         * */
        openSourcePreference.setIconSpaceReserved(false);
        openSourcePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), OpenSourceActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    private void findPreference(){
        progressPreference = (ListPreference) findPreference("progressBarList");
        openSourcePreference = findPreference("openSource");
    }

}
