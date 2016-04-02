package todolist.huji.ac.il.todolistv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String _TABLE_NAME = "todo_db";
	private SQLiteDatabase _db = null;

	public DBHelper(Context context) {
		super(context, _TABLE_NAME, null, DATABASE_VERSION);
		this.createTable();
	}

	public void onCreate(SQLiteDatabase db) {
		this._db = db;

		createTable();
	}

	private void createTable() {
		if (this._db == null) {
			this._db = this.getWritableDatabase();
		}

		String sql = "create table if not exists " + _TABLE_NAME + " (" +
				" id integer primary key autoincrement," +
				" title text," +
				" due long);";

		this._db.execSQL(sql);
	}

	private void dropTable() {
		if (this._db == null) {
			this._db = this.getWritableDatabase();
		}

		String sql = "drop table if exists " + _TABLE_NAME + ";";
		this._db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.dropTable();
		this.createTable();
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.dropTable();
		this.createTable();
	}

	public void insert(ToDoListItem item) {
		if (this._db == null) {
			this._db = this.getWritableDatabase();
		}

		ContentValues contentValues = new ContentValues();

		contentValues.put("title", item.getName());
		contentValues.put("due", TodoListManagerActivity.DATE_FORMAT.format(item.getDueDate()));
		long id = this._db.insert(_TABLE_NAME, null, contentValues);
		item.setID(id);
	}

	public void delete(ToDoListItem item) {
		if (this._db == null) {
			this._db = this.getWritableDatabase();
		}

		String[] parameters = new String[]{Long.toString(item.getID())};

		this._db.delete(_TABLE_NAME, "(id =?)", parameters);
	}

	public ArrayList<ToDoListItem> loadItems() {
		if (this._db == null) {
			this._db = this.getWritableDatabase();
		}

		ToDoListItem item;
		int id;
		String title;
		Date dueDate;
		ArrayList<ToDoListItem> items = new ArrayList<>();

		String sql = "select * " +
				"from " + _TABLE_NAME + " " +
				"order by id;";

		Cursor cursor = cursor = this._db.rawQuery(sql, null);
		//getReadableDatabase().rawQuery(sql, null);

		int indexID = cursor.getColumnIndex("id");
		int indexTitle = cursor.getColumnIndex("title");
		int indexDue = cursor.getColumnIndex("due");

		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {

			id = cursor.getInt(indexID);
			title = cursor.getString(indexTitle);
			try {
				dueDate = TodoListManagerActivity.DATE_FORMAT.parse(cursor.getString(indexDue));
			} catch (ParseException e) {
				dueDate = null;
			}

			item = new ToDoListItem(id, title, dueDate);
			items.add(item);

			cursor.moveToNext();
		}

		cursor.close();

		return (items);
	}
}
