package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.MainActivity.ChecklistBoxChildFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CategoryEditFragment extends Fragment
implements ChecklistDialog.ChecklistDialogListener
,ChecklistBoxChildFragment {

	private MainActivity activity;
	private View rootView;
	private ListView listCategory;
	//private CategoryListAdapter mCLAdapter;

	public CategoryEditFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.rootView = inflater.inflate(R.layout.fragment_history, container, false);

//		listCategory = (ListView)rootView.findViewById()
		//TODO 未実装
		return rootView;
	}

	@Override
	public void onClickMenu(int menuId) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onChecklistInfoSave(Checklist clist, int dialogType) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onChecklistInfoCansel() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
