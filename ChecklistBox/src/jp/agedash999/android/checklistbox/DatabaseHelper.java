package jp.agedash999.android.checklistbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "checklist_box.db";
	private static final int DBVERSION = 0;

	private static final String TABLE_IDLIST = "id_list";
	private static final String TABLE_CURRENTLIST = "current_list";
	private static final String TABLE_STOCKLIST = "stock_list";
	private static final String TABLE_HISTORYLIST = "history_list";
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_PREFIX_CHECKLIST = "checklist_";

	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_TABLEID = "table_id";
	private static final String COLUMN_CATEGORYID = "category_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_SORTNUM = "sort_num";
	private static final String COLUMN_EXPDATE = "exp_date";
	private static final String COLUMN_FINDATE = "fin_date";
	private static final String COLUMN_CHECKED = "checked";
	private static final String COLUMN_NEXTID = "next_id";

	//TODO テーブル項目定義
	//ID管理テーブル
	private static final String CTBL_IDLIST =
			"create table " + TABLE_IDLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_NEXTID + " integer not null)";
	//処理中チェックリストテーブル
	private static final String CTBL_CURRENTLIST =
			"create table " + TABLE_CURRENTLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_TABLEID + " integer not null,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_EXPDATE + " text)";
	//保存チェックリストテーブル
	private static final String CTBL_STOCKLIST =
			"create table " + TABLE_STOCKLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_TABLEID + " integer not null,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_CATEGORYID + " integer,"
			+ COLUMN_SORTNUM + " real)";
	//履歴チェックリストテーブル
	private static final String CTBL_HISTORYLIST =
			"create table " + TABLE_HISTORYLIST + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_TABLEID + " integer not null,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_CATEGORYID + " integer,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_FINDATE + " text)";
	//カテゴリテーブル
	private static final String CTBL_CATEGORY =
			"create table " + TABLE_CATEGORY + " ("
			+ COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_CATEGORYID + " integer not null,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_SORTNUM + " real)";
	//チェックリストテーブル
	private static final String CTBL_CHECKLIST_SUF =
			COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_SORTNUM + " real,"
			+ COLUMN_SORTNUM + " integer)";


	public DatabaseHelper(Context context){
		super(context, DBNAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO テーブル作成
		db.execSQL(CTBL_IDLIST);
		db.execSQL(CTBL_CATEGORY);
		db.execSQL(CTBL_CURRENTLIST);
		db.execSQL(CTBL_STOCKLIST);
		db.execSQL(CTBL_HISTORYLIST);

		// TODO ID管理テーブルの初期化
		ContentValues cv = new ContentValues();

		cv.put(COLUMN_NAME, TABLE_CURRENTLIST);
		cv.put(COLUMN_NEXTID, 1);
		db.insert(TABLE_IDLIST, "DB初期化null", cv);
		cv.clear();

		cv.put(COLUMN_NAME, TABLE_CATEGORY);
		cv.put(COLUMN_NAME, 1);
		db.insert(TABLE_IDLIST, "DB初期化null", cv);

		// TODO 初回データの読み込み・テーブル作成

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

	private String makeCTBLString(int table_id){
		return "create table " + TABLE_PREFIX_CHECKLIST +
				table_id + " (" + CTBL_CHECKLIST_SUF;
	}

}
