package jp.agedash999.android.checklistbox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ChecklistDialogHandler{

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

	public ChecklistDialogHandler(){
		mDialogType = FOR_HOME_NEW;
		mDate = Calendar.getInstance();
		mChecklist = new Checklist(Checklist.CHECKLIST_STORE, "", null);
		idDialogTitle = R.string.dialog_title_clist_create_stock;
		idLabelDate = R.string.dialog_label_clist_credate;
		idPositiveLabel = R.string.dialog_button_clist_create;
		idNegativeLabel = R.string.dialog_button_clist_cansel;
	}

	public static ChecklistDialogHandler getHandlerBlank(int dialogType){

		ChecklistDialogHandler handler = new ChecklistDialogHandler();
		handler.mDialogType = dialogType;
		handler.mDate = Calendar.getInstance();
		switch (dialogType) {
		case FOR_HOME_NEW:
			//TODO 日付の初期設定
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING, "", null);
			handler.idDialogTitle = R.string.dialog_title_clist_create_home;
			handler.idLabelDate = R.string.dialog_label_clist_expdate;
			handler.idPositiveLabel = R.string.dialog_button_clist_create;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_NEW:
			//TODO 日付の初期設定
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_STORE, "", null);
			handler.idDialogTitle = R.string.dialog_title_clist_create_stock;
			handler.idLabelDate = R.string.dialog_label_clist_credate;
			handler.idPositiveLabel = R.string.dialog_button_clist_create;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		default:
			break;
		}
		return handler;
	}

	public static ChecklistDialogHandler getHandler(
			int dialogType,Checklist clist){
		ChecklistDialogHandler handler = new ChecklistDialogHandler();
		handler.mDialogType = dialogType;
		handler.mDate = Calendar.getInstance();
//		ChecklistCategory undef = ChecklistManager.getCategoryUndefined();

		switch (dialogType) {
		case FOR_HOME_EDIT:
			handler.mChecklist = clist;
			handler.idDialogTitle = R.string.dialog_title_clist_edit_home;
			handler.idLabelDate = R.string.dialog_label_clist_expdate;
			handler.idPositiveLabel = R.string.dialog_button_clist_update;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_EDIT:
			handler.mChecklist = clist;
			handler.idDialogTitle = R.string.dialog_title_clist_edit_stock;
			handler.idLabelDate = R.string.dialog_label_clist_credate;
			handler.idPositiveLabel = R.string.dialog_button_clist_update;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HOME_STORE:
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_STORE,
					new String(clist.getTitle()), null);
			handler.mChecklist.setMemo(new String(clist.getMemo()));
			handler.mChecklist.setChecklist(clist.getChecklistCopy(false));
			handler.idDialogTitle = R.string.dialog_title_clist_store_home;
			handler.idLabelDate = R.string.dialog_label_clist_credate;
			handler.idPositiveLabel = R.string.dialog_button_clist_save;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HISTORY_STORE:
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_STORE,
					new String(clist.getTitle()), null);
			handler.mChecklist.setMemo(new String(clist.getMemo()));
			handler.mChecklist.setChecklist(clist.getChecklistCopy(false));
			handler.idDialogTitle = R.string.dialog_title_clist_store_history;
			handler.idLabelDate = R.string.dialog_label_clist_credate;
			handler.idPositiveLabel = R.string.dialog_button_clist_save;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_STOCK_TOHOME:
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING,
					new String(clist.getTitle()), null);
			handler.mChecklist.setMemo(new String(clist.getMemo()));
			handler.mChecklist.setChecklist(clist.getChecklistCopy(false));
			handler.idDialogTitle = R.string.dialog_title_clist_copy_stock;
			handler.idLabelDate = R.string.dialog_label_clist_expdate;
			handler.idPositiveLabel = R.string.dialog_button_clist_tohome;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		case FOR_HISTORY_TOHOME:
			handler.mChecklist = new Checklist(Checklist.CHECKLIST_RUNNING,
					new String(clist.getTitle()), null);
			handler.mChecklist.setMemo(new String(clist.getMemo()));
			handler.mChecklist.setChecklist(clist.getChecklistCopy(false));
			handler.idDialogTitle = R.string.dialog_title_clist_copy_history;
			handler.idLabelDate = R.string.dialog_label_clist_expdate;
			handler.idPositiveLabel = R.string.dialog_button_clist_tohome;
			handler.idNegativeLabel = R.string.dialog_button_clist_cansel;
			break;
		default:
			break;
		}

		return handler;
	}

	public Dialog createAlertDialog(MainActivity a) {
		final MainActivity activity = a;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_checklist, null);

		((TextView)view.findViewById(R.id.tv_label_date))
		.setText(activity.getString(idLabelDate));

		etx_title = ((EditText)view.findViewById(R.id.etx_title));
		etx_title.setText(mChecklist.getTitle());
		etx_title.requestFocus();
		etx_title.setSelection(etx_title.getText().length());
		etx_memo = ((EditText)view.findViewById(R.id.etx_memo));
//		etx_memo.setText(mChecklist.getMemo());
		spn_category = (Spinner)view.findViewById(R.id.spn_category);

		if(mDialogType == FOR_HISTORY_STORE || mDialogType == FOR_HOME_STORE
				|| mDialogType == FOR_STOCK_NEW || mDialogType == FOR_STOCK_EDIT){
			((TextView)view.findViewById(R.id.tv_label_category)).setVisibility(TextView.VISIBLE);
			spn_category.setVisibility(Spinner.VISIBLE);
			List<ChecklistCategory> stockList = ((MainActivity)activity).getChecklistManager().getStockList();
			spn_category.setAdapter(new ArrayAdapter<ChecklistCategory>(
					activity, android.R.layout.simple_spinner_item,stockList));
			spn_category.setSelection(stockList.indexOf(mChecklist.getCategory()));
		}

		txv_date = (TextView)view.findViewById(R.id.tv_date);
		mDate = mChecklist.getDate();
		txv_date.setText(mChecklist.getDateFormated());
		txv_date.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					Calendar cal = mChecklist.getDate();
					DatePickerDialog datePickerDialog = new DatePickerDialog(
							activity, new OnDateSetListener() {

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
		builder.setTitle(activity.getString(idDialogTitle));
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
				//TODO いる？
				dialog.dismiss();
			}
		});

		builder.setNegativeButton(idNegativeLabel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onChecklistInfoCansel();
				//TODO いる？
				dialog.dismiss();
			}
		});

		return builder.create();
//		return super.onCreateDialog(savedInstanceState);
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

	public interface ChecklistDialogListener
	//extends EventListener {
	{
		public void onChecklistInfoSave(Checklist clist, int dialogType);

		public void onChecklistInfoCansel();

	}
}
