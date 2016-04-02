package todolist.huji.ac.il.todolistv3;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ListItemsArrayAdapter extends ArrayAdapter<ToDoListItem> {

	public ListItemsArrayAdapter(Context context, ArrayList<ToDoListItem> items) {
		super(context, 0, items);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ToDoListItem item = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_todo_list_item_show, parent, false);
		}

		Date now = ToDoListItem.removeTime(new Date());
		Date itemDate = item.getDueDate();

		// Setting the date text and color. items with today due date (regardless the time!) appear in yellow
		String dateText = "";
		int color;
		if (itemDate.after(now)) {
			color = Color.BLACK;
		} else {
			if (itemDate.before(now)) {
				color = Color.RED;
			} else {
				color = Color.YELLOW;
				dateText = " --- TODAY!";
			}
		}
		String backgroundColor = (position % 2 == 0 ? "#7FBFEF" : "#00B0F0");

		convertView.setBackgroundColor(Color.parseColor(backgroundColor));

		final TextView id = (TextView) convertView.findViewById(R.id.txtID);
		final TextView name = (TextView) convertView.findViewById(R.id.txtTodoTitle);
		final TextView dueDate = (TextView) convertView.findViewById(R.id.txtTodoDueDate);


		id.setText(Long.toString(item.getID()));

		name.setText(item.getName());
		name.setTextColor(color);

		dueDate.setText(TodoListManagerActivity.DATE_FORMAT.format(item.getDueDate()) + dateText);
		dueDate.setTextColor(color);

		return (convertView);
	}
}