package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.ContextMenuHandler.ContextMenuFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class HomeFragment extends AbstractChildFragment
implements ContextMenuFragment{

	private static AbstractChildFragment mInstance = new HomeFragment();

	private MainActivity activity;
	private View rootView;
	private HomeListAdapter mCLAdapter;
    private DragSortListView mDslv;
    private DragSortController mController;
    private String mFragmentTitle;
    private ContextMenuHandler mCMenuHandler;

    private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_HOME_ID;
    private final int FRAGMENT_ICON_ID = MainActivity.ICON_TITLE_HOME_ID;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                Checklist clist = mCLAdapter.getItem(from);

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

	public static AbstractChildFragment newInstance(){
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//使用するView・Activityをフィールドに格納
		this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
		this.mDslv = (DragSortListView)rootView.findViewById(R.id.list_home);
		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

		//Adapterのインスタンスを生成してListViewにセット
		mCLAdapter = new HomeListAdapter(getActivity(), R.layout.listrow_home,
				activity.getChecklistManager().getRunningList());

		mDslv.setAdapter(mCLAdapter);
		mDslv.setOnItemClickListener(new OnItemClickListener() {
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
//                activity.notifyChangeFragment(fragment);
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
		activity.notifyChangeFragment(this);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		mCMenuHandler = ContextMenuHandler.getHandler(activity, this, Checklist.CHECKLIST_RUNNING);
		mCMenuHandler.prepareContextMenu(menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		mCMenuHandler.contextMenuSelected(item.getItemId(),
				mCLAdapter.getChecklist(((AdapterContextMenuInfo)item.getMenuInfo()).position));

		return super.onContextItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mCLAdapter.refleshAdapter();
		this.mDslv.invalidateViews();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

			mCMenuHandler = ContextMenuHandler.getHandler(activity, this, Checklist.CHECKLIST_RUNNING);
			mCMenuHandler.contextMenuSelected(ContextMenuHandler.MENU_CREATE, null );

			break;
		case MainActivity.MENU_MOVE_ID:
			mCLAdapter.changeMoveMode();
			mDslv.invalidateViews();
			//TODO チェックリストを移動可能にする？

			break;
		case MainActivity.MENU_SORT_ID:
			//TODO ソート順ダイアログ表示処理
			final ChecklistManager manager = activity.getChecklistManager();
			int sortType = manager.getChecklistSortType(Checklist.CHECKLIST_RUNNING);

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
						manager.setChecklistSortType(
								Checklist.CHECKLIST_RUNNING, ChecklistManager.SORTTYPE_SORTNO);
					}else if(checked == radioDateAsc.getId()){
						manager.setChecklistSortType(
								Checklist.CHECKLIST_RUNNING, ChecklistManager.SORTTYPE_DATE_ASC);
					}else if(checked == radioDateDesc.getId()){
						manager.setChecklistSortType(
								Checklist.CHECKLIST_RUNNING, ChecklistManager.SORTTYPE_DATE_DESC);
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
		return MainActivity.POS_FRAGMENT_HOME;
	}


}
