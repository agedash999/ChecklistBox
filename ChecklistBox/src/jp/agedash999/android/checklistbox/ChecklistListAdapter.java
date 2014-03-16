package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChecklistListAdapter extends ArrayAdapter<Checklist> {

	private List<Checklist> mList;
	private LayoutInflater mInflater;
	private int mLayout;

	public ChecklistListAdapter(Context context, int resource, List<Checklist> objects) {
		super(context, resource, objects);
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLayout = resource;
		this.mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Checklist clist;
		if(view == null){
			view = mInflater.inflate(mLayout, null);
		}
		clist = mList.get(position);
		((TextView)view.findViewById(R.id.title_checklist)).setText(clist.getTitle());
		((TextView)view.findViewById(R.id.date)).setText(clist.getDate().toString());

		return view;
	}

}
