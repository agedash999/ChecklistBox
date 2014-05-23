package jp.agedash999.android.checklistbox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChecklistCategory {

	private int id;
	private String title;
	private List<Checklist> childList;
	private double sortNo;

	public ChecklistCategory(int id,String title,double sortNo){
		this.id = id;
		this.title = title;
		this.childList = new ArrayList<Checklist>();
		this.setSortNo(sortNo);
	}

	public ChecklistCategory(String title){
		this(-1,title,-1.0);
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
		//Checklistのカテゴリを変更した時にリストへの追加を行うので、
		//そこからのみ呼ぶ
		this.childList.add(clist);
	}

	public double getSortNo() {
		return sortNo;
	}

	public void setSortNo(double sortNo) {
		this.sortNo = sortNo;
	}

	@Override
	public String toString() {
		return this.getTitle();
	}

	public static class CategorySortNoComp implements Comparator<ChecklistCategory>{

		@Override
		public int compare(ChecklistCategory cat1, ChecklistCategory cat2) {
			if(cat1.getSortNo() < cat2.getSortNo()){
				return -1;
			}else if(cat1.getSortNo() > cat2.getSortNo()){
				return +1;
			}else{
				return 0;
			}
		}
	}

}
