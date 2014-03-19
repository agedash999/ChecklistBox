package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChecklistManager {
	private List<Checklist> runningList; //表示順に格納

	private List<Integer> categoryOrder; //カテゴリーIDをカテゴリー表示順に格納
	private Map<Integer,String> categoryList; //カテゴリIDとカテゴリ名の紐付け
	private Map<Integer,List<Checklist>> stockList; //カテゴリIDと属するチェックリストのList

	private List<Checklist> historyList; //表示順に格納

	//TODO テーブルには id・名称・ソート順　を格納する
	//TODO 新規追加時に重複チェックをする
	//TODO 名称変更時にはDBを書き換える


	public ChecklistManager(){
		this.runningList = new ArrayList<Checklist>();
		this.stockList = new HashMap<Integer, List<Checklist>>();
		this.historyList = new ArrayList<Checklist>();
		this.categoryList = new HashMap<Integer, String>();
		this.categoryOrder = new ArrayList<Integer>();

		addTestHome();
		addTestHistory();
		addTestStock();
	}

	private void addTestHome(){
		//TODO モックデータ
		ArrayList<ChecklistNode> list1 = new ArrayList<ChecklistNode>();
		Checklist clist1 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "朝やること", 0,new Date(0), list1);
		clist1.addNode("歯磨き", false);
		clist1.addNode("持ち物準備", true);
		clist1.addNode("ストレッチ", false);
		clist1.addNode("天気の確認", false);
		runningList.add(clist1);

		ArrayList<ChecklistNode> list2 = new ArrayList<ChecklistNode>();
		Checklist clist2 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "旅行の持ち物（国内）", 0,new Date(10000), list2);
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
		runningList.add(clist2);

		ArrayList<ChecklistNode> list3 = new ArrayList<ChecklistNode>();
		Checklist clist3 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "旅行の持ち物（海外）", 0,new Date(30000), list3);
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
		runningList.add(clist3);

		ArrayList<ChecklistNode> list4 = new ArrayList<ChecklistNode>();
		Checklist clist4 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "市役所でやること", 0,new Date(70000), list4);
		clist4.addNode("住民票をとる", false);
		clist4.addNode("戸籍の写しを取る", false);
		clist4.addNode("住基カードの発行申請するうううううううううううう", false);
		runningList.add(clist4);
	}

	private void addTestHistory(){
		//TODO モックデータ
		ArrayList<ChecklistNode> list1 = new ArrayList<ChecklistNode>();
		Checklist clist1 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "夜やること", 0,new Date(0), list1);
		clist1.addNode("歯磨き", false);
		clist1.addNode("明日の持ち物準備", true);
		clist1.addNode("日記を書く", false);
		clist1.addNode("明日の天気の確認", false);
		historyList.add(clist1);

		ArrayList<ChecklistNode> list2 = new ArrayList<ChecklistNode>();
		Checklist clist2 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "バイトの面接", 0,new Date(10000), list2);
		clist2.addNode("履歴書準備", false);
		clist2.addNode("スーツ準備", true);
		clist2.addNode("交通手段確認", false);
		historyList.add(clist2);

		ArrayList<ChecklistNode> list3 = new ArrayList<ChecklistNode>();
		Checklist clist3 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "旅行の持ち物（カザフスタン）", 0,new Date(30000), list3);
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
		historyList.add(clist3);

		ArrayList<ChecklistNode> list4 = new ArrayList<ChecklistNode>();
		Checklist clist4 = new Checklist(0, Checklist.CHECKLIST_RUNNING, "市役所でやること", 0,new Date(70000), list4);
		clist4.addNode("住民票をとる", false);
		clist4.addNode("戸籍の写しを取る", false);
		clist4.addNode("住基カードの発行申請するうううううううううううう", false);
		historyList.add(clist4);
	}

	private void addTestStock(){

		List<Checklist> list1 = new ArrayList<Checklist>();
		//TODO テスト用
		Checklist clist1_1 = new Checklist(52789823, Checklist.CHECKLIST_RUNNING, "保存用夜やること", 0,new Date(0));
		clist1_1.addNode("歯磨き", false);
		clist1_1.addNode("明日の持ち物準備", true);
		clist1_1.addNode("日記を書く", false);
		clist1_1.addNode("明日の天気の確認", false);
		list1.add(clist1_1);

		Checklist clist1_2 = new Checklist(23515824, Checklist.CHECKLIST_RUNNING, "保存用昼やること", 0,new Date(10000));
		clist1_2.addNode("昼食食べる", false);
		clist1_2.addNode("歯磨き", true);
		clist1_2.addNode("昼寝", false);
		list1.add(clist1_2);

		Checklist clist1_3 = new Checklist(48328712, Checklist.CHECKLIST_RUNNING, "保存用夜やること", 0,new Date(10000));
		clist1_3.addNode("寝る準備", false);
		clist1_3.addNode("音楽聞く", true);
		clist1_3.addNode("日記書く", false);
		clist1_3.addNode("ジョギング", false);
		clist1_3.addNode("神に祈る", false);
		list1.add(clist1_3);

		stockList.put(98765432, list1);
		categoryList.put(98765432, "一日の生活チェック");
		categoryOrder.add(98765432);

		List<Checklist> list2 = new ArrayList<Checklist>();
		//TODO テスト用
		Checklist clist2_1 = new Checklist(19431284, Checklist.CHECKLIST_RUNNING, "バイトの朝", 0,new Date(0));
		clist2_1.addNode("行き方確認", false);
		clist2_1.addNode("明日の持ち物準備", true);
		clist2_1.addNode("持ち物をもう一度確認", false);
		clist2_1.addNode("朝食食べる", false);
		list2.add(clist2_1);

		Checklist clist2_2 = new Checklist(71481294, Checklist.CHECKLIST_RUNNING, "バイトの面接", 0,new Date(10000));
		clist2_2.addNode("履歴書準備", false);
		clist2_2.addNode("スーツ準備", true);
		clist2_2.addNode("交通手段確認", false);
		list2.add(clist2_2);

		stockList.put(61849394, list2);
		categoryList.put(61849394, "仕事関連");
		categoryOrder.add(61849394);

		List<Checklist> list3 = new ArrayList<Checklist>();
		//TODO テスト用
		Checklist clist3_1 = new Checklist(48283918, Checklist.CHECKLIST_RUNNING, "旅行一般", 0,new Date(0));
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
		list3.add(clist3_1);

		Checklist clist3_2 = new Checklist(59281293, Checklist.CHECKLIST_RUNNING, "国内旅行", 0,new Date(10000));
		clist3_2.addNode("免許証", false);
		clist3_2.addNode("交通手段確認", false);
		list3.add(clist3_2);

		Checklist clist3_3 = new Checklist(91818181, Checklist.CHECKLIST_RUNNING, "海外旅行", 0,new Date(10000));
		clist3_3.addNode("地球の歩き方", false);
		clist3_3.addNode("パスポート", true);
		list3.add(clist3_3);

		stockList.put(28378192, list3);
		categoryList.put(28378192, "旅行関連");
		categoryOrder.add(28378192);

		List<Checklist> list4 = new ArrayList<Checklist>();
		//TODO テスト用

		stockList.put(48193481, list4);
		categoryList.put(48193481, "カテゴリ未指定");
		categoryOrder.add(48193481);

		//TODO 別のカテゴリーもセット

	}

	public List<Checklist> getRunningList(){
		return runningList;
	}

	public Map<Integer,String> getCategoryList(){
//		//表示順に並べ替えたカテゴリタイトルを渡す
//		List<String> list = new ArrayList<String>();
//		Iterator<Integer> i = categoryOrder.iterator();
//		while(i.hasNext()){
//			list.add(categoryList.get(i.next()));
//		}
//		return list;

		//そのまま返す
		return categoryList;
	}
	public List<Checklist> getStockListFromCategory(int category){
		//TODO 渡されたカテゴリーIDのチェックリストを取得
		//事前にフィールドに保存しておいて、そこから取ってくる
		return stockList.get(category);
	}
	public void getStockAndCategory(
			List<String> p_categoryList,
			List<List<Checklist>> p_stockList){
		Iterator<Integer> i = categoryOrder.iterator();
		while(i.hasNext()){
			int categoryID = i.next();
			p_categoryList.add(categoryList.get(categoryID));
			p_stockList.add(stockList.get(categoryID));
		}
	}
//	public Map<Integer,List<Checklist>> getStockList(){
//		return stockList;
//	}
	public List<Checklist> getHistoryList(){
		return historyList;
	}

	public void nodeChecked(){
		//チェックをtrueに設定

		//データベース更新（カラム値変更のみ）
	}

	public void checklistFinished(){
		//リストの入れ替え（現行→履歴）
		//データベース更新（現行→履歴）
		//完了日付を設定
	}

	public void nodeAdded(){
		//データベース更新（チェックリストTBLにinsert）
	}

	public void checklistAdded(){
		//新規作成の場合
		//テーブル作成

		//保存CLからのコピーの場合
		//テーブル作成
	}

	public void nodeMoved(){
		//リストの格納順変更

		//データベース更新

	}

	public void checklistMoved(){
		//カテゴリ変更がある場合、カテゴリ変更
		//リストの格納順変更

		//データベースも、カテゴリ変更がある場合、カテゴリ変更
		//データベース更新
	}

	public void nodeRemoved(){
		//リストから削除

		//データベース更新
	}

	public void checklistRemoved(){
		//リストから削除

		//データベース更新（テーブル削除）
	}

	public void checklistStored(){
		//リストに追加

		//データベース更新
	}
}
