package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBAccess {
	private DatabaseHelper mDBHelper;
	private ContentValues mCV = new ContentValues();

	public DBAccess(Context context){
		mDBHelper = new DatabaseHelper(context);
	}

	public List<Checklist> getCurrentListAll(){
		List<Checklist> currentList = new ArrayList<Checklist>();
		mCV.clear();
		//TODO 並べ替え
		Cursor cursor = mDBHelper.getReadableDatabase().rawQuery(
				"select * from " + DatabaseHelper.TABLE_CURRENTLIST, null);

		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				Checklist clist = new Checklist(
						cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
						Checklist.CHECKLIST_RUNNING,
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
						null,
						cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXPDATE)));
				readChecklistNodes(clist);
				currentList.add(clist);
				cursor.moveToNext();
			}
		}
		mCV.clear();
		return currentList;
	}

	public void getCategory(Map<Integer,String> categoryList, List<Integer> categoryOrder ){
		mCV.clear();
		//TODO 並べ替え
		Cursor cursor = mDBHelper.getReadableDatabase().rawQuery
				("select * from " + DatabaseHelper.TABLE_CATEGORY, null);
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
				categoryList.put(
						id
						,cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
				categoryOrder.add(id);
				cursor.moveToNext();
			}
		}
		mCV.clear();
	}

	public List<ChecklistCategory> getStockListAll(){
		mCV.clear();
		List<ChecklistCategory> stockList = new ArrayList<ChecklistCategory>();
		//TODO 並べ替え
		Cursor cursorCategory = mDBHelper.getReadableDatabase().rawQuery
				("select * from " + DatabaseHelper.TABLE_CATEGORY, null);
		if(cursorCategory.moveToFirst()){
			while(!cursorCategory.isAfterLast()){
				//TODO 追加実装ポイント：カテゴリ内並び替え
				ChecklistCategory category = new ChecklistCategory(
						cursorCategory.getInt(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_ID)),
						cursorCategory.getString(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
				String selectStr = DatabaseHelper.COLUMN_CATEGORYID + "=?";
				String[] selectionArgs = { Integer.toString(category.getId()) };
				Cursor cursorStock = mDBHelper.getReadableDatabase().query(
						DatabaseHelper.TABLE_STOCKLIST, null, selectStr, selectionArgs, null, null, null);
				if(cursorStock.moveToFirst()){
					while(!cursorStock.isAfterLast()){
						Checklist clist = new Checklist(
								cursorStock.getInt(cursorStock.getColumnIndex(DatabaseHelper.COLUMN_ID)),
								Checklist.CHECKLIST_STORE,
								cursorStock.getString(cursorStock.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
								cursorStock.getString(cursorStock.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
								category,
								cursorStock.getInt(cursorStock.getColumnIndex(DatabaseHelper.COLUMN_CREDATE)));
						readChecklistNodes(clist);
						category.addChecklist(clist);
						cursorStock.moveToNext();
					}
				}
				stockList.add(category);
				cursorCategory.moveToNext();
			}
		}
		mCV.clear();
		return stockList;
	}

//	public Map<Integer,List<Checklist>> getStockListAll(Map<Integer,String> categoryList){
//		mCV.clear();
//		Map<Integer,List<Checklist>> stockList = new HashMap<Integer, List<Checklist>>();
//		int categoryID;
//		for(Map.Entry<Integer, String> cat : categoryList.entrySet()){
//			stockList.put(cat.getKey(), new ArrayList<Checklist>());
//		}
//		//TODO 並べ替え
//		Cursor cursor = mDBHelper.getReadableDatabase().rawQuery
//				("select * from " + DatabaseHelper.TABLE_STOCKLIST, null);
//		if(cursor.moveToFirst()){
//			while(!cursor.isAfterLast()){
//				categoryID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORYID));
//				Checklist clist = new Checklist(
//						cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
//						Checklist.CHECKLIST_STORE,
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
//						categoryID,
//						cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREDATE)));
//				readChecklistNodes(clist);
//				stockList.get(categoryID).add(clist);
//				cursor.moveToNext();
//			}
//		}
//		mCV.clear();
//		return stockList;
//	}

	public List<Checklist> getHistoryListAll(){
		List<Checklist> historyList = new ArrayList<Checklist>();
		mCV.clear();
		//TODO 並べ替え
		Cursor cursor = mDBHelper.getReadableDatabase().rawQuery(
				"select * from " + DatabaseHelper.TABLE_HISTORYLIST, null);

		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				Checklist clist = new Checklist(
						cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
						Checklist.CHECKLIST_HISTORY,
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
						null,
						cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_FINDATE)));
				readChecklistNodes(clist);
				historyList.add(clist);
				cursor.moveToNext();
			}
		}
		mCV.clear();
		return historyList;
	}

