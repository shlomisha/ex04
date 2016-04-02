package todolist.huji.ac.il.todolistv3;

import java.util.Calendar;
import java.util.Date;

public class ToDoListItem {

	private static long _lastID = 0;

	private long _id;
	private String _name;
	private Date _dueDate;

	public ToDoListItem(String name, Date dueDate) {
		//_lastID++;
		this(++_lastID, name, dueDate);
	}

	public ToDoListItem(long id, String name, Date dueDate) {
		this._id = id;
		this._name = name;
		this._dueDate = removeTime((Date) dueDate.clone());
	}

	public ToDoListItem(long id) {
		this._id = id;
	}

	public void setID(long id) {
		this._id = id;
	}

	public long getID() {
		return (this._id);
	}

	public String getName() {
		return (this._name);
	}

	public Date getDueDate() {
		return ((Date) this._dueDate.clone());
	}

	public static Date removeTime(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return (calendar.getTime());
	}

}