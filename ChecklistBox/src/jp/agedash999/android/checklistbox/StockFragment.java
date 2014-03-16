package jp.agedash999.android.checklistbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StockFragment extends Fragment{
	public static final String ARG_SECTION_NUMBER = "section_number";

	public StockFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_stock, container, false);
//		TextView dummyTextView = (TextView) rootView.findViewById(R.id.section);
//		dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
		return rootView;
	}

}
