package jp.agedash999.android.checklistbox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity{

	public static final int TITLE_HOME_ID = R.string.fragment_title_home;
	public static final int TITLE_STOCK_ID = R.string.fragment_title_stock;
	public static final int TITLE_HISTORY_ID = R.string.fragment_title_history;
	public static final int TITLE_CATEGORYEDIT_ID = R.string.fragment_title_categoryedit;
	public static final int TITLE_SETTINGS_ID = R.string.fragment_title_settings;

	public static final int POS_FRAGMENT_HOME = 0;
	public static final int POS_FRAGMENT_STOCK = 1;
	public static final int POS_FRAGMENT_HISTORY = 2;
	public static final int POS_FRAGMENT_CATEGORYEDIT = 3;
	public static final int POS_FRAGMENT_SETTING = 4;

	public static final int ICON_TITLE_HOME_ID = R.drawable.ic_home;
	public static final int ICON_TITLE_STOCK_ID = R.drawable.ic_stock;
	public static final int ICON_TITLE_HISTORY_ID = R.drawable.ic_history;

	public static final int MENU_ADD_ID = R.id.menu_add;
	public static final int MENU_MOVE_ID = R.id.menu_move;
	public static final int MENU_SORT_ID = R.id.menu_sort;
	public static final int MENU_SETTINGS_ID = R.id.menu_settings;
	public static final int APL_NAME_ID = R.string.app_name;

//	private static final int RQC_SETTINGS = 100;

	private DrawerMenu mDrawer;

	private IChildFragment homeFragment = null;
	private IChildFragment stockFragment = null;
	private IChildFragment historyFragment = null;
	private IChildFragment categoryEditFragment = null;
	private IChildFragment settingsFragment = null;

	private IChildFragment childFragment;


	private TextView mTitleMain;
	private ImageView mTitleIcon;

	private ChecklistManager mCLManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//設定の読み込み（初回起動のみ）
		PreferenceManager.setDefaultValues(this, R.xml.setting, false);
		Thread.setDefaultUncaughtExceptionHandler(
				new MyUncaughtExceptionHandler(getApplicationContext()));

		mCLManager = new ChecklistManager(this);

		homeFragment = HomeFragment.newInstance();
		stockFragment = StockFragment.newInstance();
		historyFragment = HistoryFragment.newInstance();
		categoryEditFragment = new CategoryEditFragment();
		settingsFragment = new SettingsFragment();

		childFragment = homeFragment;

		// ActionBar関連の設定

		// ※これだとうまくいかない※
		// 　タイトルがセンタリングされない
		// 　inflate と setCustomViewをそれぞれやってるのが問題？
//		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//		getSupportActionBar().setCustomView(R.layout.action_bar);

//		ActionBar bar = getActionBar();
//		View actionBarView = getLayoutInflater().inflate(R.layout.action_bar, null);
//
//		mTitleMain = (TextView)actionBarView.findViewById(R.id.title_main);
//		mTitleIcon = (ImageView) actionBarView.findViewById(R.id.iv_title);
//
//		bar.setCustomView(actionBarView);
//		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
//        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        bar.setDisplayShowHomeEnabled(true);

		ActionBar bar = getActionBar();
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
		bar.setCustomView(R.layout.action_bar);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        bar.setDisplayShowHomeEnabled(true);

		mTitleMain = (TextView)bar.getCustomView().findViewById(R.id.title_main);
		mTitleIcon = (ImageView)bar.getCustomView().findViewById(R.id.iv_title);

        mDrawer = new DrawerMenu(this);

        //DrawerMenuのコンストラクタに移転


        getActionBar().setDisplayHomeAsUpEnabled(true);
//		setTitle("");

        // TODO changeFragmentを通す？
		// 起動時のFragmentとしてホーム画面をセットする
//		getFragmentManager().beginTransaction().replace(R.id.main_layout, (Fragment)childFragment)
//		.commit();
//		callSettings();

	}

	@Override
	protected void onStart() {
		super.onStart();
		//前回バグで強制終了した場合はダイアログ表示
		//TODO バグレポートダイアログをどこから呼び出すか
//		MyUncaughtExceptionHandler.showBugReportDialogIfExist();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getFragmentManager().beginTransaction().replace(R.id.main_layout, (Fragment)childFragment)
		.commit();

	}

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	public ChecklistManager getChecklistManager(){
		return mCLManager;
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawer.getDrawerToggle().syncState();
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
		if(mDrawer.isLeftDrawerOpen()){
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
		if (mDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
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

	public void changeFragment(int position) {
		switch (position) {
		case POS_FRAGMENT_HOME:
			childFragment = homeFragment;
			break;

		case POS_FRAGMENT_STOCK:
			childFragment = stockFragment;
			break;

		case POS_FRAGMENT_HISTORY:
			childFragment = historyFragment;
			break;

//		default:
//			break;
		}
		//			Bundle args = new Bundle();
		//			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		//			fragment.setArguments(args);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.main_layout, (Fragment)childFragment);
		ft.addToBackStack(null);
		ft.commit();

		// Highlight the selected item, update the title, and close the drawer
//		mDrawerListMain.setItemChecked(position, true);
		//		    setTitle(mPlanetTitles[position]);
//		mDrawerLayout.closeDrawer(mLeftDrawer);
	}

	public void callCategoryEdit(){
		childFragment = categoryEditFragment;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.main_layout, (Fragment)childFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void callSettings(){
		childFragment = settingsFragment;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.main_layout, (Fragment)childFragment);
		ft.addToBackStack(null);
		ft.commit();

//		Intent intent = new Intent(this, SettingActivity.class);
//		startActivityForResult(intent, RQC_SETTINGS);
	}

	public void notifyChangeFragment(IChildFragment fragment){
		//TODO 画面変更時の処理
		//TODO メニューボタンのON/OFF

		childFragment = fragment;
		setTitleText(childFragment.getFragmenTitle() , childFragment.getFragmentIconID());
	}

	public void setTitleText(String title , int icon_id){

		mTitleMain.setText(title);
		if(icon_id==0){
			mTitleIcon.setVisibility(View.GONE);
		}else{
			mTitleIcon.setImageResource(icon_id);
			mTitleIcon.setVisibility(View.VISIBLE);
		}
	}

//	@Override
//	protected void onActivityResult(
//			int requestCode,int resultCode,Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (RQC_SETTINGS == resultCode){
//			loadPreference();
//		}
//	}

	public SharedPreferences getPreference(){
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

}