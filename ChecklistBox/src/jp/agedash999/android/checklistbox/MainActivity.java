package jp.agedash999.android.checklistbox;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{

	public static final int MENU_ADD_ID = R.id.menu_add;
	public static final int MENU_MOVE_ID = R.id.menu_move;
	public static final int MENU_SORT_ID = R.id.menu_sort;
	public static final int MENU_SETTINGS_ID = R.id.menu_settings;
	public static final int APL_NAME_ID = R.string.app_name;

	private static final int RQC_SETTINGS = 100;

	private ChecklistBoxChildFragment homeFragment = null;
	private ChecklistBoxChildFragment stockFragment = null;
	private ChecklistBoxChildFragment historyFragment = null;
	private ChecklistBoxChildFragment categoryEditFragment = null;

	private ChecklistBoxChildFragment childFragment;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mLeftDrawer;
	private ListView mDrawerListMain;
	private ListView mDrawerListSub;
	private ActionBarDrawerToggle mDrawerToggle;

	private String appName;

	private TextView mTitleMain;
	private TextView mTitleSub;

	private String[] mDrawerMainMenuItems;
	private String[] mDrawerSubMenuItems;
	private DrawerMenuListener mDrawerListener;

	private ChecklistManager mCLManager;
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		homeFragment = HomeFragment.newInstance();
		stockFragment = StockFragment.newInstance();
		historyFragment = HistoryFragment.newInstance();
		categoryEditFragment = new CategoryEditFragment();

		childFragment = homeFragment;

		appName = getResources().getString(APL_NAME_ID);

		// ActionBar関連の設定
		ActionBar bar = getActionBar();
		View actionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);

		mTitleMain = (TextView)actionBarView.findViewById(R.id.title_main);
		mTitleSub = (TextView)actionBarView.findViewById(R.id.title_sub);

		bar.setCustomView(actionBarView);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        bar.setDisplayShowHomeEnabled(true);

		// ドロワー関連のUI取得
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerListMain = (ListView) findViewById(R.id.list_drawer_main);
		mDrawerListSub = (ListView) findViewById(R.id.list_drawer_sub);

		// ドロワーのメニュー文字列取得
		mDrawerMainMenuItems = getResources().getStringArray(R.array.drawer_menu1);
		mDrawerSubMenuItems = getResources().getStringArray(R.array.drawer_menu2);

		// ドロワーのListAdapterセット
		mDrawerListMain.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , mDrawerMainMenuItems));
		mDrawerListSub.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , mDrawerSubMenuItems));

		// ドロワーのListItemクリックリスナーセット
		mDrawerListener = new DrawerMenuListener();
		mDrawerListMain.setOnItemClickListener(mDrawerListener);
		mDrawerListSub.setOnItemClickListener(mDrawerListener);

		//TODO タイトルをフラグメントごとに設定する
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			//TODO アクションバーのメニューON/OFF

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
//				setTitle(mTitle);
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
//				setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
//		setTitle("");

		// 起動時のFragmentとしてホーム画面をセットする
		getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, (Fragment)childFragment)
		.commit();

		mCLManager = new ChecklistManager(this);
	}

	public ChecklistManager getChecklistManager(){
		return mCLManager;
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		//		menu.getItem(0).setEnabled(false);

		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		if(mDrawerLayout.isDrawerOpen(mLeftDrawer)){
//			menu.findItem(R.id.menu_add).setEnabled(false);
//			menu.findItem(R.id.menu_settings).setVisible(false);
			for(int i = 0 ; i<menu.size() ; i++){
				menu.getItem(i).setEnabled(false);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			// アプリアイコンがタップされた場合、ここで処理しない（Drawerを開く）
			return true;
		}
		//ActionBarアイコンがタップされた場合はここで処理
		int itemId = item.getItemId();

		switch (itemId) {
//		case MENU_ADD_ID:
//
//			break;
		case MENU_MOVE_ID:
			childFragment.onClickMenu(itemId);

			break;
		case MENU_SORT_ID:
			childFragment.onClickMenu(itemId);

			break;
		case MENU_SETTINGS_ID:
			//設定の場合はここで捌く
//			Intent intent = new Intent(this, SettingActivity.class);
//			startActivityForResult(intent, RQC_SETTINGS);
			callSettings();

			break;
		default:
			childFragment.onClickMenu(itemId);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void callSettings(){
		Intent intent = new Intent(this, SettingActivity.class);
		startActivityForResult(intent, RQC_SETTINGS);
	}

	private class DrawerMenuListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(parent.equals(mDrawerListMain)){
				changeFragment(position);
				mDrawerLayout.closeDrawer(mLeftDrawer);
			}else if(parent.equals(mDrawerListSub)){
				if(position == 0){
					callCategoryEdit(position);
					mDrawerLayout.closeDrawer(mLeftDrawer);
				}else if(position == 1){
					callSettings();
					mDrawerLayout.closeDrawer(mLeftDrawer);
				}
			}
		}

		private void changeFragment(int position) {
			switch (position) {
			case 0:
				childFragment = homeFragment;
				break;

			case 1:
				childFragment = stockFragment;
				break;

			case 2:
				childFragment = historyFragment;
				break;

//			default:
//				break;
			}
			//			Bundle args = new Bundle();
			//			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
			//			fragment.setArguments(args);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.main_layout, (Fragment)childFragment);
			ft.addToBackStack(null);
			ft.commit();

			// Highlight the selected item, update the title, and close the drawer
//			mDrawerListMain.setItemChecked(position, true);
			//		    setTitle(mPlanetTitles[position]);
//			mDrawerLayout.closeDrawer(mLeftDrawer);
		}

		private void callCategoryEdit(int position){
			childFragment = categoryEditFragment;
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.main_layout, (Fragment)childFragment);
			ft.addToBackStack(null);
			ft.commit();

			// Highlight the selected item, update the title, and close the drawer
			mDrawerListMain.setItemChecked(position, true);
			//		    setTitle(mPlanetTitles[position]);
			mDrawerLayout.closeDrawer(mLeftDrawer);
		}

	}

	public void notifyChangeFragment(ChecklistBoxChildFragment fragment){
		//TODO 画面変更時の処理
		//TODO メニューボタンのON/OFF

		childFragment = fragment;
		setTitleText(childFragment.getFragmenTitle(), childFragment.getFragmenSubTitle());
	}

	public void setTitleText(String main, String sub){
		if(sub == null){
			mTitleSub.setVisibility(View.GONE);
			mTitleMain.setText(appName + "> " + main);
		}else{
			mTitleSub.setVisibility(View.VISIBLE);
			mTitleMain.setText(appName + " > " + main);
			mTitleSub.setText(sub);
		}
	}

	@Override
	protected void onActivityResult(
			int requestCode,int resultCode,Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RQC_SETTINGS == resultCode){
			loadPreference();
		}
	}

	public void loadPreference(){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public SharedPreferences getPreference(){
		return mPreferences;
	}

	public interface ChecklistBoxChildFragment{

		public String getFragmenTitle();

		public String getFragmenSubTitle();

		public void onClickMenu(int menuId);

	}
}