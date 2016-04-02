package todolist.huji.ac.il.todolistv3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_todo_list_add);
		final Context c = this;

		final Button buttonAdd = (Button) findViewById(R.id.btnOK);
		final Button buttonCancel = (Button) findViewById(R.id.btnCancel);
		final EditText editTextTitle = (EditText) findViewById(R.id.edtNewItem);
		final DatePicker datePickerDueDate = (DatePicker) findViewById(R.id.datePicker);

		buttonAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = editTextTitle.getText().toString();
				Intent result = new Intent();

				if (title.equals("")) {

					new AlertDialog.Builder(c)
							.setTitle("Add new item")
							.setMessage("Item title can not be left empty")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();

					return;
				}

				Date dueDate = new Date(datePickerDueDate.getCalendarView().getDate());

				result.putExtra("title", title);
				result.putExtra("dueDate", dueDate);

				setResult(Activity.RESULT_OK, result);
				finish();
			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}