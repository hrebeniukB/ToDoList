package data;

import android.provider.BaseColumns;


public final class ListContract {
    private ListContract() {
    };

    public static final class TaskList implements BaseColumns {
        public final static String TABLE_NAME = "tasks";
        public final static String COLUMN_TASK = "task";
        public final static String COLUMN_CHECKED = "checked";
    }

}
