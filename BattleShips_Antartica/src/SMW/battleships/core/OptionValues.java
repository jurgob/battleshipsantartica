package SMW.battleships.core;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

 public class OptionValues {
	 static int rows;
	 static int columns;
	 static boolean facebookNotify;
	 public static  Facebook facebook;
	 public static AsyncFacebookRunner myAsyncRunner;
	 
	public static boolean isFacebookNotify() {
		return facebookNotify;
	}
	public static void setFacebookNotify(boolean facebookNotify) {
		OptionValues.facebookNotify = facebookNotify;
	}
	public static int getRows() {
		return rows;
	}
	public static void setRows(int rows) {
		OptionValues.rows = rows;
	}
	public static int getColumns() {
		return columns;
	}
	public static void setColumns(int columns) {
		OptionValues.columns = columns;
	}
	 
	 
	 
}
