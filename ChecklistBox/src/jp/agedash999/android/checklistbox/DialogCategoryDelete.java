package jp.agedash999.android.checklistbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DialogCategoryDelete extends DialogFragment {

//	private final String KEY_DIALOG_TITLE = "dialog_title";
//	private final String KEY_DIALOG_LABEL_DATE = "label_title";
	private final int idDialogLayout = R.layout.dialog_category_delete;
	private final int idDialogTitle = R.string.dialog_title_category_delete;

	private MainActivity activity;
	private ChecklistCategory mCategory;
	private ChecklistDialogListener mListener;

	public static DialogCategoryDelete getDialog(ChecklistCategory category){
		DialogCategoryDelete dialog = new DialogCategoryDelete();
		dialog.mCategory = category;

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		activity = (MainActivity)getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(idDialogLayout, null);

		return builder.create();
//		return super.onCreateDialog(savedInstanceState);
	}




	public void setChecklistDialogListener(ChecklistDialogListener listener){
		mListener = listener;
	}

	public void removeChecklistDialogListener(){
		mListener = null;
	}

	public interface ChecklistDialogListener
	//extends EventListener {
	{
		public void onChecklistInfoSave(Checklist clist, int dialogType);

		public void onChecklistInfoCansel();

	}

//	public class DateSetListener implements OnDateSetListener{
//
//		@Override
//		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//			Calendar cal;
//
//		}
//
//	}
}
