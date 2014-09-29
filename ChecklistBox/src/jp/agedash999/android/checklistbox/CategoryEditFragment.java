package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class CategoryEditFragment extends Fragment
implements ChecklistBoxChildFragment {

	private MainActivity activity;
	private View rootView;
	private CategoryListAdapter mCLAdapter;
    private DragSortListView mDslv;
    private DragSortController mController;
    private String mFragmentTitle;

    private final int FRAGMENT_TITLE_ID = MainActivity.TITLE_CATEGORYEDIT_ID;

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;

	private final int idDeleteDialogLayout = R.layout.dialog_category_delete;
	private final int idDeleteDialogTitle = R.string.dialog_title_category_delete;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                ChecklistCategory category = mCLAdapter.getItem(from);

                activity.getChecklistManager().moveCategory(category, to);
                mCLAdapter.notifyDataSetChanged();

                //TODO テスト用
                activity.getChecklistManager().testCategoryFieldOutput();
            }
        }
    };

    public DragSortController getController() {
        return mController;
    }

	public CategoryEditFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//使用するView・Activityをフィールドに格納
		this.rootView = inflater.inflate(R.layout.fragment_categoryedit, container, false);
		this.mDslv = (DragSortListView)rootView.findViewById(R.id.list_category);
		this.mFragmentTitle = activity.getResources().getString(FRAGMENT_TITLE_ID);

		//Adapterのインスタンスを生成してListViewにセット
		mCLAdapter = new CategoryListAdapter(getActivity(), R.layout.listrow_category,
				activity.getChecklistManager().getStockList());

		mDslv.setAdapter(mCLAdapter);

		registerForContextMenu(mDslv);
		mController = buildController(mDslv);
		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDropListener(onDrop);
		mDslv.setDragEnabled(true);

		activity.getChecklistManager().sortCategory();
		activity.notifyChangeFragment(this);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//TODO あとでstringを修正　※Nodeの同じ部分も
		menu.setHeaderTitle(getString(R.string.conmenu_home_title));
		menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
		menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//TODO ここから下は未実装　HomeFragmentを参考に実装する
		final int contextIndex = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		final ChecklistCategory contextCategory = mCLAdapter.getItem(contextIndex);

		switch(item.getItemId()){
		case CONTEXT_MENUID_EDIT:
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			final EditText editView = new EditText(activity);
			editView.setText(contextCategory.getTitle());
			editView.setSelection(editView.getText().length());

			//TODO 文字列外部化
			builder.setView(editView)
			.setTitle("テスト")
			.setPositiveButton("保存", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					contextCategory.setTitle(editView.getText().toString());
					activity.getChecklistManager().updateCategoryInfo(contextCategory);
					mCLAdapter.notifyDataSetChanged();
				}
			})
			.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO 自動生成されたメソッド・スタブ
				}
			})
			.show();

			break;

		case CONTEXT_MENUID_DELETE:
			AlertDialog.Builder builder_del = new AlertDialog.Builder(activity);
			View view = activity.getLayoutInflater().inflate(idDeleteDialogLayout, null);

			//EditTextにカテゴリタイトルを設定・編集不可に
			EditText categoryTitle = (EditText)view.findViewById(R.id.edt_category_title);
			categoryTitle.setText(contextCategory.getTitle());
			categoryTitle.setFocusable(false);


			final Spinner spinner = (Spinner)view.findViewById(R.id.spn_category);
			spinner.setAdapter(new ArrayAdapter<ChecklistCategory>(
					activity, android.R.layout.simple_spinner_item,
					activity.getChecklistManager().getStockList()));

			final TextView tv_spinner = (TextView)view.findViewById(R.id.tv_label_category);

			final CheckBox cbx = (CheckBox)view.findViewById(R.id.cbx_delete);

			cbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						spinner.setVisibility(View.GONE);
						tv_spinner.setVisibility(View.GONE);
					}else{
						spinner.setVisibility(View.VISIBLE);
						tv_spinner.setVisibility(View.VISIBLE);
					}
				}
			});

			cbx.setChecked(true);
			spinner.setVisibility(View.GONE);
			tv_spinner.setVisibility(View.GONE);

			builder_del.setView(view)
			.setTitle(idDeleteDialogTitle)
			.setPositiveButton(R.string.dialog_button_category_delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					if(!cbx.isChecked()){
						activity.getChecklistManager().moveChecklistToCategory(
								contextCategory,
								activity.getChecklistManager().getStockList().get(spinner.getSelectedItemPosition()));
					}
					activity.getChecklistManager().deleteCategory(contextCategory);
					mCLAdapter.notifyDataSetChanged();
				}
			})
			.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO 自動生成されたメソッド・スタブ
				}
			})
			.show();

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
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			final EditText editView = new EditText(activity);
			editView.setSelection(editView.getText().length());

			//TODO 文字列外部化
			builder.setView(editView)
			.setTitle("テスト")
			.setPositiveButton("保存", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					ChecklistCategory newCategory =
							new ChecklistCategory(editView.getText().toString());
					activity.getChecklistManager().insertCategory(newCategory);
					mCLAdapter.notifyDataSetChanged();
				}
			})
			.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					// TODO 自動生成されたメソッド・スタブ
				}
			})
			.show();

			break;

		case MainActivity.MENU_MOVE_ID:
			mCLAdapter.changeMoveMode();
			mDslv.invalidateViews();
			//TODO チェックリストを移動可能にする？

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
		return 0;
	}
}
