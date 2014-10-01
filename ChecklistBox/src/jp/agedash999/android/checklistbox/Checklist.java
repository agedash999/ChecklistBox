package jp.agedash999.android.checklistbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Checklist {
	public static final int CHECKLIST_RUNNING = 1;
	public static final int CHECKLIST_STORE = 2;
	public static final int CHECKLIST_HISTORY = 3;

	private int id = -1;
	private int cltype = -1;

	private String title;
	private String memo;
	private ChecklistCategory category;
	private Calendar date;//実施日または完了日
	private double sortNo;
	private List<ChecklistNode> checklist;


	//内部使用
	private Checklist(int id,int cltype, String title, String memo,
			ChecklistCategory category ,Calendar date,double sortNo){

		this.id = id;
		this.cltype = cltype;

		if(title == null){
			title = "";
		}else{
			this.title = title;
		}

		if(memo == null){
			this.memo = "";
		}else{
			this.memo = memo;
		}

		//TODO カテゴリーがnullだった場合の処理
//		if(cltype == CHECKLIST_RUNNING || cltype == CHECKLIST_HISTORY){
//			this.category = category;
//		}else if(cltype == CHECKLIST_STORE){
////			category.getId();//null確認
//			this.category = category;
//		}

		this.category = category;

		this.date = date;

		this.setSortNo(sortNo);
		this.checklist = new ArrayList<ChecklistNode>();
	}

	//DBテーブルから取得した時に使用
	public Checklist(int id,int cltype, String title, String memo,
			ChecklistCategory category ,long timeInMillis,double sortNo){
		this(id,cltype,title,memo,category,Calendar.getInstance(),sortNo);
		this.getDate().setTimeInMillis(timeInMillis);
	}

	//UI上で新規作成するときに使用
	public Checklist(int cltype, String title,ChecklistCategory category){
		this(-1,cltype,title,"",category,Calendar.getInstance(),-1);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//TODO 必要？
	public void setType(int clType){
		this.cltype = clType;
	}

	public int getType(){
		return this.cltype;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return this.title;
	}

	public void setMemo(String memo){
		this.memo = memo;
	}

	public String getMemo(){
		return this.memo;
	}

	public void setDate(Calendar date){
		this.date = date;
	}

	public Calendar getDate(){
		return this.date;
	}

	public List<ChecklistNode> getChecklist() {
		return checklist;
	}

	public List<ChecklistNode> getChecklistCopy(boolean forCheckCopy) {
		Iterator<ChecklistNode> it = checklist.iterator();
		List<ChecklistNode> list = new ArrayList<ChecklistNode>();
		boolean checked;
		while(it.hasNext()){
			ChecklistNode n = it.next();
			if(forCheckCopy){
				checked = n.isChecked();
			}else{
				checked = false;
			}
			ChecklistNode node = new ChecklistNode(n.getTitle().toString(), checked);
			list.add(node);
		}
		return list;
	}

	public void setChecklist(List<ChecklistNode> checklist) {
		this.checklist = checklist;
	}

	public void clearNodes(){
		this.checklist.clear();
	}

	public void addNode(ChecklistNode node){
		this.checklist.add(node);
	}

	@Deprecated
	public void addNode(String title,boolean checked){
		this.checklist.add(new ChecklistNode(title, checked));
	}

	public List<ChecklistNode> getNodes(){
		return checklist;
	}

//	public int getCategoryID() {
//		return categoryID;
//	}
//
//	public void setCategoryID(int categoryID) {
//		this.categoryID = categoryID;
//	}

	public String getDateFormated(){
		//TODO ロケール考慮
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E)");
		return sdf.format(date.getTime());
	}

	public ChecklistCategory getCategory() {
		return category;
	}

	public void setCategory(ChecklistCategory category) {
		//TODO 追加実装ポイント：並べ替え
		if(this.cltype != CHECKLIST_STORE){
			return;
		}

		if(this.category != null){
			this.category.getChildList().remove(this);
		}

		if(!category.getChildList().contains(this)){
			category.addChecklist(this);
		}
		this.category = category;
	}

	public double getSortNo() {
		return sortNo;
	}

	public void setSortNo(double sortNo) {
		this.sortNo = sortNo;
	}

	public int getUncheckedQty(){
		int i = 0;
		Iterator<ChecklistNode> iter = checklist.iterator();
		while(iter.hasNext()){
			ChecklistNode node = iter.next();
			if(!node.isChecked()) i++;
		}
		return i;
	}

	public int getNodeQty(){
		return checklist.size();
	}

	public static class ChecklistSortNoComp implements Comparator<Checklist>{

		@Override
		public int compare(Checklist clist1, Checklist clist2) {
			if(clist1.getSortNo() < clist2.getSortNo()){
				return -1;
			}else if(clist1.getSortNo() > clist2.getSortNo()){
				return +1;
			}else{
				return 0;
			}
		}
	}

	public static class ChecklistDateComp implements Comparator<Checklist>{
		@Override
		public int compare(Checklist clist1, Checklist clist2) {
			if(clist1.getDate().getTimeInMillis() < clist2.getDate().getTimeInMillis()){
				return -1;
			}else if(clist1.getDate().getTimeInMillis() > clist2.getDate().getTimeInMillis()){
				return +1;
			}else{
				return 0;
			}
		}
	}
}