//	public void getStockListOfCategory(int categoryID){
//		//TODO 使う？
//	}


	public void readChecklistNodes(Checklist clist){
		clist.clearNodes();
		//TODO 並べ替え
		Cursor cursorNodes = mDBHelper.getReadableDatabase().rawQuery
				("select * from " + mDBHelper.
						getTableName(clist.getId(), clist.getType())
						, null);
		if(cursorNodes.moveToFirst()){
			while(!cursorNodes.isAfterLast()){
				ChecklistNode node = new ChecklistNode(
						cursorNodes.getInt(cursorNodes.getColumnIndex(DatabaseHelper.COLUMN_ID)),
						cursorNodes.getString(cursorNodes.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
						intToBool(cursorNodes.getInt
								(cursorNodes.getColumnIndex(DatabaseHelper.COLUMN_CHECKED))));
				clist.addNode(node);
				cursorNodes.moveToNext();
			}
		}
	}


	public void updateChecklistInfo(Checklist clist){
		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			mCV.clear();
			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
			mCV.put(DatabaseHelper.COLUMN_EXPDATE, clist.getDate().getTimeInMillis());
			mDBHelper.getWritableDatabase().update
					(DatabaseHelper.TABLE_CURRENTLIST, mCV,
					DatabaseHelper.COLUMN_ID + "=?" ,
					new String[]{String.valueOf(clist.getID())});
			mCV.clear();

			break;
		case Checklist.CHECKLIST_STORE:
			mCV.clear();
			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
			mCV.put(DatabaseHelper.COLUMN_CATEGORYID, clist.getCategory().getId());
			mCV.put(DatabaseHelper.COLUMN_CREDATE, clist.getDate().getTimeInMillis());
			mDBHelper.getWritableDatabase().update
					(DatabaseHelper.TABLE_STOCKLIST, mCV,
					DatabaseHelper.COLUMN_ID + "=?" ,
					new String[]{String.valueOf(clist.getID())});
			mCV.clear();

			break;
		case Checklist.CHECKLIST_HISTORY:
			//不要？新規作成のみ

			break;
		default:
			break;
		}
	}

	public int insertNewChecklist(Checklist clist){
		int id = -1;
		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			mCV.clear();
			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
			mCV.put(DatabaseHelper.COLUMN_EXPDATE, clist.getDate().getTimeInMillis());
			id = (int) mDBHelper.getWritableDatabase().insert(
					DatabaseHelper.TABLE_CURRENTLIST ,null,mCV);
			mCV.clear();
			clist.setId(id);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getID(), clist.getType()));

			break;

		case Checklist.CHECKLIST_STORE:
			int categoryID = DatabaseHelper.CATEGORY_UNDEFINED_ID;
			if(clist.getCategory() != null){
				categoryID = clist.getCategory().getId();
			}
			mCV.clear();
			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
			mCV.put(DatabaseHelper.COLUMN_CATEGORYID, categoryID);
			mCV.put(DatabaseHelper.COLUMN_CREDATE, clist.getDate().getTimeInMillis());
			id = (int) mDBHelper.getWritableDatabase().insert(
					DatabaseHelper.TABLE_STOCKLIST ,null,mCV);
			mCV.clear();
			clist.setId(id);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getID(),clist.getType()));

			break;
		case Checklist.CHECKLIST_HISTORY:
			mCV.clear();
			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
