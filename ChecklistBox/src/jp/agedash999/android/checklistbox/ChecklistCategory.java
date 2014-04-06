package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.List;

public class ChecklistCategory {

	private int id;
	private String title;
	private List<Checklist> childList;

	public ChecklistCategory(String title){
		this.title = title;
		this.childList = new ArrayList<Checklist>();
	}

	public ChecklistCategory(int id,String title){
		this.id = id;
		this.title = title;
		this.childList = new ArrayList<Checklist>();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public List<Checklist> getChildList() {
		return childList;
	}
	public void setChildList(List<Checklist> childList) {
		this.childList = childList;
	}

	public void addChecklist(Checklist clist){
		this.childList.add(clist);
	}

	@Override
	public String toString() {
		return this.getTitle();
	}
}
