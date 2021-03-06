package com.example.finalapp.Reminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalapp.R;
import com.example.finalapp.Reminder.ReminderFrag;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddToDoFrag extends Fragment {

//    EditText selectDate;
//    EditText selectTime;
    TextView selectDate;
    TextView selectTime;
    EditText content;
    TextView saveTodo;
    int hour, minute;
    int year, month, dayOfMonth;
    Calendar c;

    public AddToDoFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_do, container, false);

        content = (EditText) view.findViewById(R.id.editContent);
        saveTodo = (TextView) view.findViewById(R.id.saveTodo);
        selectDate = (TextView) view.findViewById(R.id.selectDate);
        selectTime = (TextView) view.findViewById(R.id.selectTime);


        ImageButton goToReminder = (ImageButton) view.findViewById(R.id.goToReminder);
        goToReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.clearFocus();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new ReminderFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectDate(view);
            }
        });
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectTime(view);
            }
        });
        saveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveTodo(view);
            }
        });

        return view;
    }

    public void onClickSelectDate(View view) {
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                year = selectedYear;
                month = selectedMonth + 1;
                dayOfMonth = selectedDayOfMonth;
                selectDate.setText(String.format("%02d/%02d/%02d", year, month, dayOfMonth));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), style, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    public void onClickSelectTime(View view) {
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                selectTime.setText(String.format("%02d:%02d", hour, minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void onClickSaveTodo(View view) {
        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        if(TextUtils.isEmpty(content.getText())) {
            content.setError("Please Input Content of Todo");
            return;
        }
        int newId = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, "todos") + 1;
//        int newId = dbHelper.getTodoCnt();
        System.out.println(newId);
        dbHelper.saveTodos(content.getText().toString(), selectDate.getText().toString(), selectTime.getText().toString(), Integer.toString(newId));
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        Fragment fragment = new ReminderFrag();
        fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
    }
}