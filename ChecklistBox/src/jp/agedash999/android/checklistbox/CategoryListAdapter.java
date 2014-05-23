package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter  extends ArrayAdapter<ChecklistCategory> {

	private Context context;
	private List<ChecklistCategory> mList;
	private LayoutInflater mInflater;
	private int mLayout;

	private boolean moveMode = false;

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
		ChecklistCategory category;
		if(view == null){
			view = mInflater.inflate(mLayout, null);
		}
		category = mList.get(position);
		((TextView)view.findViewById(R.id.title_category)).setText(category.getTitle());

		if(moveMode){
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.VISIBLE);
		}else{
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.GONE);
		}

		return view;
	}

	public void changeMoveMode(){
		moveMode = (!moveMode);
	}

	public ChecklistCategory getCategory(int position){
		return mList.get(position);
	}

}
