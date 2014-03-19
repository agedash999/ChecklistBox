package jp.agedash999.android.checklistbox;

import android.os.Bundle;
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

public class MainActivity extends FragmentActivity{

	private Fragment mFragmentAtPos0 = null;
	private Fragment mFragmentAtPos1 = null;
	private Fragment mFragmentAtPos2 = null;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mLeftDrawer;
	private ListView mDrawerListMain;
	private ListView mDrawerListSub;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private String[] mDrawerMainMenuItems;
	private String[] mDrawerSubMenuItems;

	private ChecklistManager mCLManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentAtPos0 = new HomeFragment();
		mFragmentAtPos1 = new StockFragment();
		mFragmentAtPos2 = new HistoryFragment();

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
		mDrawerListMain.setOnItemClickListener(new DrawerMenuListener());

		//TODO タイトルをフラグメントごとに設定する
		mTitle = mDrawerTitle = getTitle();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			//TODO アクションバーのメニューON/OFF

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				setTitle(mTitle);
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// 起動時のFragmentとしてホーム画面をセットする
		getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, mFragmentAtPos0)
		.commit();

		mCLManager = new ChecklistManager();
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
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// アプリアイコンタップ
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private class DrawerMenuListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(parent.equals(mDrawerListMain)){
				changeFragment(position);
			}else if(parent.equals(mDrawerSubMenuItems)){

			}
		}

		private void changeFragment(int position) {
			// Create a new fragment and specify the planet to show based on position
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = mFragmentAtPos0;
				break;

			case 1:
				fragment = mFragmentAtPos1;
				break;

			case 2:
				fragment = mFragmentAtPos2;
				break;

			default:
				fragment = mFragmentAtPos0;
				break;
			}
			//			Bundle args = new Bundle();
			//			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
			//			fragment.setArguments(args);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.main_layout, fragment);
			ft.addToBackStack(null);
			ft.commit();

			// Highlight the selected item, update the title, and close the drawer
			mDrawerListMain.setItemChecked(position, true);
			//		    setTitle(mPlanetTitles[position]);
			mDrawerLayout.closeDrawer(mLeftDrawer);
		}
	}
}
