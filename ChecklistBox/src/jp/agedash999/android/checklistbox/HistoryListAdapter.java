package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryListAdapter extends ArrayAdapter<Checklist> {

	private Context context;
	private List<Checklist> mList;
	private LayoutInflater mInflater;
	private int mLayout;
	private int dialogType;

	private boolean moveMode = false;

	public static final int DIALOG_HOME = 0;
	public static final int DIALOG_HISTORY = 1;

	public HistoryListAdapter(Context context, int resource, List<Checklist> objects, int dialogType) {
		super(context, resource, objects);
		this.context = context;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLayout = resource;
		this.mList = objects;
		this.dialogType = dialogType;
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
		((TextView)view.findViewById(R.id.date)).setText(clist.getDateFormated());

		if(moveMode){
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.VISIBLE);
		}else{
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.GONE);
		}

		switch (dialogType) {
		case DIALOG_HOME:
			((TextView)view.findViewById(R.id.label_date)).setText(context.getString(R.string.checklist_row_expdate));
			break;
		case DIALOG_HISTORY:
			((TextView)view.findViewById(R.id.label_date)).setText(context.getString(R.string.checklist_row_findate));
			break;
		}

		return view;
	}

	public void changeMoveMode(){
		moveMode = (!moveMode);
	}

	public Checklist getChecklist(int position){
		return mList.get(position);
	}
}
