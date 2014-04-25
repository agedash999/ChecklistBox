package jp.agedash999.android.checklistbox;

import java.util.Comparator;

public class ChecklistNode {
	private int id;
	private String title;
	private boolean checked;
	private double sortNo;

	public ChecklistNode(int ID, String title,boolean checked,double sortNo){
		this.id = ID;
		this.title = title;
		this.checked = checked;
		this.setSortNo(sortNo);
	}

	public ChecklistNode(String title,boolean checked){
		this(-1,title,checked,-1);
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public double getSortNo() {
		return sortNo;
	}

	public void setSortNo(double sortNo) {
		this.sortNo = sortNo;
	}

	public static class NodeSortNoComp implements Comparator<ChecklistNode>{

		@Override
		public int compare(ChecklistNode node1, ChecklistNode node2) {
			if(node1.getSortNo() < node2.getSortNo()){
				return -1;
			}else if(node1.getSortNo() > node2.getSortNo()){
				return +1;
			}else{
				return 0;
			}
		}
	}

	public static class NodeCheckedComp implements Comparator<ChecklistNode>{

		@Override
		public int compare(ChecklistNode node1, ChecklistNode node2) {
			if(node1.isChecked() == node2.isChecked()){
				return 0;
			}else if(!node1.isChecked() && node2.isChecked()){
				return -1;
			}else{
				return +1;
			}
		}
	}
}
