package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerMenu {

	private final MainActivity activity;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mLeftDrawer;
	private ListView mDrawerListMain;
	private ListView mDrawerListSub;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] mDrawerMainMenuItems;
	private String[] mDrawerSubMenuItems;
	private DrawerMenuListener mDrawerListener;


	public DrawerMenu(final MainActivity activity){
		this.activity = activity;

		// ドロワー関連のUI取得
		mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) activity.findViewById(R.id.left_drawer);
		mDrawerListMain = (ListView) activity.findViewById(R.id.list_drawer_main);
		mDrawerListSub = (ListView) activity.findViewById(R.id.list_drawer_sub);

		// ドロワーのメニュー文字列取得
		String[] arrMenu1Title = activity.getResources().getStringArray(R.array.drawer_menu1);
		String[] arrMenu1Icon = activity.getResources().getStringArray(R.array.drawer_menu1_icon);
		List<DrawerMenuItem> listMenu1 = new ArrayList<DrawerMenu.DrawerMenuItem>();

		int i = 0;
		while(i < arrMenu1Icon.length){
			listMenu1.add(new DrawerMenuItem(
					activity.getResources().getIdentifier(arrMenu1Icon[i], "drawable", activity.getPackageName()),
					arrMenu1Title[i]));
			i++;
		}

		String[] arrMenu2Title = activity.getResources().getStringArray(R.array.drawer_menu2);
		List<DrawerMenuItem> listMenu2 = new ArrayList<DrawerMenu.DrawerMenuItem>();

		for(String title : arrMenu2Title){
			listMenu2.add(new DrawerMenuItem(0, title));
		}

		// ドロワーのListAdapterセット
//		mDrawerListMain.setAdapter(new ArrayAdapter<String>(activity,
//				android.R.layout.simple_list_item_1 , mDrawerMainMenuItems));
//		mDrawerListSub.setAdapter(new ArrayAdapter<String>(activity,
//				android.R.layout.simple_list_item_1 , mDrawerSubMenuItems));

		mDrawerListMain.setAdapter(new DrawerListAdapter(activity,0 , listMenu1));
		mDrawerListSub.setAdapter(new DrawerListAdapter(activity,0 , listMenu2));

		// ドロワーのListItemクリックリスナーセット
		mDrawerListener = new DrawerMenuListener();
		mDrawerListMain.setOnItemClickListener(mDrawerListener);
		mDrawerListSub.setOnItemClickListener(mDrawerListener);

		//TODO タイトルをフラグメントごとに設定する
		mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			//TODO アクションバーのメニューON/OFF

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				//				setTitle(mTitle);
				activity.invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				//				setTitle(mDrawerTitle);
				activity.invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	public ActionBarDrawerToggle getDrawerToggle(){
		return mDrawerToggle;
	}

	public boolean isLeftDrawerOpen(){
		return mDrawerLayout.isDrawerOpen(mLeftDrawer);
	}

	public class DrawerListAdapter extends ArrayAdapter<DrawerMenuItem>{

		private List<DrawerMenuItem> menuList;
		private LayoutInflater layoutInflater;

		public DrawerListAdapter(Context context, int resource, List<DrawerMenuItem> objects) {
			super(context, resource, objects);
			this.menuList = objects;
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 特定の行(position)のデータを得る
			DrawerMenuItem item = menuList.get(position);

			// convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
			if (null == convertView) {
				convertView = layoutInflater.inflate(R.layout.listrow_drawer, null);
			}

			ImageView iconView = (ImageView) convertView.findViewById(R.id.iv_menuicon);
			// TODO アイコンのIDが0の場合はImageViewを不可視にする
			if(item.id_icon==0){
				iconView.setImageResource(item.id_icon);
				iconView.setVisibility(View.GONE);
			}else{
				iconView.setImageResource(item.id_icon);
				iconView.setVisibility(View.VISIBLE);
			}

			TextView titleView = (TextView) convertView.findViewById(R.id.txv_menutitle);
			titleView.setText(item.title);

			return convertView;
		}
	}

	private class DrawerMenuItem{
		public int id_icon;
		public String title;

		private DrawerMenuItem(int id_icon,String title){
			this.id_icon = id_icon;
			this.title = title;
		}
	}

	private class DrawerMenuListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(parent.equals(mDrawerListMain)){
				activity.changeFragment(position);
				mDrawerLayout.closeDrawer(mLeftDrawer);
			}else if(parent.equals(mDrawerListSub)){
				if(position == 0){
					activity.callCategoryEdit();
					mDrawerLayout.closeDrawer(mLeftDrawer);
					// Highlight the selected item, update the title, and close the drawer
					mDrawerListMain.setItemChecked(position, true);
					//		    setTitle(mPlanetTitles[position]);
					mDrawerLayout.closeDrawer(mLeftDrawer);

				}else if(position == 1){
					activity.callSettings();
					mDrawerLayout.closeDrawer(mLeftDrawer);
				}
			}
		}
	}
}
