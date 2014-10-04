package jp.agedash999.android.checklistbox;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;

public class ChecklistManager {

	private MainActivity mActivity;
	private DBAccess mDBAccess;

	private List<Checklist> runningList; //表示順に格納

//	private List<Integer> categoryOrder; //カテゴリーIDをカテゴリー表示順に格納
//	private Map<Integer,String> categoryList; //カテゴリIDとカテゴリ名の紐付け
//	private Map<Integer,List<Checklist>> stockList; //カテゴリIDと属するチェックリストのList

	private List<ChecklistCategory> stockList; //カテゴリ並び順<チェックリスト並び順>で格納

	private List<Checklist> historyList; //表示順に格納

	//TODO テーブルには id・名称・ソート順　を格納する
	//TODO 新規追加時に重複チェックをする
	//TODO 名称変更時にはDBを書き換える

//	private static ChecklistCategory undef;

	public static final int SORTTYPE_SORTNO = 1;
	public static final int SORTTYPE_DATE_ASC = 2;
	public static final int SORTTYPE_DATE_DESC = 3;
	public static final int SORTTYPE_SORTNO_CHECKED = 4;

	private static final String PREF_KEY_SORT_HOME = "key_sort_home";
	private static final String PREF_KEY_SORT_STOCK = "key_sort_stock";
	private static final String PREF_KEY_SORT_HISTORY = "key_sort_history";
	private static final String PREF_KEY_SORT_NODEHOME = "key_sort_nodehome";
	private static final String PREF_KEY_SORT_NODESTOCK = "key_sort_nodestock";
	private static final String PREF_KEY_SORT_NODEHISTORY = "key_sort_nodehistory";

	public ChecklistManager(MainActivity activity){
		mActivity = activity;
		mDBAccess = new DBAccess(mActivity);

		this.runningList = mDBAccess.getCurrentListAll();
		//TODO テスト用データ生成
		boolean testFlag = false;
		if(runningList.isEmpty()){
			testFlag = true;
			addTestHome();
			addTestHistory();
			addTestStock();
			this.runningList = mDBAccess.getCurrentListAll();
		}
//		this.categoryList = new HashMap<Integer, String>();
//		this.categoryOrder = new ArrayList<Integer>();
//		mDBAccess.getCategory(categoryList, categoryOrder);
//
//		this.stockList = mDBAccess.getStockListAll(categoryList);

		this.stockList = mDBAccess.getStockListAll();

		//カテゴリ未指定の取得
//		Iterator<ChecklistCategory> iter = stockList.iterator();
//		ChecklistCategory cat;
//		boolean finish = false;
//		while(iter.hasNext() && !finish){
//			cat = iter.next();
//			if(cat.getId() == DatabaseHelper.CATEGORY_UNDEFINED_ID){
//				ChecklistManager.undef = cat;
//				finish = true;
//			}
//		}

		if(testFlag){
			addTestStock2();
		}

		this.historyList = mDBAccess.getHistoryListAll();

		//TODO 設定処理差し込み

		SharedPreferences pref = mActivity.getPreference();

		//TODO デバッグ出力
		testRunningDBoutput();
		Log.d("checklist_box", "*************************************************");
		testRunningFieldOutput();

	}

	private void addTestHome(){
		//TODO テストデータ
		Checklist clist1 = new Checklist(Checklist.CHECKLIST_RUNNING, "朝やること", null);
		clist1.addNode("歯磨き", false);
		clist1.addNode("持ち物準備", true);
		clist1.addNode("ストレッチ", false);
		clist1.addNode("天気の確認", false);
		clist1.setMemo("毎朝必ずやるリスト");
		clist1.setDate(new GregorianCalendar(2014, 10, 15));
		mDBAccess.testDataAdd(clist1);

		Checklist clist2 = new Checklist(Checklist.CHECKLIST_RUNNING, "旅行の持ち物（登別）", null);
		clist2.addNode("パンツ", false);
		clist2.addNode("靴下", true);
		clist2.addNode("携帯バッテリー", false);
		clist2.addNode("飲み物", false);
		clist2.addNode("保険証", false);
		clist2.addNode("かばん", false);
		clist2.addNode("財布", true);
		clist2.addNode("タバコ", false);
		clist2.addNode("ライター", true);
		clist2.addNode("爪切り", false);
		clist2.setMemo("朝必ずチェックする");
		clist2.setDate(new GregorianCalendar(2015, 3, 25));
		mDBAccess.testDataAdd(clist2);

		Checklist clist4 = new Checklist(Checklist.CHECKLIST_RUNNING, "市役所でやること", null);
		clist4.addNode("住民票をとる", false);
		clist4.addNode("戸籍の写しを取る", false);
		clist4.addNode("住基カードの発行申請するうううううううううううう", false);
		clist4.setDate(new GregorianCalendar(2014, 12, 20));
		mDBAccess.testDataAdd(clist4);
	}

