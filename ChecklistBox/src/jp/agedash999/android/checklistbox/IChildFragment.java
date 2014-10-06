package jp.agedash999.android.checklistbox;

public interface IChildFragment{

	public abstract String getFragmenTitle();

	public abstract int getFragmentIconID();

	public abstract void onClickMenu(int menuId);

	public abstract int getFragmentPositionID();

}

