package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CategoryListAdapter  extends ArrayAdapter<ChecklistCategory> {

	private Context context;
	private List<ChecklistCategory> mList;
	private LayoutInflater mInflater;
	private int mLayout;

	public CategoryListAdapter(Context context, int resource, List<ChecklistCategory> objects) {
		super(context, resource, objects);
		this.context = context;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLayout = resource;
		this.mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ChecklistCategory clist;
		if(view == null){
			view = mInflater.inflate(mLayout, null);
		}
		clist = mList.get(position);



		return view;
	}
}