//			mCV.put(DatabaseHelper.COLUMN_CATEGORYID, clist.getCategoryID());
			mCV.put(DatabaseHelper.COLUMN_FINDATE, clist.getDate().getTimeInMillis());
			id = (int) mDBHelper.getWritableDatabase().insert(
					DatabaseHelper.TABLE_HISTORYLIST ,null,mCV);
			mCV.clear();
			clist.setId(id);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getID(),clist.getType()));

			break;
		default:
			break;
		}
		return id;
	}

	public void insertChecklistNodes(Checklist clist){
		Iterator<ChecklistNode> it = clist.getNodes().iterator();
		ChecklistNode node;
		int id;
		while(it.hasNext()){
			node = it.next();
			id = insertChecklistNode(clist, node);
			node.setID(id);
		}
	}

	public int insertChecklistNode(Checklist clist ,ChecklistNode node){
		mCV.clear();
		//TODO ノードID取得・セット
		mCV.put(DatabaseHelper.COLUMN_NAME, node.getTitle());
		mCV.put(DatabaseHelper.COLUMN_CHECKED, boolToInt(node.isChecked()));
		int id = (int)mDBHelper.getWritableDatabase().insert(
				mDBHelper.getTableName(clist.getId(), clist.getType()), null, mCV);
		node.setID(id);
		mCV.clear();

		return id;
	}

	public void updateChecklistNode(Checklist clist ,ChecklistNode node){
		mCV.clear();
		mCV.put(DatabaseHelper.COLUMN_NAME, node.getTitle());
		mCV.put(DatabaseHelper.COLUMN_CHECKED, boolToInt(node.isChecked()));
		mDBHelper.getWritableDatabase().update
				(mDBHelper.getTableName(clist.getId(), clist.getType()), mCV,
				DatabaseHelper.COLUMN_ID + "=?" ,
				new String[]{String.valueOf(node.getID())});
		mCV.clear();
	}

	public int insertNewCategory(ChecklistCategory category){
		mCV.clear();
		Cursor cursor = mDBHelper.getWritableDatabase().rawQuery(
				"select max(" + DatabaseHelper.COLUMN_SORTNUM + ") from "
						+ DatabaseHelper.TABLE_CATEGORY, null);
		int sortNum = 0;
		if(cursor.moveToFirst()){
			sortNum  = ((int)cursor.getDouble
//					(cursor.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));
					(cursor.getColumnIndex("max(" + DatabaseHelper.COLUMN_SORTNUM + ")")));
			sortNum++;
		}
		mCV.put(DatabaseHelper.COLUMN_NAME, category.getTitle());
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, sortNum);
		int id =(int) mDBHelper.getWritableDatabase().insert
		(DatabaseHelper.TABLE_CATEGORY , null, mCV);
		return id;
	}

	public void copyChecklistNodes(String fromTableID, String toTableID){
		//TODO チェックリストコピーメソッド

	}

	public void deleteChecklist(int clType, int id){
		switch (clType) {
		case Checklist.CHECKLIST_RUNNING:
			mCV.clear();
			mDBHelper.getWritableDatabase().delete(
					DatabaseHelper.TABLE_CURRENTLIST,
					DatabaseHelper.COLUMN_ID + "=?",
					new String[]{String.valueOf(id)});
			mCV.clear();
			break;
		case Checklist.CHECKLIST_STORE:
			break;
		case Checklist.CHECKLIST_HISTORY:
			mCV.clear();
			mDBHelper.getWritableDatabase().delete(
					DatabaseHelper.TABLE_HISTORYLIST,
					DatabaseHelper.COLUMN_ID + "=?",
					new String[]{String.valueOf(id)});
			mCV.clear();
			break;
		default:
			break;
		}
	}

	public void testDataAdd(Checklist clist){
		int id = insertNewChecklist(clist);
		clist.setId(id);
		for(ChecklistNode node :clist.getNodes()){
			insertChecklistNode(clist, node);
		}
	}

	private boolean intToBool(int i){
		if(i!=0){
			return true;
		}
		return false;
	}
	private int boolToInt(boolean b){
		if(b){
			return 1;
		}
		return 0;
	}

}
