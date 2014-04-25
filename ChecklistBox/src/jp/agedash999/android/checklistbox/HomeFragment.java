package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class HomeFragment extends Fragment
implements ChecklistDialog.ChecklistDialogListener
,ChecklistBoxChildFragment {

	private MainActivity activity;
	private View rootView;
//	private ListView listHome;
	private HomeListAdapter mCLAdapter;
    private DragSortListView mDslv;
    private DragSortController mController;

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;
	private final int CONTEXT_MENUID_STOCK = 2;

	private int contextIndex;
	private Checklist contextChecklist;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                Checklist clist = mCLAdapter.getItem(from);

                List<Checklist> test = activity.getChecklistManager().getRunningList();

                activity.getChecklistManager().moveRunningList(clist, to);

                //方法１
//              mCLAdapter.remove(clist);
//              mCLAdapter.insert(clist, to);

                //方法２
                mCLAdapter.notifyDataSetChanged();

                //方法３
//                mDslv.invalidateViews()

            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
        	mCLAdapter.remove(mCLAdapter.getItem(which));
        }
    };

    public DragSortController getController() {
        return mController;
    }

	public HomeFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_home, container, false);

//		listHome = (ListView)rootView.findViewById(R.id.list_home);
		mDslv = (DragSortListView)rootView.findViewById(R.id.list_home);

		mCLAdapter = new HomeListAdapter(getActivity(), R.layout.listrow_home,
				activity.getChecklistManager().getRunningList(),HomeListAdapter.DIALOG_HOME);
		mDslv.setAdapter(mCLAdapter);
		mDslv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ListView listView = (ListView) parent;
                Checklist clist = (Checklist)listView.getItemAtPosition(position);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ChecklistFragment fragment = ChecklistFragment.newInstance(clist);
                ft.replace(R.id.main_layout, fragment);
                ft.addToBackStack(null);
                ft.commit();
                activity.notifyChangeFragment(fragment);
            }
        });

		registerForContextMenu(mDslv);
		mController = buildController(mDslv);
		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDropListener(onDrop);
		mDslv.setRemoveListener(onRemove);
		mDslv.setDragEnabled(true);

		activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_RUNNING);

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
		this.contextIndex = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		this.contextChecklist = mCLAdapter.getChecklist(contextIndex);
		ChecklistDialog dialog;
		switch (item.getItemId()) {
		case CONTEXT_MENUID_EDIT:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_HOME_EDIT, contextChecklist);
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
		case CONTEXT_MENUID_STOCK:
			dialog = ChecklistDialog.getDialog(ChecklistDialog.FOR_HOME_STORE, contextChecklist);
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
			activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_RUNNING);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_HOME_EDIT:
			activity.getChecklistManager().updateChecklistInfo(clist);
			activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_RUNNING);
			mCLAdapter.notifyDataSetChanged();
			break;
		case ChecklistDialog.FOR_HOME_STORE:
			activity.getChecklistManager().insertChecklist(clist);
			mCLAdapter.notifyDataSetChanged();
			//TODO 画面を写す？
			break;
//		default:
//			break;
		}
	}

	@Override
	public void onChecklistInfoCansel() {

	}

	private void onDeleteChecklist(Checklist clist){
		activity.getChecklistManager().removeChecklist(clist);
		mCLAdapter.notifyDataSetChanged();
	}

	private void onDeleteCanseled(){

	}

	@Override
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			ChecklistDialog dialog;
			dialog = ChecklistDialog.getDialogBlank(ChecklistDialog.FOR_HOME_NEW);
			dialog.setChecklistDialogListener(this);
			dialog.show(getFragmentManager(), "create");
			break;
		case MainActivity.MENU_MOVE_ID:
			mCLAdapter.changeMoveMode();
			mDslv.invalidateViews();
			//TODO チェックリストを移動可能にする？

			break;
		case MainActivity.MENU_SORT_ID:
			//TODO ソート順ダイアログ表示処理
			final ChecklistManager manager = activity.getChecklistManager();
			int sortType = manager.getRunninglistSortType();

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			final RadioButton radioSortNo = new RadioButton(activity);
			radioSortNo.setText(R.string.dialog_radio_sort_sortno);
			final RadioButton radioDateAsc = new RadioButton(activity);
			radioDateAsc.setText(R.string.dialog_radio_sort_expdate_asc);
			final RadioButton radioDateDesc = new RadioButton(activity);
			radioDateDesc.setText(R.string.dialog_radio_sort_expdate_desc);
			final RadioGroup group = new RadioGroup(activity);
			group.addView(radioSortNo);
			group.addView(radioDateAsc);
			group.addView(radioDateDesc);

			if(sortType == ChecklistManager.SORTTYPE_SORTNO){
				group.check(radioSortNo.getId());
			}else if(sortType == ChecklistManager.SORTTYPE_DATE_ASC){
				group.check(radioDateAsc.getId());
			}else if(sortType == ChecklistManager.SORTTYPE_DATE_DESC){
				group.check(radioDateDesc.getId());
			}

			builder.setView(group)
			.setTitle("テスト")
			.setPositiveButton("変更", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					int checked = group.getCheckedRadioButtonId();
					if(checked == radioSortNo.getId()){
						manager.setRunninglistSortType(ChecklistManager.SORTTYPE_SORTNO);
					}else if(checked == radioDateAsc.getId()){
						manager.setRunninglistSortType(ChecklistManager.SORTTYPE_DATE_ASC);
					}else if(checked == radioDateDesc.getId()){
						manager.setRunninglistSortType(ChecklistManager.SORTTYPE_DATE_DESC);
					}
					manager.sortChecklist(Checklist.CHECKLIST_RUNNING);
					mDslv.invalidateViews();
				}
			})
			.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ

				}
			})
			.show();


			break;
		case MainActivity.MENU_SETTINGS_ID:
			//TODO 設定の場合はここでは処理しない？

			break;
//		default:
//			break;
		}
	}

	public DragSortController buildController(DragSortListView dslv) {
        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.iv_drag_handle);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);

        return controller;
    }
}
