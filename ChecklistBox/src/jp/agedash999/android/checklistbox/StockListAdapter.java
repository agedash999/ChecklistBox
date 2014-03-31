package jp.agedash999.android.checklistbox;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class StockListAdapter extends BaseExpandableListAdapter {

	private List<List<Checklist>> stockList; //チェックリストのList カテゴリ順<表示順>に格納
	private List<String> categoryList; //カテゴリ名のリスト カテゴリ表示順に格納

	private LayoutInflater layoutInflater;

	public StockListAdapter(
			List<String> categoryList ,
			List<List<Checklist>> stockList ,
			LayoutInflater layoutInflater ){
		super();
		this.categoryList = categoryList;
		this.stockList = stockList;
		this.layoutInflater = layoutInflater;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return stockList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return stockList.get(groupPosition).get(childPosition).getID();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		View view = convertView;
		Checklist clist = stockList.get(groupPosition).get(childPosition);
		if(view == null){
			//TODO parentいる？
			view = layoutInflater.inflate(R.layout.listrow_stock_checklist, null);
		}

		((TextView)view.findViewById(R.id.title_stocklist))
		.setText(clist.getTitle());

		((TextView)view.findViewById(R.id.summery_stocklist))
		.setText(clist.getDateFormated());

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return stockList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return categoryList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return categoryList.size();
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
		.setText(categoryList.get(groupPosition));

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

}
