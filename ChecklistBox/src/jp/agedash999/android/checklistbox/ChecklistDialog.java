package jp.agedash999.android.checklistbox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jp.agedash999.android.checklistbox.ChecklistDialogHandler.ChecklistDialogListener;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@Deprecated
public class ChecklistDialog extends DialogFragment {

//	private final String KEY_DIALOG_TITLE = "dialog_title";
//	private final String KEY_DIALOG_LABEL_DATE = "label_title";

	public final static int FOR_HOME_NEW = 0;
	public final static int FOR_STOCK_NEW = 1;
	public final static int FOR_HOME_EDIT = 2;
	public final static int FOR_STOCK_EDIT = 3;
	public final static int FOR_HOME_STORE = 4;
	public final static int FOR_HISTORY_STORE = 5;
	public final static int FOR_STOCK_TOHOME = 6;
	public final static int FOR_HISTORY_TOHOME = 7;

	private Checklist mChecklist;
	private int mDialogType;
	private int idLabelDate;
	private int idDialogTitle;
	private int idPositiveLabel;
	private int idNegativeLabel;
	private Calendar mDate;

	private EditText etx_title;
	private EditText etx_memo;
	private TextView txv_date;
	private Spinner spn_category;

	private ChecklistDialogListener mListener;

	public ChecklistDialog(){
		super();
		mDialogType = FOR_HOME_NEW;
		mDate = Calendar.getInstance();
		mChecklist = new Checklist(Checklist.CHECKLIST_STORE, "", null);
		idDialogTitle = R.string.dialog_title_clist_create_stock;
		idLabelDate = R.string.dialog_label_clist_credate;
		idPositiveLabel = R.string.dialog_button_clist_create;
		idNegativeLabel = R.string.dialog_button_clist_cansel;
	}

	public static ChecklistDialog getDialogBlank(int dialogType){

		ChecklistDialog dialog = new ChecklistDialog();
		dialog.mDialogType = dialogType;
		dialog.mDate = Calendar.getInstance();
		switch (dialogType) {
		case FOR_HOME_NEW:
			//TODO 日付の初期設定
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING, "", null);
			dialog.idDialogTitle = R.string.dialog_title_clist_create_home;
			dialog.idLabelDate = R.string.dialog_label_clist_expdate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_create;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_NEW:
			//TODO 日付の初期設定
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_STORE, "", null);
			dialog.idDialogTitle = R.string.dialog_title_clist_create_stock;
			dialog.idLabelDate = R.string.dialog_label_clist_credate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_create;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		default:
			break;
		}
		return dialog;
	}

	public static ChecklistDialog getDialog(
			int dialogType,Checklist clist){
		ChecklistDialog dialog = new ChecklistDialog();
		dialog.mDialogType = dialogType;
		dialog.mDate = Calendar.getInstance();
//		ChecklistCategory undef = ChecklistManager.getCategoryUndefined();

		switch (dialogType) {
		case FOR_HOME_EDIT:
			dialog.mChecklist = clist;
			dialog.idDialogTitle = R.string.dialog_title_clist_edit_home;
			dialog.idLabelDate = R.string.dialog_label_clist_expdate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_update;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_EDIT:
			dialog.mChecklist = clist;
			dialog.idDialogTitle = R.string.dialog_title_clist_edit_stock;
			dialog.idLabelDate = R.string.dialog_label_clist_credate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_update;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HOME_STORE:
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_STORE,
					new String(clist.getTitle()), null);
			dialog.mChecklist.setMemo(new String(clist.getMemo()));
			dialog.mChecklist.setChecklist(clist.getChecklistCopy(false));
			dialog.idDialogTitle = R.string.dialog_title_clist_store_home;
			dialog.idLabelDate = R.string.dialog_label_clist_credate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_save;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HISTORY_STORE:
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_STORE,
					new String(clist.getTitle()), null);
			dialog.mChecklist.setMemo(new String(clist.getMemo()));
			dialog.mChecklist.setChecklist(clist.getChecklistCopy(false));
			dialog.idDialogTitle = R.string.dialog_title_clist_store_history;
			dialog.idLabelDate = R.string.dialog_label_clist_credate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_save;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_TOHOME:
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING,
					new String(clist.getTitle()), null);
			dialog.mChecklist.setMemo(new String(clist.getMemo()));
			dialog.mChecklist.setChecklist(clist.getChecklistCopy(false));
			dialog.idDialogTitle = R.string.dialog_title_clist_copy_stock;
			dialog.idLabelDate = R.string.dialog_label_clist_expdate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_tohome;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HISTORY_TOHOME:
			dialog.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING,
					new String(clist.getTitle()), null);
			dialog.mChecklist.setMemo(new String(clist.getMemo()));
			dialog.mChecklist.setChecklist(clist.getChecklistCopy(false));
			dialog.idDialogTitle = R.string.dialog_title_clist_copy_history;
			dialog.idLabelDate = R.string.dialog_label_clist_expdate;
			dialog.idPositiveLabel = R.string.dialog_button_clist_tohome;
			dialog.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		default:
			break;
		}

		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_checklist, null);

		((TextView)view.findViewById(R.id.tv_label_date))
		.setText(getString(idLabelDate));

		etx_title = ((EditText)view.findViewById(R.id.etx_title));
