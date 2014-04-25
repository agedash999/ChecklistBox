package jp.agedash999.android.checklistbox;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

	private static ChecklistCategory undef;

	public static final int SORTTYPE_SORTNO = 1;
	public static final int SORTTYPE_DATE_ASC = 2;
	public static final int SORTTYPE_DATE_DESC = 3;
	public static final int SORTTYPE_SORTNO_CHECKED = 4;

	private int runninglistSortType;
	private int nodeSortType;
	private int categorySortType;

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
		Iterator<ChecklistCategory> iter = stockList.iterator();
		ChecklistCategory cat;
		boolean finish = false;
		while(iter.hasNext() && !finish){
			cat = iter.next();
			if(cat.getId() == DatabaseHelper.CATEGORY_UNDEFINED_ID){
				ChecklistManager.undef = cat;
				finish = true;
			}
		}

		if(testFlag){
			addTestStock2();
		}

		this.historyList = mDBAccess.getHistoryListAll();

		//TODO 設定処理差し込み
		runninglistSortType = SORTTYPE_SORTNO;
		nodeSortType = SORTTYPE_SORTNO_CHECKED;
		categorySortType = SORTTYPE_SORTNO;

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
		mDBAccess.testDataAdd(clist1);

		Checklist clist2 = new Checklist(Checklist.CHECKLIST_RUNNING, "旅行の持ち物（国内）", null);
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
		mDBAccess.testDataAdd(clist2);

		Checklist clist3 = new Checklist(Checklist.CHECKLIST_RUNNING, "旅行の持ち物（海外）", null);
		clist3.addNode("パンツ", false);
		clist3.addNode("靴下", true);
		clist3.addNode("携帯バッテリー", false);
		clist3.addNode("飲み物", false);
		clist3.addNode("保険証", false);
		clist3.addNode("かばん", false);
		clist3.addNode("財布", true);
		clist3.addNode("タバコ", false);
		clist3.addNode("ライター", true);
		clist3.addNode("爪切り", false);
		clist3.setMemo("");
		mDBAccess.testDataAdd(clist3);

		Checklist clist4 = new Checklist(Checklist.CHECKLIST_RUNNING, "市役所でやること", null);
		clist4.addNode("住民票をとる", false);
		clist4.addNode("戸籍の写しを取る", false);
		clist4.addNode("住基カードの発行申請するうううううううううううう", false);
		mDBAccess.testDataAdd(clist4);
	}

	private void addTestHistory(){
		//TODO テストデータ
		Checklist clist1 = new Checklist(Checklist.CHECKLIST_HISTORY, "夜やること", null);
		clist1.addNode("歯磨き", false);
		clist1.addNode("明日の持ち物準備", true);
		clist1.addNode("日記を書く", false);
		clist1.addNode("明日の天気の確認", false);
		mDBAccess.testDataAdd(clist1);

		Checklist clist2 = new Checklist(Checklist.CHECKLIST_HISTORY, "バイトの面接", null);
		clist2.addNode("履歴書準備", false);
		clist2.addNode("スーツ準備", true);
		clist2.addNode("交通手段確認", false);
		mDBAccess.testDataAdd(clist2);

		Checklist clist3 = new Checklist(Checklist.CHECKLIST_HISTORY, "旅行の持ち物（カザフスタン）", null);
		clist3.addNode("パンツ", false);
		clist3.addNode("靴下", true);
		clist3.addNode("携帯バッテリー", false);
		clist3.addNode("飲み物", false);
		clist3.addNode("保険証", false);
		clist3.addNode("かばん", false);
		clist3.addNode("財布", true);
		clist3.addNode("タバコ", false);
		clist3.addNode("ライター", true);
		clist3.addNode("地球の歩き方（カザフスタン）", false);
		mDBAccess.testDataAdd(clist3);

		Checklist clist4 = new Checklist(Checklist.CHECKLIST_HISTORY, "市役所でやること", null);
		clist4.addNode("住民票をとる", false);
		clist4.addNode("戸籍の写しを取る", false);
		clist4.addNode("住基カードの発行申請するうううううううううううう", false);
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
		clist1_1.addNode("明日の持ち物準備", true);
		clist1_1.addNode("日記を書く", false);
		clist1_1.addNode("明日の天気の確認", false);
		mDBAccess.testDataAdd(clist1_1);

		Checklist clist1_2 = new Checklist
				(Checklist.CHECKLIST_STORE, "保存用昼やること", category1);
		clist1_2.addNode("昼食食べる", false);
		clist1_2.addNode("歯磨き", true);
		clist1_2.addNode("昼寝", false);
		mDBAccess.testDataAdd(clist1_2);

		Checklist clist1_3 = new Checklist
				(Checklist.CHECKLIST_STORE, "保存用夜やること", category1);
		clist1_3.addNode("寝る準備", false);
		clist1_3.addNode("音楽聞く", true);
		clist1_3.addNode("日記書く", false);
		clist1_3.addNode("ジョギング", false);
		clist1_3.addNode("神に祈る", false);
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
		clist2_1.addNode("明日の持ち物準備", true);
		clist2_1.addNode("持ち物をもう一度確認", false);
		clist2_1.addNode("朝食食べる", false);
		mDBAccess.testDataAdd(clist2_1);

		Checklist clist2_2 = new Checklist
				(Checklist.CHECKLIST_STORE, "バイトの面接", category2);
		clist2_2.addNode("履歴書準備", false);
		clist2_2.addNode("スーツ準備", true);
		clist2_2.addNode("交通手段確認", false);
		mDBAccess.testDataAdd(clist2_2);

		ChecklistCategory category3 = new ChecklistCategory("旅行関連");
		mDBAccess.insertNewCategory(category3);
		//TODO テスト用
		Checklist clist3_1 = new Checklist
				(Checklist.CHECKLIST_STORE, "旅行一般", category3);
		clist3_1.addNode("靴下", false);
		clist3_1.addNode("パンツ", true);
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
		mDBAccess.testDataAdd(clist3_2);

		Checklist clist3_3 = new Checklist
				(Checklist.CHECKLIST_STORE, "海外旅行", category3);
		clist3_3.addNode("地球の歩き方", false);
		clist3_3.addNode("パスポート", true);
		mDBAccess.testDataAdd(clist3_3);

		ChecklistCategory category4 = new ChecklistCategory("空のカテゴリテスト");
		mDBAccess.insertNewCategory(category4);
	}

	private void addTestStock2(){
		Checklist clist5_1 = new Checklist
				(Checklist.CHECKLIST_STORE, "スポーツジムの持ち物", undef);
		clist5_1.addNode("着替えのシャツ", false);
		clist5_1.addNode("着替えのパンツ", false);
		clist5_1.addNode("着替えの靴下", false);
		clist5_1.addNode("シューズ", false);
		clist5_1.addNode("バスタオル", false);
		clist5_1.addNode("洗面用具", false);
		clist5_1.addNode("汚れ物入れ", false);
		clist5_1.addNode("イヤホン", false);
		clist5_1.addNode("ICカード", false);
		clist5_1.setMemo("トレーニングウェアは着ている前提");
		mDBAccess.testDataAdd(clist5_1);
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

	public static ChecklistCategory getCategoryUndefined(){
		return ChecklistManager.undef;
	}

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

	public void moveNode(){
		//リストの格納順変更

		//データベース更新

	}

	public void checklistMove(){
		//カテゴリ変更がある場合、カテゴリ変更
		//リストの格納順変更

		//データベースも、カテゴリ変更がある場合、カテゴリ変更
		//データベース更新
	}

	public void removeNode(){
		//リストから削除

		//データベース更新
	}

	public void removeChecklist(Checklist clist){
		//データベース更新（テーブル削除）
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

		int id;
		switch (clist.getType()) {
		case Checklist.CHECKLIST_RUNNING:
			mDBAccess.insertNewChecklist(clist);
			mDBAccess.insertChecklistNodes(clist);
			runningList.add(clist);
			break;
		case Checklist.CHECKLIST_STORE:
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
		mDBAccess.updateChecklistInfo(clist);
	}

	public void insertCategory(ChecklistCategory category){
		//TODO 後で動作確認
		mDBAccess.insertNewCategory(category);
		stockList.add(category);
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

	private String rightPad(String str, int size) {
	    StringBuilder sb = new StringBuilder();

	    sb.append(str);

	    for (int i = str.length(); i < size; i++) {
	        sb.append(" ");
	    }

	    return sb.toString();
	}

	public void sortChecklist(int cltype){
		if(runninglistSortType==SORTTYPE_SORTNO){
			sortChecklistSortNo(cltype);
		}else if(runninglistSortType== SORTTYPE_DATE_ASC){
			sortChecklistDate(cltype ,false);
		}else if(runninglistSortType== SORTTYPE_DATE_DESC){
			sortChecklistDate(cltype ,true);
			//TODO reverse
		}else{
			//ここには入らない
		}
	}

	public void sortNode(Checklist clist){
		if(nodeSortType==SORTTYPE_SORTNO_CHECKED){
			sortNodeSortNo(clist);
			sortNodeChecked(clist);
		}else if(nodeSortType==SORTTYPE_SORTNO){
			sortNodeSortNo(clist);
		}else{
			//ここには入らない
		}
	}

	public void sortCategory(){
		if(categorySortType==SORTTYPE_SORTNO){
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

	private void sortNodeSortNo(Checklist clist){
		Collections.sort(clist.getChecklist(), new ChecklistNode.NodeSortNoComp());
	}

	private void sortNodeChecked(Checklist clist){
		Collections.sort(clist.getChecklist(), new ChecklistNode.NodeCheckedComp());
	}

	private void sortCategorySortNo(){
		Collections.sort(stockList, new ChecklistCategory.CategorySortNoComp());
	}

	private void reverseChecklist(int cltype){

	}

	public int getRunninglistSortType() {
		return runninglistSortType;
	}

	public void setRunninglistSortType(int runninglistSortType) {
		this.runninglistSortType = runninglistSortType;
	}

	public int getNodeSortType() {
		return nodeSortType;
	}

	public void setNodeSortType(int nodeSortType) {
		this.nodeSortType = nodeSortType;
	}

	public int getCategorySortType() {
		return categorySortType;
	}

	public void setCategorySortType(int categorySortType) {
		this.categorySortType = categorySortType;
	}

}
