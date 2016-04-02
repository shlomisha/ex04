package todolist.huji.ac.il.todolistv3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoListManagerActivity extends AppCompatActivity {

	public static ListItemsArrayAdapter itemsAdapter;

	public static ArrayList<ToDoListItem> todoList = new ArrayList<ToDoListItem>();

	public static SimpleDateFormat DATE_FORMAT = null;

	private static DBHelper _DB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		DATE_FORMAT = new SimpleDateFormat(getString(R.string.dateStringFormat));

		_DB = new DBHelper(this);

		this.setList();
		ListView list = (ListView) findViewById(R.id.lstTodoItems);
		registerForContextMenu(list);
	}

	private void setList() {

//		this.todoList = new ArrayList<ToDoListItem>();
//		try {
//			this.todoList.add(new ToDoListItem("todo 1", DATE_FORMAT.parse("18/03/2016 23:59")));
//			this.todoList.add(new ToDoListItem("call 03-1234567", DATE_FORMAT.parse("19/03/2016 17:28")));
//			this.todoList.add(new ToDoListItem("todo 3", DATE_FORMAT.parse("22/04/2016 14:20")));
//		this.todoList.add(new ToDoListItem("todo 4", "24/04/2016 10:45"));
//		this.todoList.add(new ToDoListItem("todo 5", "26/04/2016 08:40"));
//		this.todoList.add(new ToDoListItem("todo 6", "28/04/2016 09:15"));
//		} catch (ParseException ex) {
//			// Do nothing for now...
//		}

		todoList = _DB.loadItems();

		final ListView list = (ListView) findViewById(R.id.lstTodoItems);
		final TextView emptyList = (TextView) findViewById(R.id.txtTodoItemsEmpty);

		itemsAdapter = new ListItemsArrayAdapter(this, todoList);
		list.setAdapter(itemsAdapter);

		emptyList.setVisibility(todoList.size() == 0 ? View.VISIBLE : View.GONE);
	}

	public void addTodoItem(String title, Date dueDate) {
		ToDoListItem item = new ToDoListItem(title, dueDate);
		_DB.insert(item);
		todoList.add(item);

		final TextView emptyList = (TextView) findViewById(R.id.txtTodoItemsEmpty);
		emptyList.setVisibility(View.GONE);

		itemsAdapter.notifyDataSetChanged();
	}

	public static void deleteTodoItem(long id) {
		int index = 0;
		for (int i = 0; i < todoList.size(); i++) {
			if (todoList.get(i).getID() == id) {
				todoList.remove(index);
				break;
			}
		}

		ToDoListItem item = new ToDoListItem(id);

		_DB.delete(item);

		itemsAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menuItemAdd) {

			final Intent intent = new Intent(getBaseContext(), AddNewTodoItemActivity.class);
			startActivityForResult(intent, 42);

		}
		if (id == R.id.menuItemExit) {
			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		itemsAdapter.notifyDataSetChanged();

	}

	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == 42) {
			if (resCode == Activity.RESULT_OK) {

				String title = data.getStringExtra("title").toString();
				Date dueDate = (Date) data.getSerializableExtra("dueDate");

				addTodoItem(title, dueDate);
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		final ToDoListItem item = this.itemsAdapter.getItem(info.position);

		menu.setHeaderTitle(item.getName());

		getMenuInflater().inflate(R.menu.todo_list_context_menu, menu);
		MenuItem callItem = menu.getItem(1);

		String callPrefix = getResources().getString(R.string.callPrefix).toLowerCase() + " ";
		if (item.getName().toLowerCase().startsWith(callPrefix) == false) {
			callItem.setVisible(false);
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


		final Context c = this;
		final ToDoListItem todoItem = todoList.get(info.position);

		switch (item.getItemId()) {

			case R.id.menuItemDelete:

				new AlertDialog.Builder(c)
						.setTitle("Deleting a task")
						.setMessage("Are you sure you want to delete \"" + todoItem.getName() + "\"?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								TodoListManagerActivity.deleteTodoItem(todoItem.getID());
								//finish();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Doing nothing, the dialog will discard itself
							}
						}).show();
				break;

			case R.id.menuItemCall:
				String number = todoItem.getName();
				number = number.substring(number.indexOf(" ") + 1);

				Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.uriCallPrefix) + number));
				startActivity(dial);
				break;
		}
		return true;
	}

}
