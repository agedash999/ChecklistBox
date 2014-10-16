package jp.agedash999.android.checklistbox;

import jp.agedash999.android.checklistbox.ChecklistDialogHandler.ChecklistDialogListener;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.view.ContextMenu;

public class ContextMenuHandler implements ChecklistDialogListener{

	public static final int CONTEXT_MENUID_EDIT = 0;
	public static final int CONTEXT_MENUID_DELETE = 1;
	public static final int CONTEXT_MENUID_STOCK = 2;
	public static final int CONTEXT_MENUID_TOHOME = 3;
	public static final int MENU_CREATE = 4;

	private static ContextMenuHandler mInstance = new ContextMenuHandler();

	private MainActivity activity;
	private ContextMenuFragment fragment;
	private int mClistType;
	private Checklist mChecklist;

	private int menuSelected;

	private ContextMenuHandler(){}

	public static ContextMenuHandler getHandler(MainActivity activity,
			ContextMenuFragment fragment, int clistType){
		mInstance.activity = activity;
		mInstance.fragment = fragment;
		mInstance.mClistType = clistType;

		//初期化
		mInstance.mChecklist = null;
		mInstance.menuSelected = -1;

		return mInstance;
	}

	public ContextMenuHandler(MainActivity activity,
			ContextMenuFragment fragment, int clistType){
		this.activity = activity;
		this.fragment = fragment;
	}

	public void prepareContextMenu(ContextMenu menu){
		switch (mClistType) {
		case Checklist.CHECKLIST_RUNNING:
			menu.setHeaderTitle(activity.getString(R.string.conmenu_home_title));
			menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
			menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
			menu.add(0, CONTEXT_MENUID_STOCK, 0, R.string.conmenu_stock);
			break;
		case Checklist.CHECKLIST_STORE:
			menu.setHeaderTitle(activity.getString(R.string.conmenu_home_title));
			menu.add(0, CONTEXT_MENUID_EDIT, 0, R.string.conmenu_edit);
			menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
			menu.add(0, CONTEXT_MENUID_TOHOME, 0, R.string.conmenu_tohome);
			break;
		case Checklist.CHECKLIST_HISTORY:
			menu.setHeaderTitle(activity.getString(R.string.conmenu_home_title));
			menu.add(0, CONTEXT_MENUID_DELETE, 0, R.string.conmenu_delete);
			menu.add(0, CONTEXT_MENUID_STOCK, 0, R.string.conmenu_stock);
			menu.add(0, CONTEXT_MENUID_TOHOME, 0, R.string.conmenu_tohome);
			break;
		default:
			break;
		}
	}

//	public void addMenuSelected(){
//		this.mChecklist = null;
//		this.menuSelected = MENU_CREATE;
//
//		ChecklistDialogHandler handler;
//		handler = ChecklistDialogHandler.getHandlerBlank(ChecklistDialogHandler.FOR_HOME_NEW);
//		handler.setChecklistDialogListener(this);
//		handler.createAlertDialog(activity).show();
//
//	}

	public void contextMenuSelected(int menuType , Checklist clist){
		this.mChecklist = clist;
		this.menuSelected = menuType;

		ChecklistDialogHandler handler;

		switch (menuSelected) {

		case MENU_CREATE:
			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				handler = ChecklistDialogHandler.getHandlerBlank(ChecklistDialogHandler.FOR_HOME_NEW);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_STORE:
				handler = ChecklistDialogHandler.getHandlerBlank(ChecklistDialogHandler.FOR_STOCK_NEW);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_HISTORY:
				//ここには入らない想定
				break;
			default:
				break;
			}

			break;
		case CONTEXT_MENUID_EDIT:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_HOME_EDIT, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_STORE:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_STOCK_EDIT, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_HISTORY:
				//ここには入らない想定
				break;
			default:
				break;
			}

