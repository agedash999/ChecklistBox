package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

/**
 * チェックリストノード一覧画面のFragment
 */
public class ChecklistFragment extends Fragment
implements ChecklistBoxChildFragment{

	private MainActivity activity;
	private ChecklistBoxChildFragment parent;
	private View rootView;
	private ChecklistAdapter mCLAdapter;
	private DragSortListView mDslv;
	private DragSortController mController;
    private String mFragmentTitle;

	private Checklist mChecklist;

	private ListView listChecklist;
	private EditText etx_checklist_add;
	private TextView tv_checklist_title;
	private TextView tv_summery;

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;

//	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
//		@Override
//		public void remove(int which) {
//			mCLAdapter.remove(mCLAdapter.getItem(which));
//		}
//	};

	public DragSortController getController(){
		return mController;
	}

	public ChecklistFragment() {
		super();
	}

	public static ChecklistFragment newInstance
	(Checklist clist, ChecklistBoxChildFragment parent){
		ChecklistFragment instance = new ChecklistFragment();
		instance.mChecklist = clist;
		instance.parent = parent;
		instance.mFragmentTitle = parent.getFragmenTitle();
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//使用するView・Activityをフィールドに格納
		this.rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
		this.activity = (MainActivity)getActivity();
		this.mDslv = (DragSortListView)rootView.findViewById(R.id.list_checklist);
		//		this.listChecklist = (ListView)rootView.findViewById(R.id.list_checklist);

		this.tv_checklist_title = (TextView) rootView.findViewById(R.id.title_checklist);
		this.tv_summery = (TextView) rootView.findViewById(R.id.tv_summery);

		this.etx_checklist_add = (EditText)rootView.findViewById(R.id.etx_checklist_add);

		tv_checklist_title.setText(mChecklist.getTitle());
		
		

		//キーイベントリスナーの設定
		etx_checklist_add.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//TODO 後でコメント追加
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if(event.getAction() == KeyEvent.ACTION_UP){
						return true;
					}
					String nodeTitle = etx_checklist_add.getText().toString();
					if (nodeTitle != null && nodeTitle.length() != 0) {
						ChecklistNode node = new ChecklistNode(
								nodeTitle, false);
						activity.getChecklistManager().addNode(mChecklist, node);
						mCLAdapter.notifyDataSetChanged();
						etx_checklist_add.setText("");
					}
					return true;
				}
				return false;
			}
		});


		//Adapterのインスタンスを生成してListViewにセット
		mCLAdapter = new ChecklistAdapter(getActivity(), R.layout.listrow_checklist,
				mChecklist.getNodes());

		mDslv.setDropListener(mCLAdapter);
		//		listChecklist.setAdapter(mCLAdapter);//不要？
		mDslv.setAdapter(mCLAdapter);

		registerForContextMenu(mDslv);

//		mController = buildController(mDslv);//使わない

		mController = new SectionController(mDslv, mCLAdapter);
		mController.setDragHandleId(R.id.iv_drag_handle);
		mController.setSortEnabled(true);
		mController.setDragInitMode(DragSortController.ON_DOWN);
		mController.setRemoveEnabled(false);
		mController.setRemoveMode(DragSortController.FLING_REMOVE);

		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDropListener(mCLAdapter);
