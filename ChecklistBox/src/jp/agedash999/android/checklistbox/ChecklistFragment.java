package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.ListView;
import android.widget.TextView;

/**
 * チェックリストノード一覧画面のFragment
 */
public class ChecklistFragment extends Fragment
	implements ChecklistBoxChildFragment{

	private MainActivity activity;
	private ChecklistAdapter mCLAdapter;
	private Checklist mChecklist;

	private View rootView;
	private ListView listChecklist;
	private EditText etx_checklist_add;

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
		this.listChecklist = (ListView)rootView.findViewById(R.id.list_checklist);
		this.etx_checklist_add = (EditText)rootView.findViewById(R.id.etx_checklist_add);

		//Adapterのインスタンスを生成してListViewにセット
		mCLAdapter = new ChecklistAdapter(getActivity(), R.layout.listrow_checklist,
				mChecklist.getNodes());
		listChecklist.setAdapter(mCLAdapter);

		activity.getChecklistManager().sortNode(mChecklist);
		activity.notifyChangeFragment(this);

		return rootView;
	}

	/**
	 * チェックリストノード一覧画面のAdapter
	 */
	class ChecklistAdapter extends ArrayAdapter<ChecklistNode>{

		private List<ChecklistNode> mList;
		private LayoutInflater mInflater;
		private int mLayout;

		public ChecklistAdapter(Context context, int resource, List<ChecklistNode> objects) {
			super(context, resource, objects);
			this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mLayout = resource;
			this.mList = objects;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View view = convertView;
			ChecklistNode cnode;
			if(view == null){
				view = mInflater.inflate(mLayout, null);
			}
			cnode = mList.get(position);
			TextView etx_title_node = (TextView)view.findViewById(R.id.txv_title_node);
			etx_title_node.setText(cnode.getTitle());
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
					// TODO 自動生成されたメソッド・スタブ
					TextView txv_position = (TextView)((View)buttonView.getParent()).findViewById(R.id.txv_position_hide);
					int nodePosition = Integer.parseInt(txv_position.getText().toString());
					ChecklistNode node = mChecklist.getNodes().get(nodePosition);
					node.setChecked(isChecked);
					activity.getChecklistManager().nodeUpdated(mChecklist, node);
					activity.getChecklistManager().sortNode(mChecklist);
					notifyDataSetChanged();
				}
			});

			return view;

		}
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
}
