package jp.agedash999.android.checklistbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryFragment extends Fragment{
	public static final String ARG_SECTION_NUMBER = "section_number";

	private MainActivity activity;
	private View rootView;
	private ListView listHistory;
	private ChecklistListAdapter mCLAdapter;

	public HistoryFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_history, container, false);

		listHistory = (ListView)rootView.findViewById(R.id.list_history);

		mCLAdapter = new ChecklistListAdapter(getActivity(), R.layout.listrow_history,
				activity.getChecklistManager().getHistoryList());
		listHistory.setAdapter(mCLAdapter);
		listHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Checklist clist = (Checklist)parent.getItemAtPosition(position);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment fragment = ChecklistFragment.newInstance(clist);
				ft.replace(R.id.main_layout, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}

		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


}
