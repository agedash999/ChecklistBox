package jp.agedash999.android.checklistbox;

import android.support.v4.app.Fragment;

public abstract class AbstractChildFragment extends Fragment {

	public abstract String getFragmenTitle();

	public abstract int getFragmentIconID();

	public abstract void onClickMenu(int menuId);

	public abstract int getFragmentPositionID();

}