	private void addTestHistory(){
		//TODO テストデータ
		Checklist clist1 = new Checklist(Checklist.CHECKLIST_HISTORY, "夜やること", null);
		clist1.addNode("歯磨き", true);
		clist1.addNode("明日の持ち物準備", true);
		clist1.addNode("日記を書く", true);
		clist1.addNode("明日の天気の確認", true);
		clist1.setDate(new GregorianCalendar(2014, 9, 20));
		mDBAccess.testDataAdd(clist1);

		Checklist clist2 = new Checklist(Checklist.CHECKLIST_HISTORY, "バイトの面接", null);
		clist2.addNode("履歴書準備", true);
		clist2.addNode("スーツ準備", true);
		clist2.addNode("交通手段確認", true);
		clist1.setDate(new GregorianCalendar(2011, 2, 1));
		mDBAccess.testDataAdd(clist2);

		Checklist clist3 = new Checklist(Checklist.CHECKLIST_HISTORY, "旅行の持ち物（大阪）", null);
		clist3.addNode("パンツ", true);
		clist3.addNode("靴下", true);
		clist3.addNode("携帯バッテリー", true);
		clist3.addNode("飲み物", true);
		clist3.addNode("保険証", true);
		clist3.addNode("かばん", true);
		clist3.addNode("財布", true);
		clist3.addNode("タバコ", true);
		clist3.addNode("ライター", true);
		clist3.addNode("地球の歩き方（大阪）", true);
		clist1.setDate(new GregorianCalendar(2014, 5, 1));
		mDBAccess.testDataAdd(clist3);

		Checklist clist4 = new Checklist(Checklist.CHECKLIST_HISTORY, "市役所でやること", null);
		clist4.addNode("住民票をとる", true);
		clist4.addNode("戸籍の写しを取る", true);
		clist4.addNode("住基カードの発行申請するうううううううううううう", true);
		clist1.setDate(new GregorianCalendar(2014, 7, 28));
		mDBAccess.testDataAdd(clist4);
	}