//		etx_title.setText(mChecklist.getTitle());
//		etx_title.requestFocus();
//		etx_title.setSelection(etx_title.getText().length());
		etx_memo = ((EditText)view.findViewById(R.id.etx_memo));
//		etx_memo.setText(mChecklist.getMemo());
		spn_category = (Spinner)view.findViewById(R.id.spn_category);

		if(mDialogType == FOR_HISTORY_STORE || mDialogType == FOR_HOME_STORE
				|| mDialogType == FOR_STOCK_NEW || mDialogType == FOR_STOCK_EDIT){
			((TextView)view.findViewById(R.id.tv_label_category)).setVisibility(TextView.VISIBLE);
			spn_category.setVisibility(Spinner.VISIBLE);
//			List<ChecklistCategory> stockList = ((MainActivity)getActivity()).getChecklistManager().getStockList();
//			spn_category.setAdapter(new ArrayAdapter<ChecklistCategory>(
//					getActivity(), android.R.layout.simple_spinner_item,stockList));
//			spn_category.setSelection(stockList.indexOf(mChecklist.getCategory()));
		}

		txv_date = (TextView)view.findViewById(R.id.tv_date);
//		mDate = mChecklist.getDate();
//		txv_date.setText(mChecklist.getDateFormated());
		txv_date.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					Calendar cal = mChecklist.getDate();
					DatePickerDialog datePickerDialog = new DatePickerDialog(
							getActivity(), new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									mDate.set(year, monthOfYear, dayOfMonth);
									reloadDate();
								}
							} , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
				}
				return true;
			}
		});

		builder.setView(view);
		builder.setTitle(getString(idDialogTitle));
		builder.setPositiveButton(idPositiveLabel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ここでデータの更新を行う
				mChecklist.setTitle(etx_title.getText().toString());
				mChecklist.setMemo(etx_memo.getText().toString());
				if(spn_category.getVisibility() == Spinner.VISIBLE){
					ChecklistCategory category = (ChecklistCategory)spn_category.getSelectedItem();
					mChecklist.setCategory(category);
				}
				mChecklist.setDate(mDate);
				mListener.onChecklistInfoSave(mChecklist, mDialogType);
				dismiss();
			}
		});

		builder.setNegativeButton(idNegativeLabel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onChecklistInfoCansel();
				dismiss();
			}
		});

		return builder.create();
//		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		etx_title.setText(mChecklist.getTitle());
		etx_title.setSelection(etx_title.getText().length());
		etx_memo.setText(mChecklist.getMemo());

		if(spn_category.getVisibility()==Spinner.VISIBLE){
			List<ChecklistCategory> stockList = ((MainActivity)getActivity()).getChecklistManager().getStockList();
			spn_category.setAdapter(new ArrayAdapter<ChecklistCategory>(
					getActivity(), android.R.layout.simple_spinner_item,stockList));
			spn_category.setSelection(stockList.indexOf(mChecklist.getCategory()));
		}

		mDate = mChecklist.getDate();
		txv_date.setText(mChecklist.getDateFormated());

	}

	@Override
	public void onDetach() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDetach();
		dismiss();
	}

	private void reloadDate(){
		//TODO ロケール考慮
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E)");
		txv_date.setText(sdf.format(mDate.getTime()));
	}


	public void setChecklistDialogListener(ChecklistDialogListener listener){
		mListener = listener;
	}

	public void removeChecklistDialogListener(){
		mListener = null;
	}

//	public interface ChecklistDialogListener
//	//extends EventListener {
//	{
//		public void onChecklistInfoSave(Checklist clist, int dialogType);
//
//		public void onChecklistInfoCansel();
//
//	}

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