//		mDslv.setRemoveListener(onRemove); //不要？
		mDslv.setDragEnabled(true);

		registerForContextMenu(mDslv);

		//		Button btn = (Button)rootView.findViewById(R.id.button_test);
		//		registerForContextMenu(btn);


		activity.getChecklistManager().sortNode(mChecklist);
		activity.notifyChangeFragment(this);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.conmenu_home_title));
		menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
		menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final int contextIndex = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		final ChecklistNode contextNode = mCLAdapter.getItem(contextIndex);

		switch (item.getItemId()) {
		case CONTEXT_MENUID_EDIT:
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			final EditText editView = new EditText(activity);
			editView.setText(contextNode.getTitle());
			editView.setSelection(editView.getText().length());

			builder.setView(editView)
			.setTitle("テスト")
			.setPositiveButton("保存", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					contextNode.setTitle(editView.getText().toString());
					activity.getChecklistManager().nodeUpdated(mChecklist, contextNode);
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
			builder_del.setTitle(R.string.dialog_title_node_delete);
			builder_del.setMessage(R.string.dialog_message_node_delete);
			builder_del.setPositiveButton(R.string.dialog_button_node_delete, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDeleteNode(contextIndex);

				}
			});
			builder_del.setNegativeButton(R.string.dialog_button_node_cansel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					onDeleteNodeCanseled(contextIndex);
				}
			});
			builder_del.create().show();

			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);

	}


	private void activateAddView(){
		//追加エリアの可視化
		this.etx_checklist_add.setVisibility(View.VISIBLE);
		this.etx_checklist_add.setEnabled(true);
		this.etx_checklist_add.requestFocus();
		//入力フォーカスの設定
		InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(etx_checklist_add, 0);
	}

	private void inactivateAddView(){
		this.etx_checklist_add.setVisibility(View.GONE);
		this.etx_checklist_add.setEnabled(false);
	}

	@Override
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			if(!etx_checklist_add.isEnabled()){
				activateAddView();
			}else{
				inactivateAddView();
			}
			break;
		case MainActivity.MENU_MOVE_ID:
			mCLAdapter.changeMoveMode();
			mDslv.invalidateViews();
			//TODO チェックリストを移動可能にする？

			break;
		case MainActivity.MENU_SORT_ID:
			//TODO ソート順ダイアログ表示処理
			final ChecklistManager manager = activity.getChecklistManager();
			int sortType = manager.getNodeSortType();

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			final RadioButton radioSortNo = new RadioButton(activity);
			radioSortNo.setText(R.string.dialog_radio_sort_check_off);
			final RadioButton radioCheckDown = new RadioButton(activity);
			radioCheckDown.setText(R.string.dialog_radio_sort_check_down);
			final RadioGroup group = new RadioGroup(activity);
			group.addView(radioSortNo);
			group.addView(radioCheckDown);

			if(sortType == ChecklistManager.SORTTYPE_SORTNO){
				group.check(radioSortNo.getId());
			}else if(sortType == ChecklistManager.SORTTYPE_SORTNO_CHECKED){
				group.check(radioCheckDown.getId());
			}

			builder.setView(group)
			.setTitle("テスト")
			.setPositiveButton("変更", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					int checked = group.getCheckedRadioButtonId();
					boolean section = false;
					if(checked == radioSortNo.getId()){
						manager.setNodeSortType(ChecklistManager.SORTTYPE_SORTNO);
						section = false;
					}else if(checked == radioCheckDown.getId()){
						manager.setNodeSortType(ChecklistManager.SORTTYPE_SORTNO_CHECKED);
						section = true;
					}
					manager.sortNode(mChecklist);
					mCLAdapter.notifyDataSetChanged();
					mCLAdapter.enableSection(section);
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

	private void onDeleteNode(int nodeIndex){
		mChecklist.getChecklist().remove(nodeIndex);
		mCLAdapter.enableSection(true);
		mCLAdapter.notifyDataSetChanged();
	}

	private void onDeleteNodeCanseled(int nodeIndex){

	}

	@Override
	public void onResume() {
		super.onResume();
		this.tv_summery.setText(getSummery());
	}

	@Override
	public String getFragmenTitle() {
		return mFragmentTitle;
	}

	@Override
	public int getFragmentIconID() {
		return parent.getFragmentIconID();
	}


	private String getSummery(){

		String summery = null;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
		String summeryType;
		String dateString = "";

		switch (mChecklist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			// TODO いったん初期値を設定（本来はXMLから読み込みたい）
			summeryType = pref.getString(SettingActivity.KEY_VIEWITEM_HOME, "date");
			dateString = activity.getString(R.string.checklist_row_expdate);
			break;

		case Checklist.CHECKLIST_STORE:
			// TODO いったん初期値を設定（本来はXMLから読み込みたい）
			summeryType = pref.getString(SettingActivity.KEY_VIEWITEM_STOCK, "date");
			dateString = activity.getString(R.string.checklist_row_credate);
			break;

		case Checklist.CHECKLIST_HISTORY:
			// TODO いったん初期値を設定（本来はXMLから読み込みたい）
			summeryType = pref.getString(SettingActivity.KEY_VIEWITEM_HISTORY, "date");
			dateString = activity.getString(R.string.checklist_row_findate);
			break;

		default:
			summeryType = "date";
			break;
		}

		if(summeryType.equals("date")){
			summery = dateString + mChecklist.getDateFormated();
		}else if(summeryType.equals("node")){
			if(mChecklist.getType() == Checklist.CHECKLIST_RUNNING){
				summery = activity.getString(R.string.checklist_row_node_fin_qty) +
						mChecklist.getUncheckedQty() + "/" + mChecklist.getNodeQty();
			}else{
				summery = activity.getString(R.string.checklist_row_nodeqty) +
						mChecklist.getNodeQty();
			}
		}else if(summeryType.equals("memo")){
			summery = mChecklist.getMemo();
		}

		return summery;
	}

//	public String getFragmenSubTitle() {
//		return mChecklist.getTitle();
//	}


	/**
	 * チェックリストノード一覧画面のAdapter
	 */
	class ChecklistAdapter extends ArrayAdapter<ChecklistNode> implements DragSortListView.DropListener {

		private final static int SECTION_DIV = 0;
		private final static int SECTION_ONE = 1;
		private final static int SECTION_TWO = 2;

		private Context context;
		private List<ChecklistNode> mList;
		private LayoutInflater mInflater;
		private int mLayout;
		private int mDivLayout;
		private boolean enableSection = false;
		private int mDivPos;

		private boolean moveMode = false;

		public ChecklistAdapter(Context context, int resource, List<ChecklistNode> objects) {
			super(context, resource, objects);
			this.context = context;
			this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mLayout = resource;
			this.mDivLayout = R.layout.listrow_section_div;
			this.mList = objects;
			//TODO テスト的に指定
			//			mDivPos = mList.size() /2 ;
			if(activity.getChecklistManager().getNodeSortType()
					== ChecklistManager.SORTTYPE_SORTNO_CHECKED){
				//TODO ちょっと止めとく
				enableSection = true;
//				enableSection = false;
				//仕切りのIndexを設定
				mDivPos = mChecklist.getUncheckedQty();
			}else{
				enableSection = false;
				mDivPos = -1;
				//				mDivPos = mChecklist.getChecklist().size();
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			final int vType = getItemViewType(position);
			View view = convertView;
			final int dataPosition = dataPosition(position);
			ChecklistNode cnode;
			if(vType == SECTION_DIV){
				//TODO Divのレイアウトを設定
				view = mInflater.inflate(mDivLayout, null);
				return view;
//			}else if(view == null){
			}else{
				view = mInflater.inflate(mLayout, null);
			}
//			cnode = mList.get(position);
			cnode = mList.get(dataPosition);
			TextView etx_title_node = (TextView)view.findViewById(R.id.txv_title_node);
			etx_title_node.setText(cnode.getTitle());

			if(moveMode){
				((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.VISIBLE);
				((CheckBox)view.findViewById(R.id.cbx_checked)).setVisibility(View.GONE);
			}else{
				((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.GONE);
				((CheckBox)view.findViewById(R.id.cbx_checked)).setVisibility(View.VISIBLE);
				view.setLongClickable(true);
			}

			//ChecklisNodeクリック時の動作（タイトル編集UIの表示）
//			etx_title_node.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//					TextView txv_position = (TextView)((View)v.getParent()).findViewById(R.id.txv_position_hide);
//					final int nodePosition = Integer.parseInt(txv_position.getText().toString());
//
//					final EditText editView = new EditText(activity);
//					editView.setText(mChecklist.getNodes().get(nodePosition).getTitle());
//					editView.setSelection(editView.getText().length());
//
//					//					final TextView hide = new TextView(activity);
//					//					hide.setVisibility(View.GONE);
//					//					TextView txv_position = (TextView)((View)v.getParent()).findViewById(R.id.txv_position_hide);
//					//					hide.setText(txv_position.getText());
//
//					builder.setView(editView)
//					.setTitle("テスト")
//					.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//							ChecklistNode node = mChecklist.getNodes().get(nodePosition);
//							node.setTitle(editView.getText().toString());
//							activity.getChecklistManager().nodeUpdated(mChecklist, node);
//							notifyDataSetChanged();
//						}
//					})
//					.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//							// TODO 自動生成されたメソッド・スタブ
//						}
//					})
//					.show();
//				}
//			});

			((TextView)view.findViewById(R.id.txv_position_hide)).setText(Integer.toString(position));
			//TODO こっちに修正する予定
			//			((TextView)view.findViewById(R.id.txv_position_hide)).setText(Integer.toString(cnode.getID()));
			CheckBox cbx_checked = (CheckBox)view.findViewById(R.id.cbx_checked);
			cbx_checked.setChecked(cnode.isChecked());
			cbx_checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					TextView txv_position = (TextView)((View)buttonView.getParent()).findViewById(R.id.txv_position_hide);
					int nodePosition = Integer.parseInt(txv_position.getText().toString());
					ChecklistNode node = mChecklist.getNodes().get(dataPosition(nodePosition));
					node.setChecked(isChecked);
					activity.getChecklistManager().nodeUpdated(mChecklist, node);
					activity.getChecklistManager().sortNode(mChecklist);
					mDivPos = mChecklist.getUncheckedQty();
					mCLAdapter.notifyDataSetChanged();
				}
			});
			return view;

		}

		@Override
		public void drop(int from, int to) {
			if (from != to) {
				ChecklistNode node = mCLAdapter.getItem(from);

				activity.getChecklistManager().moveNode(mChecklist, node, dataPosition(to));
				mCLAdapter.notifyDataSetChanged();

			}
		}

		public void changeMoveMode(){
			moveMode = (!moveMode);
		}

//		@Override
//		public void drop(int from, int to) {
//			if (from != to) {
//				ChecklistNode data = mList.remove(dataPosition(from));
//				mList.add(dataPosition(to), data);
//				notifyDataSetChanged();
//			}
//		}

		@Override
		public int getCount() {
			if(enableSection){
				return mList.size() + 1;
			}else{
				return mList.size();
			}
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			if(enableSection){
				return position != mDivPos;
			}else{
				return true;
			}
		}

		public int getDivPosition() {
			return mDivPos;
		}

		@Override
		public int getViewTypeCount() {
			if(enableSection){
				return 3;
			}else{
				return 1;
			}
		}

		@Override
		public ChecklistNode getItem(int position) {
			if (position == mDivPos) {
				//TODO null返して大丈夫？
				return null;
			} else {
				return mList.get(dataPosition(position));
			}
		}

		@Override
		public long getItemId(int position) {
			//TODO 用途不明
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			if(!enableSection) return SECTION_ONE;
			if (position == mDivPos) {
				return SECTION_DIV;
			} else if (position < mDivPos) {
				return SECTION_ONE;
			} else {
				return SECTION_TWO;
			}
		}

		public int dataPosition(int position) {
			if(enableSection){
				return position > mDivPos ? position - 1 : position;
			}else{
				return position;
			}
		}

		//        public Drawable getBGDrawable(int type) {
		//            Drawable d;
		//            if (type == SECTION_ONE) {
		//                d = getResources().getDrawable(R.drawable.bg_handle_section1_selector);
		//            } else {
		//                d = getResources().getDrawable(R.drawable.bg_handle_section2_selector);
		//            }
		//            d.setLevel(3000);
		//            return d;
		//        }
		public void enableSection(boolean isEnable){
			if(isEnable){
				mDivPos = mChecklist.getUncheckedQty();
			}else{
				mDivPos = -1;
			}
			enableSection = isEnable;
		}

	}

	private class SectionController extends DragSortController{

		private int mPos;
//		private int mDivPos;
		private ChecklistAdapter mAdapter;
	    private int mDragHandleId;

		private DragSortListView mDslv;


		public SectionController(DragSortListView dslv, ChecklistAdapter adapter){
			super(dslv, 0, DragSortController.ON_DOWN, 0);
			setRemoveEnabled(false);
			mDslv = dslv;
			mAdapter = adapter;
//			mDivPos = adapter.getDivPosition();
		}

		@Override
		public int startDragPosition(MotionEvent ev){
            int res = super.dragHandleHitPosition(ev);
            if (res == mAdapter.getDivPosition()) {
                return DragSortController.MISS;
            }
            return res;

//            ドラッグハンドルがsampleと異なるので削除
//            int width = mDslv.getWidth();
//
//            if ((int) ev.getX() < width / 3) {
//                return res;
//            } else {
//                return DragSortController.MISS;
//            }
		}

        @Override
        public View onCreateFloatView(int position) {

//            return super.onCreateFloatView(position);


            mPos = position;

            View v = mAdapter.getView(position, null, mDslv);
            if (position < mAdapter.getDivPosition()) {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_handle_section1));
            } else {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_handle_section2));
            }
            v.getBackground().setLevel(10000);
            return v;
        }

        private int origHeight = -1;

        @Override
        public void onDragFloatView(View floatView, Point floatPoint, Point touchPoint) {

//        	super.onDragFloatView(floatView, floatPoint, touchPoint);

            final int first = mDslv.getFirstVisiblePosition();
            final int lvDivHeight = mDslv.getDividerHeight();

            if (origHeight == -1) {
                origHeight = floatView.getHeight();
            }

            View div = mDslv.getChildAt(mAdapter.getDivPosition() - first);

            if (touchPoint.x > mDslv.getWidth() / 2) {
                float scale = touchPoint.x - mDslv.getWidth() / 2;
                scale /= (float) (mDslv.getWidth() / 5);
                ViewGroup.LayoutParams lp = floatView.getLayoutParams();
                lp.height = Math.max(origHeight, (int) (scale * origHeight));
//                Log.d("mobeta", "setting height "+lp.height);
                floatView.setLayoutParams(lp);
            }

            if (div != null) {
                if (mPos > mAdapter.getDivPosition()) {
                    // don't allow floating View to go above
                    // section divider
                    final int limit = div.getBottom() + lvDivHeight;
                    if (floatPoint.y < limit) {
                        floatPoint.y = limit;
                    }
                } else {
                    // don't allow floating View to go below
                    // section divider
                    final int limit = div.getTop() - lvDivHeight - floatView.getHeight();
                    if (floatPoint.y > limit) {
                        floatPoint.y = limit;
                    }
                }
            }
        }

        @Override
        public void onDestroyFloatView(View floatView) {
            //do nothing; block super from crashing
        }
	}
}