	private void addTestStock(){

//		List<Checklist> list1 = new ArrayList<Checklist>();
		//TODO テスト用
		int categoryID;

		ChecklistCategory category1 = new ChecklistCategory("一日の生活チェック");
		mDBAccess.insertNewCategory(category1);
		Checklist clist1_1 = new Checklist
				(Checklist.CHECKLIST_STORE, "保存用朝やること", category1);
		clist1_1.addNode("歯磨き", false);
		clist1_1.addNode("明日の持ち物準備", false);
		clist1_1.addNode("日記を書く", false);
		clist1_1.addNode("明日の天気の確認", false);
		clist1_1.setDate(new GregorianCalendar(2014, 7, 28));
		mDBAccess.testDataAdd(clist1_1);

		Checklist clist1_2 = new Checklist
				(Checklist.CHECKLIST_STORE, "保存用昼やること", category1);
		clist1_2.addNode("昼食食べる", false);
		clist1_2.addNode("歯磨き", false);
		clist1_2.addNode("昼寝", false);
		clist1_2.setDate(new GregorianCalendar(2014, 5, 20));
		mDBAccess.testDataAdd(clist1_2);

		Checklist clist1_3 = new Checklist
				(Checklist.CHECKLIST_STORE, "保存用夜やること", category1);
		clist1_3.addNode("寝る準備", false);
		clist1_3.addNode("音楽聞く", false);
		clist1_3.addNode("日記書く", false);
		clist1_3.addNode("ジョギング", false);
		clist1_3.addNode("神に祈る", false);
		clist1_3.setDate(new GregorianCalendar(2014, 8, 8));
		mDBAccess.testDataAdd(clist1_3);

//		stockList.put(98765432, list1);
//		categoryList.put(98765432, "一日の生活チェック");
//		categoryOrder.add(98765432);

//		List<Checklist> list2 = new ArrayList<Checklist>();
		//TODO テスト用
		ChecklistCategory category2 = new ChecklistCategory("仕事関連");
		mDBAccess.insertNewCategory(category2);
		Checklist clist2_1 = new Checklist
				(Checklist.CHECKLIST_STORE, "バイトの朝", category2);
		clist2_1.addNode("行き方確認", false);
		clist2_1.addNode("明日の持ち物準備", false);
		clist2_1.addNode("持ち物をもう一度確認", false);
		clist2_1.addNode("朝食食べる", false);
		clist2_1.setDate(new GregorianCalendar(2014, 6, 8));
		mDBAccess.testDataAdd(clist2_1);

		Checklist clist2_2 = new Checklist
				(Checklist.CHECKLIST_STORE, "バイトの面接", category2);
		clist2_2.addNode("履歴書準備", false);
		clist2_2.addNode("スーツ準備", false);
		clist2_2.addNode("交通手段確認", false);
		clist2_2.setDate(new GregorianCalendar(2012, 6, 8));
		mDBAccess.testDataAdd(clist2_2);

		ChecklistCategory category3 = new ChecklistCategory("旅行関連");
		mDBAccess.insertNewCategory(category3);
		//TODO テスト用
		Checklist clist3_1 = new Checklist
				(Checklist.CHECKLIST_STORE, "旅行一般", category3);
		clist3_1.addNode("靴下", false);
		clist3_1.addNode("パンツ", false);
		clist3_1.addNode("頭痛薬", false);
		clist3_1.addNode("地図", false);
		clist3_1.addNode("保険証", false);
		clist3_1.addNode("たばこ", false);
		clist3_1.addNode("ライター", false);
		clist3_1.addNode("時計", false);
		clist3_1.addNode("財布", false);
		clist3_1.addNode("携帯", false);
		clist3_1.addNode("携帯バッテリー", false);
		mDBAccess.testDataAdd(clist3_1);

		Checklist clist3_2 = new Checklist
				(Checklist.CHECKLIST_STORE, "国内旅行", category3);
		clist3_2.addNode("免許証", false);
		clist3_2.addNode("交通手段確認", false);
		clist3_2.setDate(new GregorianCalendar(2014, 9,14));
		mDBAccess.testDataAdd(clist3_2);

		Checklist clist3_3 = new Checklist
				(Checklist.CHECKLIST_STORE, "海外旅行", category3);
		clist3_3.addNode("地球の歩き方", false);
		clist3_3.addNode("パスポート", false);
		mDBAccess.testDataAdd(clist3_3);

		ChecklistCategory category4 = new ChecklistCategory("空のフォルダテスト");
		mDBAccess.insertNewCategory(category4);
	}

	private void addTestStock2(){
//		Checklist clist5_1 = new Checklist
//				(Checklist.CHECKLIST_STORE, "スポーツジムの持ち物", undef);
//		clist5_1.addNode("着替えのシャツ", false);
//		clist5_1.addNode("着替えのパンツ", false);
//		clist5_1.addNode("着替えの靴下", false);
//		clist5_1.addNode("シューズ", false);
//		clist5_1.addNode("バスタオル", false);
//		clist5_1.addNode("洗面用具", false);
//		clist5_1.addNode("汚れ物入れ", false);
//		clist5_1.addNode("イヤホン", false);
//		clist5_1.addNode("ICカード", false);
//		clist5_1.setMemo("トレーニングウェアは着ている前提");
//		mDBAccess.testDataAdd(clist5_1);
	}

	public List<Checklist> getRunningList(){
		return runningList;
	}

//	public Map<Integer,String> getCategoryList(){
//
//		//そのまま返す
//		return categoryList;
//	}

//	public List<String> getCategoryList(){
//		//表示順に並べ替えたカテゴリタイトルを渡す
//		List<String> list = new ArrayList<String>();
//		Iterator<Integer> i = categoryOrder.iterator();
//		while(i.hasNext()){
//			list.add(categoryList.get(i.next()));
//		}
//		return list;
//	}
//
//	public List<Integer> getCategoryOrder(){
//		return categoryOrder;
//	}

//	public List<Checklist> getStockListFromCategory(int category){
//		//TODO 渡されたカテゴリーIDのチェックリストを取得
//		//事前にフィールドに保存しておいて、そこから取ってくる
//		return stockList.get(category);
//	}

//	public void getStockAndCategory(
//			List<String> p_categoryList,
//			List<List<Checklist>> p_stockList){
//		//TODO 格納し直すと、オブジェクト管理が複雑になるので要検討
//		Iterator<Integer> i = categoryOrder.iterator();
//		while(i.hasNext()){
//			int categoryID = i.next();
//			p_categoryList.add(categoryList.get(categoryID));
//			p_stockList.add(stockList.get(categoryID));
//		}
//	}

//	public void getStockAndCategory(
//			Map<Integer,String> p_categoryList,
//			Map<Integer,List<Checklist>> p_stockList){
//		p_categoryList = this.categoryList;
//		p_stockList = this.stockList;
//	}

//	public Map<Integer,List<Checklist>> getStockList(){
//		return stockList;
//	}

