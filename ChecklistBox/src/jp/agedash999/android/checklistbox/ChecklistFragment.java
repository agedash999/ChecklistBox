package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ChecklistFragment extends Fragment{

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
			((CheckBox)view.findViewById(R.id.cbx_checked)).setChecked(cnode.isChecked());

			return view;

		}
	}
}
