package jp.agedash999.android.checklistbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "checklist_box.db";
	private static final int DBVERSION = 1;
	public static String CATEGORY_UNDEFINED_NAME;
	public static final int CATEGORY_UNDEFINED_ID = 1;

	public static final int CHECKLIST_RUNNING = Checklist.CHECKLIST_RUNNING;
	public static final int CHECKLIST_STORE = Checklist.CHECKLIST_STORE ;
	public static final int CHECKLIST_HISTORY = Checklist.CHECKLIST_HISTORY;

	public static final String TABLE_CURRENTLIST = "current_list";
	public static final String TABLE_STOCKLIST = "stock_list";
	public static final String TABLE_HISTORYLIST = "history_list";
	public static final String TABLE_CATEGORY = "category";
	public static final String TABLE_PREFIX_CURRENT = "crnt_";
	public static final String TABLE_PREFIX_STOCK = "stck_";
	public static final String TABLE_PREFIX_HISTORY = "hist_";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORYID = "category_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MEMO = "memo";
	public static final String COLUMN_SORTNUM = "sort_num";
	public static final String COLUMN_EXPDATE = "exp_date";
	public static final String COLUMN_CREDATE = "cre_date";
	public static final String COLUMN_FINDATE = "fin_date";
	public static final String COLUMN_CHECKED = "checked";
	public static final String COLUMN_NEXTID = "next_id";

	//処理中チェックリストテーブル
	private static final String CTBL_CURRENTLIST =
			"create table " + TABLE_CURRENTLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_MEMO + " text,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_EXPDATE + " text)";
	//保存チェックリストテーブル
	private static final String CTBL_STOCKLIST =
			"create table " + TABLE_STOCKLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_MEMO + " text,"
			+ COLUMN_CATEGORYID + " integer,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_CREDATE + " text)";
	//履歴チェックリストテーブル
	private static final String CTBL_HISTORYLIST =
			"create table " + TABLE_HISTORYLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_MEMO + " text,"
			+ COLUMN_CATEGORYID + " integer,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_FINDATE + " text)";
	//カテゴリテーブル
	private static final String CTBL_CATEGORY =
			"create table " + TABLE_CATEGORY + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_SORTNUM + " real)";
	//チェックリストテーブル
	private static final String CTBL_CHECKLIST_SUF =
			COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_CHECKED + " integer,"
			+ COLUMN_SORTNUM + " real)";

	public DatabaseHelper(Context context){
		super(context, DBNAME, null, DBVERSION);
		DatabaseHelper.CATEGORY_UNDEFINED_NAME = context.getString(R.string.category_undefined);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO テーブル作成
		db.execSQL(CTBL_CATEGORY);
		db.execSQL(CTBL_CURRENTLIST);
		db.execSQL(CTBL_STOCKLIST);
		db.execSQL(CTBL_HISTORYLIST);

		// TODO 初回データの読み込み・テーブル作成
		// カテゴリ未指定レコード(ID 1)を挿入
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_NAME, CATEGORY_UNDEFINED_NAME);
		cv.put(COLUMN_SORTNUM, 0);
		db.insert(TABLE_CATEGORY, null, cv);
		cv.clear();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public String makeCTBLString(int tableID, int checklistType){
		switch (checklistType) {
		case CHECKLIST_RUNNING:
			return "create table " + TABLE_PREFIX_CURRENT +
					tableID + " (" + CTBL_CHECKLIST_SUF;
		case CHECKLIST_STORE:
			return "create table " + TABLE_PREFIX_STOCK +
					tableID + " (" + CTBL_CHECKLIST_SUF;
		case CHECKLIST_HISTORY:
			return "create table " + TABLE_PREFIX_HISTORY +
					tableID + " (" + CTBL_CHECKLIST_SUF;
		}
		return null;
	}

	public String getTableName(int tableID, int checklistType){
		switch (checklistType) {
		case CHECKLIST_RUNNING:
			return TABLE_PREFIX_CURRENT + tableID;
		case CHECKLIST_STORE:
			return TABLE_PREFIX_STOCK + tableID;
		case CHECKLIST_HISTORY:
			return TABLE_PREFIX_HISTORY + tableID;
		}
		return null;
	}
}
