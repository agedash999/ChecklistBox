package jp.agedash999.android.checklistbox;

public class ChecklistNode {
	private String title;
	private boolean checked;

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
}
