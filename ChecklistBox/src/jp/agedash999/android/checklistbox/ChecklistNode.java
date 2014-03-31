package jp.agedash999.android.checklistbox;

public class ChecklistNode {
	private int id;
	private String title;
	private boolean checked;

	public ChecklistNode(int ID, String title,boolean checked){
		this.id = ID;
		this.title = title;
		this.checked = checked;
	}

	public ChecklistNode(String title,boolean checked){
		this.title = title;
		this.checked = checked;
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
}
