package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChecklistManager {
	private List<Checklist> runningList; //表示順に格納
	private Map<Integer,List<Checklist>> stockList; //カテゴリID,チェックリストのList 表示順に格納
	private List<Checklist> historyList; //表示順に格納

	private Map<Integer,String> categoryList; //カテゴリID,カテゴリ名 カテゴリ表示順に格納
	//TODO テーブルには id・名称・ソート順　を格納する
	//TODO 新規追加時に重複チェックをする
	//TODO 名称変更時にはDBを書き換える


	public ChecklistManager(){
		this.runningList = new ArrayList<Checklist>();
		this.stockList = null;
		this.historyList = new ArrayList<Checklist>();
		this.categoryList = null;

		addTestData();
	}

	private void addTestData(){
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

	public List<Checklist> getRunningList(){
		return runningList;
	}
	public List<Checklist> getStockList(){
		return new ArrayList<Checklist>();
	}
	public List<Checklist> getHistoryList(){
		return historyList;
	}
	public  Map<Integer,String> getCategoryList(){
		return categoryList;
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
