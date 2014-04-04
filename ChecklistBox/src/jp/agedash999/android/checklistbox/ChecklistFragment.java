package jp.agedash999.android.checklistbox;

import java.util.List;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ChecklistFragment extends Fragment
	implements ChecklistBoxChildFragment{

	private MainActivity activity;
	private View rootView;
	private ListView listChecklist;
	private ChecklistAdapter mCLAdapter;

	private Checklist mChecklist;

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
		this.rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
		this.listChecklist = (ListView)rootView.findViewById(R.id.list_checklist);
		this.activity = (MainActivity)getActivity();

		mCLAdapter = new ChecklistAdapter(getActivity(), R.layout.listrow_checklist,
				mChecklist.getNodes());
		listChecklist.setAdapter(mCLAdapter);

		return rootView;
	}

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
			((TextView)view.findViewById(R.id.title_node)).setText(cnode.getTitle());
			((TextView)view.findViewById(R.id.txv_position_hide)).setText(Integer.toString(position));
			CheckBox cbx_checked = (CheckBox)view.findViewById(R.id.cbx_checked);
			cbx_checked.setChecked(cnode.isChecked());
			cbx_checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO 自動生成されたメソッド・スタブ
//					ViewParent view = buttonView.getParent();
					TextView txv_position = (TextView)((View)buttonView.getParent()).findViewById(R.id.txv_position_hide);
					int nodePosition = Integer.parseInt(txv_position.getText().toString());
					ChecklistNode node = mChecklist.getNodes().get(nodePosition);
					node.setChecked(isChecked);
					activity.getChecklistManager().nodeCheckChanged(mChecklist, node);
					notifyDataSetChanged();
				}
			});

			return view;

		}
	}

	private void activateAddView(){
		//追加エリアの有効化
		EditText edx_checklist_add = (EditText)rootView.findViewById(R.id.etx_checklist_add);
		edx_checklist_add.setVisibility(View.VISIBLE);
		edx_checklist_add.requestFocus();
		edx_checklist_add.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if(event.getAction() == KeyEvent.ACTION_UP) {

					}
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
