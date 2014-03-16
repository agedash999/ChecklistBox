package jp.agedash999.android.checklistbox;

import java.util.Date;
import java.util.List;

public class Checklist {
	public static final int CHECKLIST_RUNNING = 1;
	public static final int CHECKLIST_STORE = 2;
	public static final int CHECKLIST_HISTORY = 3;

	private int id;
	private int cltype;
	private String title;
	private int categoryID;
	private Date date;//期限または完了日
	private List<ChecklistNode> checklist;

	public Checklist(int id,int cltype, String title,int categoryID,Date date, List<ChecklistNode> checklist){
		this.id = id;
		this.cltype = cltype;
		this.title = title;
		this.categoryID = categoryID;
		this.date = date;
		this.checklist = checklist;
	}

	public String getTitle(){
		return this.title;
	}

	public Date getDate(){
		return this.date;
	}

	public void addNode(ChecklistNode node){
		this.checklist.add(node);
	}

	public void addNode(String title,boolean checked){
		this.checklist.add(new ChecklistNode(title, checked));
	}

	public List<ChecklistNode> getNodes(){
		return checklist;
	}
}