			break;
		case CONTEXT_MENUID_DELETE:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle(R.string.dialog_title_clist_delete_home);
				builder.setMessage(R.string.dialog_message_clist_delete);
				builder.setPositiveButton(R.string.dialog_button_clist_delete, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.getChecklistManager().removeChecklist(mChecklist);
						fragment.onFinishContextMenu(CONTEXT_MENUID_DELETE);
					}
				});
				builder.setNegativeButton(R.string.dialog_button_clist_cansel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						fragment.onContextMenuCanceled(CONTEXT_MENUID_DELETE);
					}
				});
				builder.create().show();

				break;
			case Checklist.CHECKLIST_STORE:
				AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
				builder2.setTitle(R.string.dialog_title_clist_delete_home);
				builder2.setMessage(R.string.dialog_message_clist_delete);
				builder2.setPositiveButton(R.string.dialog_button_clist_delete, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.getChecklistManager().removeChecklist(mChecklist);
						fragment.onFinishContextMenu(CONTEXT_MENUID_DELETE);
					}
				});
				builder2.setNegativeButton(R.string.dialog_button_clist_cansel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						fragment.onContextMenuCanceled(CONTEXT_MENUID_DELETE);
					}
				});
				builder2.create().show();

				break;
			case Checklist.CHECKLIST_HISTORY:
				AlertDialog.Builder builder3 = new AlertDialog.Builder(activity);
				builder3.setTitle(R.string.dialog_title_clist_delete_home);
				builder3.setMessage(R.string.dialog_message_clist_delete);
				builder3.setPositiveButton(R.string.dialog_button_clist_delete, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.getChecklistManager().removeChecklist(mChecklist);
						fragment.onFinishContextMenu(CONTEXT_MENUID_DELETE);
					}
				});
				builder3.setNegativeButton(R.string.dialog_button_clist_cansel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						fragment.onContextMenuCanceled(CONTEXT_MENUID_DELETE);
					}
				});
				builder3.create().show();


				break;
			default:
				break;
			}

			break;
		case CONTEXT_MENUID_STOCK:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_HOME_STORE, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_STORE:
				//ここには入らない想定
				break;
			case Checklist.CHECKLIST_HISTORY:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_HISTORY_STORE, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			default:
				break;
			}

			break;
		case CONTEXT_MENUID_TOHOME:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				//ここには入らない想定
				break;
			case Checklist.CHECKLIST_STORE:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_STOCK_TOHOME, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			case Checklist.CHECKLIST_HISTORY:
				handler = ChecklistDialogHandler.getHandler(ChecklistDialogHandler.FOR_HISTORY_TOHOME, mChecklist);
				handler.setChecklistDialogListener(this);
				handler.createAlertDialog(activity).show();

				break;
			default:
				break;
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onChecklistInfoSave(Checklist clist, int dialogType) {
		// TODO 自動生成されたメソッド・スタブ

		switch (menuSelected) {
		case MENU_CREATE:
			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				activity.getChecklistManager().insertChecklist(clist);
				activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_RUNNING);
				fragment.onFinishContextMenu(MENU_CREATE);

				break;
			case Checklist.CHECKLIST_STORE:
				activity.getChecklistManager().insertChecklist(clist);
				fragment.onFinishContextMenu(MENU_CREATE);

				break;
			case Checklist.CHECKLIST_HISTORY:
				//ここには入らない想定

				break;
			default:
				break;
			}

			break;

		case CONTEXT_MENUID_EDIT:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				activity.getChecklistManager().updateChecklistInfo(clist);
				activity.getChecklistManager().sortChecklist(Checklist.CHECKLIST_RUNNING);
				fragment.onFinishContextMenu(CONTEXT_MENUID_EDIT);

				break;
			case Checklist.CHECKLIST_STORE:
				activity.getChecklistManager().updateChecklistInfo(clist);
				fragment.onFinishContextMenu(CONTEXT_MENUID_EDIT);

				break;
			case Checklist.CHECKLIST_HISTORY:
				//ここには入らない想定

				break;
			default:
				break;
			}


			break;
		case CONTEXT_MENUID_DELETE:
			//ここには入らない想定

			break;
		case CONTEXT_MENUID_STOCK:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				activity.getChecklistManager().insertChecklist(clist);
				fragment.onFinishContextMenu(CONTEXT_MENUID_STOCK);
				//TODO 画面を写す？

				break;
			case Checklist.CHECKLIST_STORE:
				//ここには入らない想定

				break;
			case Checklist.CHECKLIST_HISTORY:
				activity.getChecklistManager().insertChecklist(clist);
				fragment.onFinishContextMenu(CONTEXT_MENUID_STOCK);

				break;
			default:
				break;
			}


			break;
		case CONTEXT_MENUID_TOHOME:

			switch (mClistType) {
			case Checklist.CHECKLIST_RUNNING:
				//ここには入らない想定

				break;
			case Checklist.CHECKLIST_STORE:
				activity.getChecklistManager().insertChecklist(clist);
				fragment.onFinishContextMenu(CONTEXT_MENUID_TOHOME);

				break;
			case Checklist.CHECKLIST_HISTORY:
				activity.getChecklistManager().insertChecklist(clist);
				fragment.onFinishContextMenu(CONTEXT_MENUID_TOHOME);

				break;
			default:
				break;
			}


			break;
		default:
			break;
		}

	}

	@Override
	public void onChecklistInfoCansel() {

	}

	public interface ContextMenuFragment{

		//v4 Fragmentクラスのメソッド
		public FragmentManager getFragmentManager();

		public void onFinishContextMenu(int menuType);

		public void onContextMenuCanceled(int menuType);

	}
}
