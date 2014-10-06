package jp.agedash999.android.checklistbox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingActivity extends Activity{

	private SettingFragment fragment;
	private SettingActivity mInstance;
	private SharedPreferences mPreferences;

	public static final String KEY_VIEWITEM_HOME = "viewitem_home";
	public static final String KEY_VIEWITEM_STOCK = "viewitem_stock";
	public static final String KEY_VIEWITEM_HISTORY = "viewitem_history";
	public static final String KEY_STOCK_INIT = "stock_init";
	public static final String KEY_HISTORY_DELETE = "history_delete";
	public static final String KEY_HISTORY_DATE = "history_delete_date";
	public static final String PREF_COMPLETE_COMFIRM = "clist_complete_confirm";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment).commit();
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
    	.registerOnSharedPreferenceChangeListener(fragment.prefListener);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
    	.unregisterOnSharedPreferenceChangeListener(fragment.prefListener);
    }

	public static class SettingFragment extends PreferenceFragment{

		private final OnSharedPreferenceChangeListener prefListener = new OnSharedPreferenceChangeListener() {
		    @Override
		    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		        if (key.equals(KEY_VIEWITEM_HOME) ||
		        	key.equals(KEY_VIEWITEM_STOCK) ||
		        	key.equals(KEY_VIEWITEM_HISTORY) ||
		        	key.equals(KEY_STOCK_INIT) ||
		        	key.equals(KEY_HISTORY_DATE) ||
		        	key.equals(PREF_COMPLETE_COMFIRM)
		        		) {
		        	ListPreference pref = (ListPreference)findPreference(key);
		            pref.setSummary(pref.getEntry());
		        }else if(key.equals(KEY_HISTORY_DELETE)){
		        	((ListPreference)findPreference(KEY_HISTORY_DATE)).setEnabled(
		        			sharedPreferences.getBoolean(key, false));
		        }
		    }
		};

		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);
            ListPreference pref;
            pref = (ListPreference)findPreference(KEY_VIEWITEM_HOME);
            pref.setSummary(pref.getEntry());
            pref = (ListPreference)findPreference(KEY_VIEWITEM_STOCK);
            pref.setSummary(pref.getEntry());
            pref = (ListPreference)findPreference(KEY_VIEWITEM_HISTORY);
            pref.setSummary(pref.getEntry());
            pref = (ListPreference)findPreference(KEY_STOCK_INIT);
            pref.setSummary(pref.getEntry());
            pref = (ListPreference)findPreference(KEY_HISTORY_DATE);
            pref.setSummary(pref.getEntry());
            pref = (ListPreference)findPreference(PREF_COMPLETE_COMFIRM);
            pref.setSummary(pref.getEntry());
            pref = null;
        }
	}
}
