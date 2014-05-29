package jp.agedash999.android.checklistbox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingActivity extends Activity {

	private SettingFragment fragment;
	private SettingActivity mInstance;
	private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment).commit();
    }

    public SharedPreferences getPreferences(){
    	return mPreferences;
    }

	public static class SettingFragment extends PreferenceFragment
	implements OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // 変更通知処理

        }
	}
}
