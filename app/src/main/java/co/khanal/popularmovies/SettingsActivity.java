package co.khanal.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by abhi on 2/18/16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.xml);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_by_key)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(preference instanceof ListPreference){
                ListPreference pref = (ListPreference) preference;
                int index = pref.findIndexOfValue(newValue.toString());
                if(index >= 0){
                    pref.setSummary(pref.getEntries()[index]);
                }
            } else {
                preference.setSummary(newValue.toString());
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);

            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(getActivity())
                            .getString(preference.getKey(), "")
            );



        }
    }



}