	public List<ChecklistCategory> getStockList(){
		return stockList;
	}

	public List<Checklist> getHistoryList(){
		return historyList;
	}

//	public static ChecklistCategory getCategoryUndefined(){
//		return ChecklistManager.undef;
//	}

	public void nodeUpdated(Checklist clist, ChecklistNode node){
		//TODO 並べ替え？
		//データベース更新（カラム値変更のみ）
		mDBAccess.updateChecklistNode(clist, node);

	}

	public void checklistFinished(){
		//リストの入れ替え（現行→履歴）
		//データベース更新（現行→履歴）
		//完了日付を設定
	}

	public void addNode(Checklist clist,ChecklistNode node){
		//データベース更新（チェックリストTBLにinsert）
		mDBAccess.insertChecklistNode(clist, node);
		clist.addNode(node);
	}

	public void addChecklist(){
		//新規作成の場合
		//テーブル作成

		//保存CLからのコピーの場合
		//テーブル作成
	}

	public void moveRunningList(Checklist clist , int to){
		runningList.remove(clist);
		runningList.add(to, clist);
//		double sortNo;
		if(to == 0){
			//挿入先が一番最初の場合
			clist.setSortNo(0.0);
			Checklist clistNext = runningList.get(to + 1);
			clistNext.setSortNo( runningList.get(to + 2).getSortNo() / 2 );
			updateChecklistInfo(clistNext);
			updateChecklistInfo(clist);

		}else if(to >= runningList.size() - 1 ){
			//挿入先が一番最後の場合
			double prevNo = runningList.get(to - 1).getSortNo();
			double sortNo = ((int)prevNo) + 1;
			clist.setSortNo(sortNo);
			updateChecklistInfo(clist);
		}else{
			double prevNo = runningList.get(to - 1).getSortNo();
			double nextNo = runningList.get(to + 1).getSortNo();
			clist.setSortNo(prevNo + ( ( nextNo - prevNo ) / 2 ));
			updateChecklistInfo(clist);
		}

		Log.d("checklist_box", clist.getTitle() + " " + to + " " + clist.getSortNo());
		testRunningDBoutput();
		Log.d("checklist_box", "*************************************************");
		testRunningFieldOutput();
	}

	public void moveNode(Checklist clist , ChecklistNode node , int to){
		List<ChecklistNode> list = clist.getChecklist();
		list.remove(node);
		list.add(to, node);

		if(to == 0){
			//挿入先が一番最初の場合
			node.setSortNo(0.0);
			ChecklistNode nodeNext = list.get(to + 1);
			nodeNext.setSortNo( list.get(to + 2).getSortNo() / 2 );
			updateNodeInfo(clist, nodeNext);
			updateNodeInfo(clist, node);

		}else if( to >= list.size() - 1 ){
			//挿入先が一番最後の場合
			double prevNo = list.get(to - 1).getSortNo();
			double sortNo = ((int)prevNo) + 1;
			node.setSortNo(sortNo);
			updateNodeInfo(clist, node);
		}else{
			double prevNo = list.get(to - 1).getSortNo();
			double nextNo = list.get(to + 1).getSortNo();
			node.setSortNo(prevNo + ( ( nextNo - prevNo ) / 2 ));
			updateNodeInfo(clist, node);
		}

		//TODO テスト出力
		Log.d("checklist_box", node.getTitle() + " " + to + " " + node.getSortNo());
		testNodeDBOutput(clist);
		Log.d("checklist_box", "*************************************************");
		testNodeFieldOutput(clist);
	}

