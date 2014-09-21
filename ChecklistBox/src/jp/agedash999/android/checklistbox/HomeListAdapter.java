package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeListAdapter extends ArrayAdapter<Checklist> {

	private Context context;
	private List<Checklist> mList;
	private LayoutInflater mInflater;
	private int mLayout;

	private boolean moveMode = false;

	private String viewItem;

	public HomeListAdapter(Context context, int resource, List<Checklist> objects) {
		super(context, resource, objects);
		this.context = context;
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

		//サマリー表示
		if(viewItem.equals("date")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					context.getString(R.string.checklist_row_expdate) + clist.getDateFormated());
		}else if(viewItem.equals("node")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					"ノード数");
		}else if(viewItem.equals("memo")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					clist.getMemo());
		}else{

		}


		if(moveMode){
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.VISIBLE);
			((ImageView)view.findViewById(R.id.iv_checklist)).setVisibility(View.GONE);
		}else{
			((ImageView)view.findViewById(R.id.iv_drag_handle)).setVisibility(View.GONE);
			((ImageView)view.findViewById(R.id.iv_checklist)).setVisibility(View.VISIBLE);
		}

		return view;
	}

	public void changeMoveMode(){
		moveMode = (!moveMode);
	}

	public Checklist getChecklist(int position){
		return mList.get(position);
	}

	public void refleshAdapter(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		//いったん初期値を設定（本来はXMLから読み込みたい）
		this.viewItem = pref.getString(SettingActivity.KEY_VIEWITEM_HOME, "date");
	}
}
