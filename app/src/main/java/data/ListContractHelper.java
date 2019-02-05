package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.ListContract.TaskList;


public class ListContractHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ListContractHelper.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "tasks.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link ListContractHelper}.
     *
     * @param context Контекст приложения
     */
    public ListContractHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + TaskList.TABLE_NAME + " ("
                + TaskList.COLUMN_TASK + " TEXT NOT NULL, "
                + TaskList.COLUMN_CHECKED + " TEXT NOT NULL );";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void ItemStatusUpdate(SQLiteDatabase db, String task, String checked) {
        db.delete(TaskList.TABLE_NAME, TaskList.COLUMN_TASK + " = ?", new String[]{task});
        ContentValues values = new ContentValues();
        values.put(TaskList.COLUMN_TASK, task);
        values.put(TaskList.COLUMN_CHECKED, checked);
        db.insert(TaskList.TABLE_NAME, null, values);

    }

    public static void ItemDelete(SQLiteDatabase db, String task) {
        db.delete(TaskList.TABLE_NAME, TaskList.COLUMN_TASK + " = ?", new String[]{task});
    }
}