	public void checklistMove(){
		//カテゴリ変更がある場合、カテゴリ変更
		//リストの格納順変更

		//データベースも、カテゴリ変更がある場合、カテゴリ変更
		//データベース更新
	}

	public void removeNode(Checklist clist, ChecklistNode node){
		//リストから削除

		//データベース更新
	}

	public void removeChecklist(Checklist clist){
		//TODO データベース更新（テーブル削除）
		//リストから削除
		int clType = clist.getType();
		switch (clType) {
		case Checklist.CHECKLIST_RUNNING:
			mDBAccess.deleteChecklist(clType, clist.getId());
			runningList.remove(clist);
			break;
		case Checklist.CHECKLIST_STORE:
			mDBAccess.deleteChecklist(clType, clist.getId());
			clist.getCategory().getChildList().remove(clist);
			break;
		case Checklist.CHECKLIST_HISTORY:
			mDBAccess.deleteChecklist(clType, clist.getId());
			historyList.remove(clist);
			break;
		default:
			break;
		}
	}

	public void insertChecklist(Checklist clist){
		//データベース更新
		//リストに追加

		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			mDBAccess.insertNewChecklist(clist);
			mDBAccess.insertChecklistNodes(clist);
			runningList.add(clist);
			break;
		case Checklist.CHECKLIST_STORE:
			clist.getCategory().getId();//TODO nullじゃないことを確認
			mDBAccess.insertNewChecklist(clist);
			mDBAccess.insertChecklistNodes(clist);
//			clist.getCategory().addChecklist(clist);
			break;
		case Checklist.CHECKLIST_HISTORY:
			mDBAccess.insertNewChecklist(clist);
			mDBAccess.insertChecklistNodes(clist);
			historyList.add(clist);
			break;
//		default:
//			break;
		}
	}

	public void updateChecklistInfo(Checklist clist){
		if(clist.getType() == Checklist.CHECKLIST_STORE){
			clist.getCategory().getId(); //TODO nullじゃないことを確認
		}
		mDBAccess.updateChecklistInfo(clist);
	}

	public void completeChecklist(Checklist clist){
		//TODO 全チェック入れる？
		clist.setType(Checklist.CHECKLIST_HISTORY);
		mDBAccess.completeChecklist(clist);
		runningList.remove(clist);
		historyList.add(clist);
	}

	public void updateNodeInfo(Checklist clist , ChecklistNode node){
		mDBAccess.updateChecklistNode(clist, node);
	}

	private void testRunningFieldOutput(){
		Iterator<Checklist> i = runningList.iterator();
		while(i.hasNext()){
			Checklist clist = i.next();
			String str =
					rightPad(Integer.toString(clist.getId()),4) +
					rightPad(clist.getTitle() ,20) +
					rightPad(clist.getMemo() ,20) +
//					rightPad(clist.getCategory().getTitle() ,18) +
					rightPad(clist.getDateFormated() ,16) +
					rightPad(Double.toString(clist.getSortNo()) ,4);
			Log.d("checklist_box", str);
		}
	}

	private void testRunningDBoutput(){
		List<Checklist> list = mDBAccess.getCurrentListAll();
		Iterator<Checklist> i = list.iterator();
		while(i.hasNext()){
			Checklist clist = i.next();
			String str =
					rightPad(Integer.toString(clist.getId()),4) +
					rightPad(clist.getTitle() ,20) +
					rightPad(clist.getMemo() ,20) +
//					rightPad(clist.getCategory().getTitle() ,18) +
					rightPad(clist.getDateFormated() ,16) +
					rightPad(Double.toString(clist.getSortNo()) ,4);
			Log.d("checklist_box", str);
		}
	}

	private void testNodeFieldOutput(Checklist clist){
		Iterator<ChecklistNode> i = clist.getChecklist().iterator();
		while(i.hasNext()){
			ChecklistNode node = i.next();
			String str =
					rightPad(Integer.toString(node.getID()), 4) +
					rightPad(node.getTitle(), 20) +
					rightPad(Double.toString(node.getSortNo()), 4);
			Log.d("checklist_box", str);
		}
	}

	private void testNodeDBOutput(Checklist clist){
		List<ChecklistNode> list = mDBAccess.testGetChecklist(clist);
		Iterator<ChecklistNode> i = list.iterator();
		while(i.hasNext()){
			ChecklistNode node = i.next();
			String str =
					rightPad(Integer.toString(node.getID()), 4) +
					rightPad(node.getTitle(), 20) +
					rightPad(Double.toString(node.getSortNo()), 4);
			Log.d("checklist_box", str);
		}
	}

	private String rightPad(String str, int size) {
	    StringBuilder sb = new StringBuilder();

	    sb.append(str);

	    for (int i = str.length(); i < size; i++) {
	        sb.append(" ");
	    }

	    return sb.toString();
	}

	public void sortChecklist(int cltype){
		int sortType = getChecklistSortType(cltype);
		if(sortType==SORTTYPE_SORTNO){
			sortChecklistSortNo(cltype);
		}else if(sortType== SORTTYPE_DATE_ASC){
			sortChecklistDate(cltype ,false);
		}else if(sortType== SORTTYPE_DATE_DESC){
			sortChecklistDate(cltype ,true);
		}else{
			//ここには入らない
		}
	}

	public void sortNode(Checklist clist){
		int sortType = getNodeSortType(clist.getType());
		if(sortType==SORTTYPE_SORTNO_CHECKED){
			sortNodeSortNo(clist);
			sortNodeChecked(clist);
		}else if(sortType==SORTTYPE_SORTNO){
			sortNodeSortNo(clist);
		}else{
			//ここには入らない
		}

		Log.d("checklist_box", "*************************************************");
		testNodeDBOutput(clist);
		Log.d("checklist_box", "*************************************************");
		testNodeFieldOutput(clist);

	}

	public void sortCategory(){
		if(getCategorySortType()==SORTTYPE_SORTNO){
			sortCategorySortNo();
		}else{
			//ここには入らない
		}
	}

	private void sortChecklistSortNo(int cltype){
		if(cltype == Checklist.CHECKLIST_RUNNING){
			Collections.sort(runningList, new Checklist.ChecklistSortNoComp());
		}else if(cltype == Checklist.CHECKLIST_STORE){
			//ここには入らない
		}else if(cltype == Checklist.CHECKLIST_HISTORY){
			//ここには入らない
		}else{
			//ここには入らない
		}
	}

	private void sortChecklistDate(int cltype, boolean isReverse){
		List<Checklist> list;
		if(cltype == Checklist.CHECKLIST_RUNNING){
			list = runningList;
		}else if(cltype == Checklist.CHECKLIST_STORE){
			//ここには入らない
			list = runningList;
		}else if(cltype == Checklist.CHECKLIST_HISTORY){
			//ここには入らない
			list = runningList;
		}else{
			//ここには入らない
			list = runningList;
		}
		Collections.sort(list, new Checklist.ChecklistDateComp());
		if(isReverse){
			Collections.reverse(list);
		}
	}

	public void moveCategory(ChecklistCategory category , int to){
		stockList.remove(category);
		stockList.add(to, category);
		if(to == 0){
			//挿入先が一番最初の場合
			category.setSortNo(0.0);
			ChecklistCategory catNext = stockList.get(to + 1);
			catNext.setSortNo( stockList.get(to + 2).getSortNo() / 2 );
			updateCategoryInfo(category);

		}else if( to >= stockList.size() - 1 ){
			//挿入先が一番最後の場合
			double prevNo = stockList.get(to - 1).getSortNo();
			double sortNo = ((int)prevNo) + 1;
			category.setSortNo(sortNo);
			updateCategoryInfo(category);
		}else{
			double prevNo = stockList.get(to - 1).getSortNo();
			double nextNo = stockList.get(to + 1).getSortNo();
			category.setSortNo(prevNo + ( ( nextNo - prevNo ) / 2 ));
			updateCategoryInfo(category);
		}

		//TODO テスト出力
		Log.d("checklist_box", category.getTitle() + " " + to + " " + category.getSortNo());
		testCategoryDBOutput();
		Log.d("checklist_box", "*************************************************");
//		testCategoryFieldOutput();
	}

	public void updateCategoryInfo(ChecklistCategory category){
		mDBAccess.updateCategoryInfo(category);
	}

	public void testCategoryFieldOutput(){
		Iterator<ChecklistCategory> i = stockList.iterator();
		while(i.hasNext()){
			ChecklistCategory category = i.next();
			String str =
					rightPad(Integer.toString(category.getId()), 4) +
					rightPad(category.getTitle(), 20) +
					rightPad(Double.toString(category.getSortNo()), 4);
			Log.d("checklist_box", str);
		}
	}

	public void insertCategory(ChecklistCategory category){
		//TODO 後で動作確認
		mDBAccess.insertNewCategory(category);
		stockList.add(category);
	}

	public void deleteCategory(ChecklistCategory category){
		Iterator<Checklist> iter = category.getChildList().iterator();
		while(iter.hasNext()){
			Checklist clist = iter.next();
			mDBAccess.deleteChecklist(Checklist.CHECKLIST_STORE, clist.getId());
		}
		mDBAccess.deleteCategoryInfo(category);
		stockList.remove(category);
	}

	public void moveChecklistToCategory(ChecklistCategory from, ChecklistCategory to){
		to.getChildList().addAll(from.getChildList());
		from.getChildList().clear();
//		Iterator<Checklist> iter = from.getChildList().iterator();
//		while(iter.hasNext()){
//			Checklist list = iter.next();
//			list.setCategory(to);
//		}
//		from.getChildList().clear();
	}

	private void testCategoryDBOutput(){
		List<ChecklistCategory> list = mDBAccess.testGetCategorylist();
		Iterator<ChecklistCategory> i = list.iterator();
		while(i.hasNext()){
			ChecklistCategory category = i.next();
			String str =
					rightPad(Integer.toString(category.getId()), 4) +
					rightPad(category.getTitle(), 20) +
					rightPad(Double.toString(category.getSortNo()), 4);
			Log.d("checklist_box", str);
		}
	}

	private void sortNodeSortNo(Checklist clist){
		Collections.sort(clist.getChecklist(), new ChecklistNode.NodeSortNoComp());
	}

	private void sortNodeChecked(Checklist clist){
		Collections.sort(clist.getChecklist(), new ChecklistNode.NodeCheckedComp());
	}

	private void sortCategorySortNo(){
		Collections.sort(stockList, new ChecklistCategory.CategorySortNoComp());
	}

	public int getChecklistSortType(int clType){
		if(clType == Checklist.CHECKLIST_RUNNING){
			return mActivity.getPreference().getInt(PREF_KEY_SORT_HOME, SORTTYPE_SORTNO);
		}else if(clType == Checklist.CHECKLIST_STORE){
			return SORTTYPE_SORTNO;
		}else if(clType == Checklist.CHECKLIST_HISTORY){
			return SORTTYPE_DATE_DESC;
		}
		//TODO 例外
		return 0;
	}

	public int getNodeSortType(int clType){
		if(clType == Checklist.CHECKLIST_RUNNING){
			return mActivity.getPreference().getInt(PREF_KEY_SORT_NODEHOME, SORTTYPE_SORTNO_CHECKED);
		}else if(clType == Checklist.CHECKLIST_STORE){
			return SORTTYPE_SORTNO;
		}else if(clType == Checklist.CHECKLIST_HISTORY){
			return SORTTYPE_SORTNO;
		}
		//TODO 例外
		return 0;
	}

	public int getCategorySortType(){
		return SORTTYPE_SORTNO;
	}

	public void setChecklistSortType(int clType, int sortType){
		if(clType == Checklist.CHECKLIST_RUNNING){
			if(1 <= sortType & sortType <= 3){
				SharedPreferences.Editor editor = mActivity.getPreference().edit();
				editor.putInt(PREF_KEY_SORT_HOME, sortType);
				editor.apply();
			}else{
				//TODO 例外
				throw new Error();
			}
		}else if(clType == Checklist.CHECKLIST_STORE){
			//いまのところ不要 任意のみ
			//TODO 例外
			throw new Error();

		}else if(clType == Checklist.CHECKLIST_HISTORY){
			//いまのところ不要 日付降順のみ
			//TODO 例外
			throw new Error();
		}
	}

	public void setNodeSortType(int clType, int sortType){
		if(clType == Checklist.CHECKLIST_RUNNING){
			if(1 == sortType | sortType == 4){
				SharedPreferences.Editor editor = mActivity.getPreference().edit();
				editor.putInt(PREF_KEY_SORT_NODEHOME, sortType);
				editor.apply();
			}else{
				//TODO 例外
				throw new Error();
			}
		}else if(clType == Checklist.CHECKLIST_STORE){
			//いまのところ不要 任意のみ
			//TODO 例外
			throw new Error();
		}else if(clType == Checklist.CHECKLIST_HISTORY){
			//いまのところ不要 任意のみ
			//TODO 例外
			throw new Error();
		}
	}
}
