package jp.agedash999.android.checklistbox;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends FragmentActivity{

	private final int POSITION_HOME = 0;
	private final int POSITION_STOCK = 1;
	private final int POSITION_HISTORY = 2;

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

	//	private SectionsPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentAtPos0 = new HomeFragment();
		mFragmentAtPos1 = new StockFragment();
		mFragmentAtPos2 = new HistoryFragment();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerListMain = (ListView) findViewById(R.id.list_drawer_main);
		mDrawerListSub = (ListView) findViewById(R.id.list_drawer_sub);

		mDrawerMainMenuItems = getResources().getStringArray(R.array.drawer_menu1);
		mDrawerSubMenuItems = getResources().getStringArray(R.array.drawer_menu2);

		// Set the adapter for the list view
		mDrawerListMain.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , mDrawerMainMenuItems));
		mDrawerListSub.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , mDrawerSubMenuItems));
//		Set the list's click listener
		mDrawerListMain.setOnItemClickListener(new DrawerMenuListener());

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//		getActionBar().setHomeButtonEnabled(true);

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

	//    @Override
	//    public void onConfigurationChanged(Configuration newConfig) {
	//        super.onConfigurationChanged(newConfig);
	//        mDrawerToggle.onConfigurationChanged(newConfig);
	//    }

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
			getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment)
			.commit();

			// Highlight the selected item, update the title, and close the drawer
			mDrawerListMain.setItemChecked(position, true);
			//		    setTitle(mPlanetTitles[position]);
			mDrawerLayout.closeDrawer(mLeftDrawer);
		}
	}

	//	/**
	//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	//	 * one of the sections/tabs/pages.
	//	 */
	//	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
	//
	//
	//		private Fragment mFragmentAtPos0Back = null;
	//
	//		private final class FragmentReplaceListener implements IFragmentReplaceListener{
	//
	//			@Override
	//			public void onSwitchToNextFragment() {
	//				FragmentTransaction ft = mFragmentManager.beginTransaction();
	////				ft.addToBackStack(null);
	//				ft.remove(mFragmentAtPos0);
	//				ft.commit();
	//				int a[] = {1,2,3};
	//				int b[] = a;
	//				b = new int[]{5,6,7};
	//				if (mFragmentAtPos0 instanceof HomeFragment){
	//					mFragmentAtPos0Back = mFragmentAtPos0;
	//					mFragmentAtPos0 = new ChecklistFragment(mListener);
	//				}else if(mFragmentAtPos0 instanceof ChecklistFragment){ // Instance of NextFragment
	//					mFragmentAtPos0 = mFragmentAtPos0Back;
	//					mFragmentAtPos0Back = null;
	//				}
	//				notifyDataSetChanged();
	//			}
	//
	//		}
	//		public SectionsPagerAdapter(FragmentManager fm) {
	//			super(fm);
	//			mFragmentManager = fm;
	//		}
	//
	//		@Override
	//		public int getItemPosition(Object object) {
	//			if (object instanceof HomeFragment && mFragmentAtPos0 instanceof ChecklistFragment){
	//				return POSITION_NONE;
	//			}
	//			if (object instanceof ChecklistFragment && mFragmentAtPos0 instanceof HomeFragment){
	//				return POSITION_NONE;
	//			}
	//			return POSITION_UNCHANGED;
	//		}
	//		@Override
	//		public Fragment getItem(int position) {
	//			Fragment fragment = null;
	//			switch (position) {
	//			case POSITION_HOME:
	//				if(mFragmentAtPos0 == null){
	//					mFragmentAtPos0 = new HomeFragment(mListener);
	//				}
	//				fragment = mFragmentAtPos0;
	//				break;
	//			case POSITION_STOCK:
	//				if(mFragmentAtPos1 == null){
	//					mFragmentAtPos1 = new StockFragment();
	//				}
	//				fragment = mFragmentAtPos1;
	//				break;
	//			case POSITION_HISTORY:
	//				if(mFragmentAtPos2 == null){
	//					mFragmentAtPos2 = new HistoryFragment();
	//				}
	//				fragment = mFragmentAtPos2;
	//				break;
	//			default:
	//				break;
	//			}
	//			//			Bundle args = new Bundle();
	//			//			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
	//			//			fragment.setArguments(args);
	//			return fragment;
	//		}
	//
	//		@Override
	//		public int getCount() {
	//			// Show 3 total pages.
	//			return 3;
	//		}
	//	}
}
