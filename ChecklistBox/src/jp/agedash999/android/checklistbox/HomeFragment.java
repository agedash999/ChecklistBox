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

public class HomeFragment extends Fragment{

	private MainActivity activity;
	private View rootView;
	private ListView listHome;
	private ChecklistListAdapter mCLAdapter;

	public HomeFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_home, container, false);

		listHome = (ListView)rootView.findViewById(R.id.list_home);

		mCLAdapter = new ChecklistListAdapter(getActivity(), R.layout.listrow_home,
				activity.getChecklistManager().getRunningList());
		listHome.setAdapter(mCLAdapter);
		listHome.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                Checklist clist = (Checklist)listView.getItemAtPosition(position);
//                Toast.makeText(activity, clist.getTitle(), Toast.LENGTH_LONG).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = ChecklistFragment.newInstance(clist);
                ft.replace(R.id.main_layout, fragment);
                ft.addToBackStack(null);
                ft.commit();
                //TODO チェックリスト内容のフラグメントに切り替え clistを使う
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
