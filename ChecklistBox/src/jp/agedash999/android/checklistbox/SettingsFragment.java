package jp.agedash999.android.checklistbox;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment  extends PreferenceFragment
implements IChildFragment{

	private SharedPreferences mPreferences;

	private MainActivity activity;
	private View rootView;
	private String mFragmentTitle;

	private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_SETTINGS_ID;

	public static final String KEY_VIEWITEM_HOME = "viewitem_home";
	public static final String KEY_VIEWITEM_STOCK = "viewitem_stock";
	public static final String KEY_VIEWITEM_HISTORY = "viewitem_history";
	public static final String KEY_STOCK_INIT = "stock_init";
	public static final String KEY_HISTORY_DELETE = "history_delete";
	public static final String KEY_HISTORY_DATE = "history_delete_date";
	public static final String PREF_COMPLETE_COMFIRM = "clist_complete_confirm";

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

	public SettingsFragment(){
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		activity.notifyChangeFragment(this);
		setHasOptionsMenu(true);

		return super.onCreateView(inflater, container, savedInstanceState);

	}


	@Override
	public void onPrepareOptionsMenu(android.view.Menu menu){
		menu.getItem(0).setEnabled(false);
		menu.getItem(0).setIcon(R.drawable.ic_alpha);
		menu.getItem(1).setEnabled(false);
		menu.getItem(1).setIcon(R.drawable.ic_alpha);
		menu.getItem(2).setEnabled(false);
		menu.getItem(2).setIcon(R.drawable.ic_alpha);
		menu.getItem(3).setEnabled(false);
		menu.getItem(3).setIcon(R.drawable.ic_alpha);

	}

    @Override
    public void onResume() {
    	super.onResume();
    	PreferenceManager.getDefaultSharedPreferences(activity)
    	.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
    	super.onPause();
    	PreferenceManager.getDefaultSharedPreferences(activity)
    	.unregisterOnSharedPreferenceChangeListener(prefListener);
    }
	@Override
	public String getFragmenTitle() {
		return mFragmentTitle;
	}

	@Override
	public int getFragmentIconID() {
		return 0;
	}

	@Override
	public void onClickMenu(int menuId) {
		// ここには入らない想定

	}

	@Override
	public int getFragmentPositionID() {
		return MainActivity.POS_FRAGMENT_SETTING;
	}
}
