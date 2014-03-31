package jp.agedash999.android.checklistbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeFragment extends Fragment
implements ChecklistDialog.ChecklistDialogListener{

	private MainActivity activity;
	private View rootView;
	private ListView listHome;
	private ChecklistListAdapter mCLAdapter;

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;
	private final int CONTEXT_MENUID_STOCK = 2;

	private int contextMenuIndex;

	public HomeFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_home, container, false);

		listHome = (ListView)rootView.findViewById(R.id.list_home);

		mCLAdapter = new ChecklistListAdapter(getActivity(), R.layout.listrow_home,
				activity.getChecklistManager().getRunningList(),ChecklistListAdapter.DIALOG_HOME);
		listHome.setAdapter(mCLAdapter);
		listHome.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ListView listView = (ListView) parent;
                Checklist clist = (Checklist)listView.getItemAtPosition(position);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = ChecklistFragment.newInstance(clist);
                ft.replace(R.id.main_layout, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

		registerForContextMenu(listHome);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.conmenu_home_title));
		menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
		menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
		menu.add(0, CONTEXT_MENUID_STOCK, 0, R.string.conmenu_stock);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		this.contextMenuIndex = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		Checklist clist = mCLAdapter.getChecklist(contextMenuIndex);
		ChecklistDialog dialog;
		switch (item.getItemId()) {
		case CONTEXT_MENUID_EDIT:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_HOME_EDIT, clist);
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
					onDeleteChecklist(contextMenuIndex);
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
		case CONTEXT_MENUID_STOCK:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_HOME_STORE, clist);
			dialog.setChecklistDialogListener(this);
			dialog.show(getFragmentManager(), "store");
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onChecklistInfoSave(Checklist clist, int dialogType) {
		//チェックリスト新規作成・更新・保存　いずれの場合も同様
		switch (dialogType) {
		case ChecklistDialog.FOR_HOME_NEW:
			activity.getChecklistManager().insertChecklist(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_HOME_EDIT:
			activity.getChecklistManager().updateChecklistInfo(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_HOME_STORE:
			activity.getChecklistManager().insertChecklist(clist);
			mCLAdapter.notifyDataSetChanged();
			break;
//		default:
//			break;
		}
	}

	@Override
	public void onChecklistInfoCansel() {

	}

	private void onDeleteChecklist(int index){
		activity.getChecklistManager().removeChecklist(Checklist.CHECKLIST_RUNNING ,index, 0);
		mCLAdapter.notifyDataSetChanged();
	}

	private void onDeleteCanseled(){

	}
}
