package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;

public class StockFragment extends Fragment
	implements ChecklistDialog.ChecklistDialogListener
	,ChecklistBoxChildFragment {

	private ChecklistBoxChildFragment mInstance;

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;
	private final int CONTEXT_MENUID_TOHOME = 3;

	private MainActivity activity;
	private View rootView;
	private ExpandableListView listStock;
	private StockListAdapter mCLAdapter;
	private ChecklistManager mCLManager;
    private String mFragmentTitle;

    private String initView;

    private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_STOCK_ID;
    private final int FRAGMENT_ICON_ID = MainActivity.ICON_STOCK_ID;

//	private int contextIndex;
	private Checklist contextChecklist;

	public static final String KEY_TITLE = "title";
	public static final String KEY_CATEGORY_ID = "category_id";

	public StockFragment() {
		super();
	}

	public static ChecklistBoxChildFragment newInstance(){
		StockFragment instance = new StockFragment();
		instance.mInstance = instance;
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_stock, container, false);
		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

		listStock = (ExpandableListView)rootView.findViewById(R.id.list_stock);

		//下位（Manager）との疎結合性を高めるため、渡されたList/Mapをここで格納し直す
		//TODO 格納し直すと、オブジェクト管理が複雑になるので要検討
//		List<String> categoryList = new ArrayList<String>();
//		List<List<Checklist>> stockList = new ArrayList<List<Checklist>>();
//		mCLManager.getStockAndCategory(categoryList, stockList);

		List<ChecklistCategory> stockList = mCLManager.getStockList();


		mCLAdapter = new StockListAdapter(getActivity(), stockList);
		listStock.setAdapter(mCLAdapter);

		listStock.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Checklist clist = (Checklist)parent.getExpandableListAdapter()
						.getChild(groupPosition, childPosition);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ChecklistFragment fragment = ChecklistFragment.newInstance(clist,mInstance);
				ft.replace(R.id.main_layout, fragment);
				ft.addToBackStack(null);
                activity.notifyChangeFragment(fragment);
                ft.commit();
				return false;
			}

		});

		registerForContextMenu(listStock);
		activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_STORE);
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

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			menu.setHeaderTitle(getString(R.string.conmenu_home_title));
			menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
			menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
			menu.add(0, CONTEXT_MENUID_TOHOME, 0, R.string.conmenu_tohome);
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
		}

		this.contextChecklist = (Checklist)mCLAdapter.getChild(groupPos, childPos);
		ChecklistDialog dialog;
		switch (item.getItemId()) {
		case CONTEXT_MENUID_EDIT:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_STOCK_EDIT, contextChecklist);
			dialog.setChecklistDialogListener(this);
			dialog.show(getFragmentManager(), "edit");
			break;
		case CONTEXT_MENUID_DELETE:
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(R.string.dialog_title_clist_delete_home);
			builder.setMessage(R.string.dialog_message_clist_delete);
			builder.setPositiveButton(R.string.dialog_button_clist_delete, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDeleteChecklist(contextChecklist);
				}
			});
			builder.setNegativeButton(R.string.dialog_button_clist_cansel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDeleteCanseled();
				}
			});
			builder.create().show();

			break;
		case CONTEXT_MENUID_TOHOME:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_STOCK_TOHOME, contextChecklist);
			dialog.setChecklistDialogListener(this);
			dialog.show(getFragmentManager(), "tohome");
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
		this.mCLManager = this.activity.getChecklistManager();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mCLAdapter.refleshAdapter();
		this.listStock.invalidateViews();
	}

	@Override
	public void onChecklistInfoSave(Checklist clist, int dialogType) {
		//チェックリスト新規作成・更新・保存　いずれの場合も同様
		switch (dialogType) {
		case ChecklistDialog.FOR_STOCK_NEW:
			activity.getChecklistManager().insertChecklist(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_STOCK_EDIT:
			activity.getChecklistManager().updateChecklistInfo(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_STOCK_TOHOME:
			activity.getChecklistManager().insertChecklist(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
//		default:
//			break;
		}
	}

	@Override
	public void onChecklistInfoCansel() {
		// TODO 自動生成されたメソッド・スタブ

	}

	private void onDeleteChecklist(Checklist clist){
		activity.getChecklistManager().removeChecklist(clist);
		mCLAdapter.notifyDataSetChanged();
	}

	private void onDeleteCanseled(){

	}

	@Override
	public void onClickMenu(int menuId) {
		ChecklistDialog dialog;
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			dialog = ChecklistDialog.getDialogBlank(ChecklistDialog.FOR_STOCK_NEW);
			dialog.setChecklistDialogListener(this);
			dialog.show(getFragmentManager(), "create");
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

}
