package jp.agedash999.android.checklistbox;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class StockListAdapter extends BaseExpandableListAdapter {

	private List<ChecklistCategory> stockList;
	private Context context;
	private LayoutInflater layoutInflater;

	private String viewItem;

	public StockListAdapter(
			Context context,
			List<ChecklistCategory> stockList){
		super();
		this.context = context;
		this.stockList = stockList;
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return stockList.get(groupPosition).getChildList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return stockList.get(groupPosition).getChildList().get(childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		View view = convertView;
		Checklist clist = (Checklist)getChild(groupPosition, childPosition);

		if(view == null){
			//TODO parentいる？
			view = layoutInflater.inflate(R.layout.listrow_stock_checklist, null);
		}

		((TextView)view.findViewById(R.id.title_stocklist))
		.setText(clist.getTitle());

		if(viewItem.equals("date")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					context.getString(R.string.checklist_row_credate) + clist.getDateFormated());
		}else if(viewItem.equals("node")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					"ノード数");
		}else if(viewItem.equals("memo")){
			((TextView)view.findViewById(R.id.tv_summery)).setText(
					clist.getMemo());
		}else{

		}

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return stockList.get(groupPosition).getChildList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return stockList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return stockList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(
			int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			//TODO parentいる？
			view = layoutInflater.inflate(R.layout.listrow_stock_category, null);
		}

		((TextView)view.findViewById(R.id.title_category))
		.setText(stockList.get(groupPosition).getTitle());

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void refleshOrder(){
		//TODO 順序再読み込み
	}

	public void refleshAdapter(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		//いったん初期値を設定（本来はXMLから読み込みたい）
		this.viewItem = pref.getString(SettingActivity.KEY_VIEWITEM_STOCK, "date");
	}

}
