package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
	private View rootView;
	private ChecklistAdapter mCLAdapter;
	private DragSortListView mDslv;
	private DragSortController mController;

	private Checklist mChecklist;

	private ListView listChecklist;
	private EditText etx_checklist_add;

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

	public static ChecklistFragment newInstance(Checklist clist){
		ChecklistFragment instance = new ChecklistFragment();
		instance.mChecklist = clist;
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
		this.etx_checklist_add = (EditText)rootView.findViewById(R.id.etx_checklist_add);

		//Adapterのインスタンスを生成してListViewにセット
		mCLAdapter = new ChecklistAdapter(getActivity(), R.layout.listrow_checklist,
				mChecklist.getNodes());

		mDslv.setDropListener(mCLAdapter);
		//		listChecklist.setAdapter(mCLAdapter);
		mDslv.setAdapter(mCLAdapter);

		registerForContextMenu(mDslv);

//		mController = buildController(mDslv);

		mController = new SectionController(mDslv, mCLAdapter);
		mController.setDragHandleId(R.id.iv_drag_handle);
		mController.setSortEnabled(true);
		mController.setDragInitMode(DragSortController.ON_DOWN);
		mController.setRemoveEnabled(false);
		mController.setRemoveMode(DragSortController.FLING_REMOVE);

		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDropListener(mCLAdapter);
//		mDslv.setRemoveListener(onRemove);
		mDslv.setDragEnabled(true);

		activity.getChecklistManager().sortNode(mChecklist);
		activity.notifyChangeFragment(this);

		return rootView;
	}

	private void activateAddView(){
		//追加エリアの可視化
		this.etx_checklist_add.setVisibility(View.VISIBLE);
		this.etx_checklist_add.requestFocus();
		//入力フォーカスの設定
		InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(etx_checklist_add, 0);

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
	}

	@Override
	public void onClickMenu(int menuId) {
		switch (menuId) {
		case MainActivity.MENU_ADD_ID:
			activateAddView();
			//TODO もう一回押したらOFFにする
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
				mDivPos = mChecklist.getUncheckedNum();
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
			}else{
				((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.GONE);
			}

			//ChecklisNodeクリック時の動作（タイトル編集UIの表示）
			etx_title_node.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					TextView txv_position = (TextView)((View)v.getParent()).findViewById(R.id.txv_position_hide);
					final int nodePosition = Integer.parseInt(txv_position.getText().toString());

					final EditText editView = new EditText(activity);
					editView.setText(mChecklist.getNodes().get(nodePosition).getTitle());
					editView.setSelection(editView.getText().length());

					//					final TextView hide = new TextView(activity);
					//					hide.setVisibility(View.GONE);
					//					TextView txv_position = (TextView)((View)v.getParent()).findViewById(R.id.txv_position_hide);
					//					hide.setText(txv_position.getText());

					builder.setView(editView)
					.setTitle("テスト")
					.setPositiveButton("保存", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							ChecklistNode node = mChecklist.getNodes().get(nodePosition);
							node.setTitle(editView.getText().toString());
							activity.getChecklistManager().nodeUpdated(mChecklist, node);
							notifyDataSetChanged();
						}
					})
					.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							// TODO 自動生成されたメソッド・スタブ
						}
					})
					.show();
				}
			});

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
					mDivPos = mChecklist.getUncheckedNum();
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

		private int dataPosition(int position) {
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
				mDivPos = mChecklist.getUncheckedNum();
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
