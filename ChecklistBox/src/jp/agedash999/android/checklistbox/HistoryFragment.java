package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.ContextMenuHandler.ContextMenuFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryFragment extends AbstractChildFragment
implements ContextMenuFragment{

	private static AbstractChildFragment mInstance = new HistoryFragment();

	private MainActivity activity;
	private View rootView;
	private ListView listHistory;
	private HistoryListAdapter mCLAdapter;
    private String mFragmentTitle;
    private ContextMenuHandler mCMenuHandler;

    private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_HISTORY_ID;
    private final int FRAGMENT_ICON_ID = MainActivity.ICON_TITLE_HISTORY_ID;

	public HistoryFragment() {
		super();
	}

	public static AbstractChildFragment newInstance(){
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_history, container, false);
		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

		listHistory = (ListView)rootView.findViewById(R.id.list_history);

		mCLAdapter = new HistoryListAdapter(getActivity(), R.layout.listrow_history,
				activity.getChecklistManager().getHistoryList());
		listHistory.setAdapter(mCLAdapter);
		listHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listView = (ListView) parent;
				Checklist clist = (Checklist)listView.getItemAtPosition(position);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ChecklistFragment fragment = ChecklistFragment.newInstance(clist, mInstance);
				ft.replace(R.id.main_layout, fragment);
				ft.addToBackStack(null);
				ft.commit();
				activity.notifyChangeFragment(fragment);
			}

		});

		registerForContextMenu(listHistory);
		activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_HISTORY);
		activity.notifyChangeFragment(this);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		mCMenuHandler = ContextMenuHandler.getHandler(activity, this, Checklist.CHECKLIST_HISTORY);
		mCMenuHandler.prepareContextMenu(menu);
}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		mCMenuHandler.contextMenuSelected(item.getItemId(),
				mCLAdapter.getChecklist(((AdapterContextMenuInfo)item.getMenuInfo()).position));

		return super.onContextItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.getItem(0).setEnabled(false);
		menu.getItem(0).setIcon(R.drawable.ic_alpha);
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mCLAdapter.refleshAdapter();
		this.listHistory.invalidateViews();
	}


	@Override
	public void onContextMenuCanceled(int menuType){

	}

	@Override
	public void onFinishContextMenu(int menuType) {
		mCLAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			//TODO ここには入らない

			break;
		case MainActivity.MENU_MOVE_ID:
			//TODO ここには入らない

			break;
		case MainActivity.MENU_SORT_ID:
			//TODO ソート順ダイアログ表示処理

			break;
		case MainActivity.MENU_SETTINGS_ID:
			//TODO ここには入らない

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

	@Override
	public int getFragmentPositionID() {
		return MainActivity.POS_FRAGMENT_HISTORY;
	}

}
