package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
				Checklist clist = makeChecklist(Checklist.CHECKLIST_RUNNING, cursor, null);
//				Checklist clist = new Checklist(
//						cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
//						Checklist.CHECKLIST_RUNNING,
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
//						null,
//						cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXPDATE)),
//						cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));
				readChecklistNodes(clist);
				currentList.add(clist);
				cursor.moveToNext();
			}
		}
		mCV.clear();
		return currentList;
	}

//	public void getCategory(Map<Integer,String> categoryList, List<Integer> categoryOrder ){
//		mCV.clear();
//		//TODO 並べ替え
//		Cursor cursor = mDBHelper.getReadableDatabase().rawQuery
//				("select * from " + DatabaseHelper.TABLE_CATEGORY, null);
//		if(cursor.moveToFirst()){
//			while(!cursor.isAfterLast()){
//				int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
//				categoryList.put(
//						id
//						,cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
//				categoryOrder.add(id);
//				cursor.moveToNext();
//			}
//		}
//		mCV.clear();
//	}

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
						cursorCategory.getString(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
						cursorCategory.getDouble(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));
				String selectStr = DatabaseHelper.COLUMN_CATEGORYID + "=?";
				String[] selectionArgs = { Integer.toString(category.getId()) };
				Cursor cursorStock = mDBHelper.getReadableDatabase().query(
						DatabaseHelper.TABLE_STOCKLIST, null, selectStr, selectionArgs, null, null, null);
				if(cursorStock.moveToFirst()){
					while(!cursorStock.isAfterLast()){
						Checklist clist = makeChecklist(Checklist.CHECKLIST_STORE, cursorStock, category);
						clist.setCategory(category);
						readChecklistNodes(clist);
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
				Checklist clist = makeChecklist(Checklist.CHECKLIST_HISTORY, cursor, null);
//				Checklist clist = new Checklist(
//						cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
//						Checklist.CHECKLIST_HISTORY,
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
//						cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
//						null,
//						cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_FINDATE)),
//						cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));
				readChecklistNodes(clist);
				historyList.add(clist);
				cursor.moveToNext();
			}
		}
		mCV.clear();
		return historyList;
	}

	private Checklist makeChecklist(int cltype,Cursor cursor,ChecklistCategory category){
		Checklist clist = new Checklist(
				cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
				cltype,
				cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
				cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMO)),
				category,
				cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
				cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));

		return clist;

	}

	private ChecklistNode makeChecklistNode(Cursor cursor){
		ChecklistNode node = new ChecklistNode(
				cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
				intToBool(cursor.getInt
						(cursor.getColumnIndex(DatabaseHelper.COLUMN_CHECKED))),
						cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));

		return node;
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
				ChecklistNode node = makeChecklistNode(cursorNodes);
				clist.addNode(node);
				cursorNodes.moveToNext();
			}
		}
	}

	public void completeChecklist(Checklist clist){

		//TODO もう少しメソッドを整理したい
		//ホームから削除
		deleteChecklist(Checklist.CHECKLIST_RUNNING, clist.getId());
		insertChecklistCV(clist, DatabaseHelper.TABLE_HISTORYLIST);

	}

	public void updateChecklistInfo(Checklist clist){
		String tableName;
		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			tableName = DatabaseHelper.TABLE_CURRENTLIST;
			updateChecklistCV(clist, tableName);

//			mCV.clear();
//			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
//			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
//			mCV.put(DatabaseHelper.COLUMN_CATEGORYID, DatabaseHelper.CATEGORY_UNDEFINED_ID);
//			mCV.put(DatabaseHelper.COLUMN_SORTNUM, clist.getSortNo());
//			mCV.put(DatabaseHelper.COLUMN_DATE, clist.getDate().getTimeInMillis());
//
//			mDBHelper.getWritableDatabase().update
//					(DatabaseHelper.TABLE_CURRENTLIST, mCV,
//					DatabaseHelper.COLUMN_ID + "=?" ,
//					new String[]{String.valueOf(clist.getId())});
//			mCV.clear();

			break;
		case Checklist.CHECKLIST_STORE:
			tableName = DatabaseHelper.TABLE_STOCKLIST;
			updateChecklistCV(clist, tableName);

//			mCV.clear();
//			mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
//			mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
//			mCV.put(DatabaseHelper.COLUMN_CATEGORYID, clist.getCategory().getId());
//			mCV.put(DatabaseHelper.COLUMN_SORTNUM, clist.getSortNo());
//			mCV.put(DatabaseHelper.COLUMN_DATE, clist.getDate().getTimeInMillis());
//			mDBHelper.getWritableDatabase().update
//					(DatabaseHelper.TABLE_STOCKLIST, mCV,
//					DatabaseHelper.COLUMN_ID + "=?" ,
//					new String[]{String.valueOf(clist.getId())});
//			mCV.clear();

			break;
		case Checklist.CHECKLIST_HISTORY:
			//不要？新規作成のみ

			break;
		default:
			break;
		}
	}

	private void updateChecklistCV(Checklist clist, String tableName){
		int categoryId = DatabaseHelper.CATEGORY_UNDEFINED_ID;
		if(clist.getCategory() != null){
			categoryId = clist.getCategory().getId();
		}
		mCV.clear();
		mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
		mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());
		mCV.put(DatabaseHelper.COLUMN_CATEGORYID, categoryId);
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, clist.getSortNo());
		mCV.put(DatabaseHelper.COLUMN_DATE, clist.getDate().getTimeInMillis());
		mDBHelper.getWritableDatabase().update
				(tableName, mCV,
				DatabaseHelper.COLUMN_ID + "=?" ,
				new String[]{String.valueOf(clist.getId())});
		mCV.clear();

	}

	public void insertNewChecklist(Checklist clist){
		String tableName;
		int id = -1;
		double sortNo = -1.0;
		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			tableName = DatabaseHelper.TABLE_CURRENTLIST;
			clist.setSortNo(getNewSortNo(tableName));
			insertChecklistCV(clist, tableName);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getId(), clist.getType()));
			break;

		case Checklist.CHECKLIST_STORE:
			tableName = DatabaseHelper.TABLE_STOCKLIST;
			clist.setSortNo(getNewSortNo(tableName));
			insertChecklistCV(clist, tableName);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getId(),clist.getType()));
			break;

		case Checklist.CHECKLIST_HISTORY:
			tableName = DatabaseHelper.TABLE_HISTORYLIST;
			clist.setSortNo(getNewSortNo(tableName));
			insertChecklistCV(clist, tableName);

			mDBHelper.getWritableDatabase().execSQL(
					mDBHelper.makeCTBLString(clist.getId(),clist.getType()));
			break;

		default:
			break;
		}
	}

	private void insertChecklistCV(Checklist clist, String tableName){
		int categoryId = DatabaseHelper.CATEGORY_UNDEFINED_ID;
		if(clist.getCategory() != null){
			categoryId = clist.getCategory().getId();
		}
		mCV.clear();
		mCV.put(DatabaseHelper.COLUMN_NAME, clist.getTitle());
		mCV.put(DatabaseHelper.COLUMN_MEMO, clist.getMemo());

		mCV.put(DatabaseHelper.COLUMN_CATEGORYID, categoryId);
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, clist.getSortNo());
		mCV.put(DatabaseHelper.COLUMN_DATE, clist.getDate().getTimeInMillis());
		int id = (int) mDBHelper.getWritableDatabase().insert(
				tableName ,null,mCV);
		mCV.clear();
		clist.setId(id);
	}

	private int getNewSortNo(String tableName){
		int sortNo = 0;
		Cursor cursor = mDBHelper.getWritableDatabase().rawQuery(
				"select max(" + DatabaseHelper.COLUMN_SORTNUM + ") from "
						+ tableName, null);
		if(cursor.moveToFirst()){
			sortNo  = ((int)cursor.getDouble
					(cursor.getColumnIndex("max(" + DatabaseHelper.COLUMN_SORTNUM + ")")));
			sortNo++;
		}
		return sortNo;
	}

	public void insertChecklistNodes(Checklist clist){
		Iterator<ChecklistNode> it = clist.getNodes().iterator();
		ChecklistNode node;
		int id;
		while(it.hasNext()){
			node = it.next();
			insertChecklistNode(clist, node);
		}
	}

	public void insertChecklistNode(Checklist clist ,ChecklistNode node){
		mCV.clear();
		String tableName = mDBHelper.getTableName(clist.getId(), clist.getType());
		int sortNum = getNewSortNo(tableName);

		//TODO ノードID取得・セット
		mCV.put(DatabaseHelper.COLUMN_NAME, node.getTitle());
		mCV.put(DatabaseHelper.COLUMN_CHECKED, boolToInt(node.isChecked()));
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, sortNum);
		int id = (int)mDBHelper.getWritableDatabase().insert(
				tableName, null, mCV);
		node.setID(id);
		node.setSortNo(sortNum);
		mCV.clear();
	}

	public void updateChecklistNode(Checklist clist ,ChecklistNode node){
		mCV.clear();
		String tableName = mDBHelper.getTableName(clist.getId(), clist.getType());
		mCV.put(DatabaseHelper.COLUMN_NAME, node.getTitle());
		mCV.put(DatabaseHelper.COLUMN_CHECKED, boolToInt(node.isChecked()));
		mDBHelper.getWritableDatabase().update
				(tableName, mCV,DatabaseHelper.COLUMN_ID + "=?" ,
				new String[]{String.valueOf(node.getID())});
		mCV.clear();
	}

	public void insertNewCategory(ChecklistCategory category){
		mCV.clear();
		String tableName = DatabaseHelper.TABLE_CATEGORY;
		int sortNum = getNewSortNo(tableName);
		mCV.put(DatabaseHelper.COLUMN_NAME, category.getTitle());
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, sortNum);
		int id =(int) mDBHelper.getWritableDatabase().insert
		(tableName , null, mCV);
		category.setId(id);
		mCV.clear();
	}

	public void deleteCategoryInfo(ChecklistCategory category){
		mCV.clear();
		mDBHelper.getWritableDatabase().delete(
				DatabaseHelper.TABLE_CATEGORY, DatabaseHelper.COLUMN_ID + "=?",
				new String[]{String.valueOf(category.getId()) });
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

	public void updateCategoryInfo(ChecklistCategory category){
		mCV.clear();
		mCV.put(DatabaseHelper.COLUMN_NAME, category.getTitle());
		mCV.put(DatabaseHelper.COLUMN_SORTNUM, category.getSortNo());
		mDBHelper.getWritableDatabase().update
				(DatabaseHelper.TABLE_CATEGORY , mCV,DatabaseHelper.COLUMN_ID + "=?" ,
				new String[]{String.valueOf(category.getId())});
		mCV.clear();
	}


	public void testDataAdd(Checklist clist){
		insertNewChecklist(clist);
		for(ChecklistNode node :clist.getNodes()){
			insertChecklistNode(clist, node);
		}
	}

	public List<ChecklistNode> testGetChecklist(Checklist clist){
//		clist.clearNodes();
		List<ChecklistNode> list = new ArrayList<ChecklistNode>();
		//TODO 並べ替え
		Cursor cursorNodes = mDBHelper.getReadableDatabase().rawQuery
				("select * from " + mDBHelper.
						getTableName(clist.getId(), clist.getType())
						, null);
		if(cursorNodes.moveToFirst()){
			while(!cursorNodes.isAfterLast()){
				ChecklistNode node = makeChecklistNode(cursorNodes);
				list.add(node);
				cursorNodes.moveToNext();
			}
		}
		return list;
	}

	public List<ChecklistCategory> testGetCategorylist(){

		List<ChecklistCategory> categoryList = new ArrayList<ChecklistCategory>();
		Cursor cursorCategory = mDBHelper.getReadableDatabase().rawQuery
				("select * from " + DatabaseHelper.TABLE_CATEGORY, null);
		if(cursorCategory.moveToFirst()){
			while(!cursorCategory.isAfterLast()){
				//TODO 追加実装ポイント：カテゴリ内並び替え
				ChecklistCategory category = new ChecklistCategory(
						cursorCategory.getInt(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_ID)),
						cursorCategory.getString(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
						cursorCategory.getDouble(cursorCategory.getColumnIndex(DatabaseHelper.COLUMN_SORTNUM)));
				categoryList.add(category);
				cursorCategory.moveToNext();
			}
		}
		mCV.clear();
		return categoryList;
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
