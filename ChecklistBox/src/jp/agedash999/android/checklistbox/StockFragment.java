package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class StockFragment extends Fragment{

	private final int CONTEXT_MENUID_EDIT = 0;
	private final int CONTEXT_MENUID_DELETE = 1;
	private final int CONTEXT_MENUID_TOHOME = 3;

	private MainActivity activity;
	private View rootView;
	private ExpandableListView listStock;
	private StockListAdapter mCLAdapter;

	private ChecklistManager mCLManager;

	public static final String KEY_TITLE = "title";
	public static final String KEY_CATEGORY_ID = "category_id";

	public StockFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_stock, container, false);

		listStock = (ExpandableListView)rootView.findViewById(R.id.list_stock);

		//下位（Manager）との疎結合性を高めるため、渡されたList/Mapをここで格納し直す
		List<String> categoryList = new ArrayList<String>();
		List<List<Checklist>> stockList = new ArrayList<List<Checklist>>();
		mCLManager.getStockAndCategory(categoryList, stockList);

		mCLAdapter = new StockListAdapter(categoryList, stockList, inflater);
		listStock.setAdapter(mCLAdapter);

		listStock.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Checklist clist = (Checklist)parent.getExpandableListAdapter()
						.getChild(groupPosition, childPosition);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment fragment = ChecklistFragment.newInstance(clist);
				ft.replace(R.id.main_layout, fragment);
				ft.addToBackStack(null);
				ft.commit();
				return false;
			}

		});

		registerForContextMenu(listStock);

		return rootView;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.conmenu_home_title));
		menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
		menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
		menu.add(0, CONTEXT_MENUID_TOHOME, 0, R.string.conmenu_tohome);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
		this.mCLManager = this.activity.getChecklistManager();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
