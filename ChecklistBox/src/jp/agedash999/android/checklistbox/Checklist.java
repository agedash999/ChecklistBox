package jp.agedash999.android.checklistbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
//	private int categoryID;
	private ChecklistCategory category;
	private Calendar date;//実施日または完了日
	private List<ChecklistNode> checklist;

	public Checklist(int id,int cltype, String title, String memo, ChecklistCategory category ,long timeInMillis){

		this.id = id;
		this.cltype = cltype;
		if(title == null){
			this.title = "";
		}else{
			this.title = title;
		}
		if(memo == null){
			this.memo = "";
		}else{
			this.memo = memo;
		}

		if(cltype == CHECKLIST_RUNNING || cltype == CHECKLIST_HISTORY){
			this.category = null;
		}else if(cltype == CHECKLIST_STORE){
			category.getId();//null確認
			this.category = category;
		}
//		if(categoryID == 0){
//			this.setCategoryID(DatabaseHelper.CATEGORY_UNDEFINED_ID);
//		}else{
//			this.setCategoryID(categoryID);
//		}

		this.date = Calendar.getInstance();
		date.setTimeInMillis(timeInMillis);
		this.checklist = new ArrayList<ChecklistNode>();
	}

	public Checklist(int cltype, String title,ChecklistCategory category,Calendar date){
		this.cltype = cltype;
		this.title = title;
		if(cltype == CHECKLIST_RUNNING || cltype == CHECKLIST_HISTORY){
			this.category = null;
		}else if(cltype == CHECKLIST_STORE){
			this.category = category;
		}
//		if(categoryID == 0){
//			this.setCategoryID(DatabaseHelper.CATEGORY_UNDEFINED_ID);
//		}else{
//			this.setCategoryID(categoryID);
//		}
		this.date = date;
		this.checklist = new ArrayList<ChecklistNode>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType(){
		return this.cltype;
	}

	public int getID(){
		return this.id;
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
		this.category.getChildList().remove(this);
		if(!category.getChildList().contains(this)){
			category.addChecklist(this);
		}
		this.category = category;
	}
}
