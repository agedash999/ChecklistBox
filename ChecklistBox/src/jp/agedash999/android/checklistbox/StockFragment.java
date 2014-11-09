package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.ContextMenuHandler.ContextMenuFragment;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;

public class StockFragment extends AbstractChildFragment
	implements ContextMenuFragment{

	private static AbstractChildFragment mInstance = new StockFragment();

	private MainActivity activity;
	private View rootView;
	private ExpandableListView listStock;
	private StockListAdapter mCLAdapter;
    private ContextMenuHandler mCMenuHandler;

    private String initView;

    private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_STOCK_ID;
    public static final int FRAGMENT_ICON_ID = MainActivity.ICON_TITLE_STOCK_ID;
	public static String mFragmentTitle;

	public static final String KEY_TITLE = "title";
	public static final String KEY_CATEGORY_ID = "category_id";

	public StockFragment() {
		super();
	}

	public static AbstractChildFragment newInstance(){
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//TODO 開発用ログ
		Log.d("checklistbox_dev_log", this.toString() + ":onCreateView called");

		this.rootView = inflater.inflate(R.layout.fragment_stock, container, false);
		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

		listStock = (ExpandableListView)rootView.findViewById(R.id.list_stock);

		//下位（Manager）との疎結合性を高めるため、渡されたList/Mapをここで格納し直す
		//TODO 格納し直すと、オブジェクト管理が複雑になるので要検討
//		List<String> categoryList = new ArrayList<String>();
//		List<List<Checklist>> stockList = new ArrayList<List<Checklist>>();
//		mCLManager.getStockAndCategory(categoryList, stockList);

		mCLAdapter = new StockListAdapter(getActivity(),
				activity.getChecklistManager().getStockList());
		listStock.setAdapter(mCLAdapter);
		listStock.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Checklist clist = (Checklist)parent.getExpandableListAdapter()
						.getChild(groupPosition, childPosition);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
//				AbstractChildFragment fragment = ChecklistFragment.newInstance(clist,mInstance);

                ChecklistFragment fragment = new ChecklistFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(ChecklistFragment.KEY_BUNDLE_CLIST_INDEX,
                		activity.getChecklistManager().addFragmentCheckList(clist));
                bundle.putString(ChecklistFragment.KEY_BUNDLE_PARENT_TYPE, ChecklistFragment.VALUE_BUNDLE_PARENT_STOCK);
                fragment.setArguments(bundle);


				ft.replace(R.id.main_layout, fragment);
				ft.addToBackStack(null);
                activity.notifyChangeFragment(fragment);
                ft.commit();
				return false;
			}

		});

		registerForContextMenu(listStock);
//		activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_STORE);
		activity.notifyChangeFragment(this);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		this.initView = pref.getString(SettingActivity.KEY_STOCK_INIT, null);
		if(initView.equals("close")){

		}else if(initView.equals("open")){
			for(int i=0;i<listStock.getExpandableListAdapter().getGroupCount();i++){
				listStock.expandGroup(i);
			}
		}else if(initView.equals("first_open")){
			listStock.expandGroup(0);
		}else{

		}
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){

			mCMenuHandler = ContextMenuHandler.getHandler(activity, this, Checklist.CHECKLIST_STORE);
			mCMenuHandler.prepareContextMenu(menu);

			//TODO グループを長押しした場合に、フォルダ編集画面に移動する
			//シングルタップをうまく無効化できず、実装見送り
//		}else if(type == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
//			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//			builder.setTitle(R.string.dialog_title_to_categoryedit);
//			builder.setMessage(R.string.dialog_message_to_categoryedit);
//			builder.setPositiveButton(R.string.dialog_button_to_categoryedit_yes, new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					activity.callCategoryEdit();
//				}
//			});
//			builder.setNegativeButton(R.string.dialog_button_to_categoryedit_no, new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					onContextMenuCanceled(ContextMenuHandler.CONTEXT_MENUID_DELETE);
//				}
//			});
//			builder.create().show();

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)item.getMenuInfo();
		int groupPos = 0;
		int childPos = 0;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){

			groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
			childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);

			mCMenuHandler.contextMenuSelected(item.getItemId(),
					(Checklist)mCLAdapter.getChild(groupPos, childPos));

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.getItem(1).setEnabled(false);
		menu.getItem(1).setIcon(R.drawable.ic_alpha);

		menu.getItem(2).setEnabled(false);
		menu.getItem(2).setIcon(R.drawable.ic_alpha);

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;

		//TODO 開発用ログ
		Log.d("checklistbox_dev_log", this.toString() + ":onAttach called");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO 開発用ログ
		Log.d("checklistbox_dev_log", this.toString() + ":onCreate called");
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mCLAdapter.refleshAdapter();
		this.listStock.invalidateViews();

		//TODO 開発用ログ
		Log.d("checklistbox_dev_log", this.toString() + ":onResume called");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onSaveInstanceState(outState);

		//TODO 開発用ログ
		Log.d("checklistbox_dev_log", this.toString() + ":onSaveInstanceState called");
	}

	@Override
	public void onContextMenuCanceled(int menuType) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onFinishContextMenu(int menuType) {
		mCLAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:

			mCMenuHandler = ContextMenuHandler.getHandler(activity, this, Checklist.CHECKLIST_STORE);
			mCMenuHandler.contextMenuSelected(ContextMenuHandler.MENU_CREATE, null );

			break;
		case MainActivity.MENU_MOVE_ID:
			//TODO チェックリストを移動可能にする？

			break;
		case MainActivity.MENU_SORT_ID:
			//TODO ソート順ダイアログ表示処理

			break;
		case MainActivity.MENU_SETTINGS_ID:
			//TODO 設定の場合はここでは処理しない？

			break;
//		default:
//			break;
		}

	}

	@Override
	public String getFragmenTitle() {
		return mFragmentTitle;
	}

	@Override
	public int getFragmentIconID() {
		return FRAGMENT_ICON_ID;
	}

	public int getFragmentPositionID(){
		return MainActivity.POS_FRAGMENT_STOCK;
	}

}
