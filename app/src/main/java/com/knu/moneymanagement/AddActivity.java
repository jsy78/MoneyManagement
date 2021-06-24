package com.knu.moneymanagement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.database.StaticVariable;

import java.util.Calendar;
import java.util.Objects;

public class AddActivity extends AppCompatActivity implements Constant {

    RadioGroup radioGroup;
    RadioButton radioButton, radioButtonIncome, radioButtonExpen;
    DatePicker datePicker;
    EditText detailText, moneyText;
    FloatingActionButton dateReset, detailReset, moneyReset;
    Button save, cancel;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        radioGroup = findViewById(R.id.radioGroupAdd);
        radioButtonIncome = findViewById(R.id.rbtn_income_add);
        radioButtonExpen = findViewById(R.id.rbtn_expen_add);
        datePicker = findViewById(R.id.datePickerAdd);
        detailText = findViewById(R.id.detailEditAdd);
        moneyText = findViewById(R.id.moneyEditAdd);
        dateReset = findViewById(R.id.dateResetAdd);
        detailReset = findViewById(R.id.detailResetAdd);
        moneyReset = findViewById(R.id.moneyResetAdd);
        save = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_addCancel);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        if(Objects.equals(category, "수입")){
            radioButtonIncome.setChecked(true);
            radioButtonExpen.setChecked(false);
        }
        else{
            radioButtonIncome.setChecked(false);
            radioButtonExpen.setChecked(true);
        }

        detailText.addTextChangedListener(new TextWatcher() {
            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (detailText.getLineCount() > 2){
                    detailText.setText(previousString);
                    detailText.setSelection(detailText.length());
                }
            }
        });

        datePicker.updateDate(StaticVariable.year, StaticVariable.month-1, StaticVariable.day);

        dateReset.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DATE);

            datePicker.updateDate(year, month-1, day);
        });

        detailReset.setOnClickListener(v -> detailText.setText(""));

        moneyReset.setOnClickListener(v -> moneyText.setText(""));

        save.setOnClickListener(view -> {
            if (detailText.getText().toString().equals("") || moneyText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else if(Integer.parseInt(moneyText.getText().toString()) <= 0){
                moneyText.setText("");
                Toast.makeText(getApplicationContext(), "금액은 0보다 커야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                MoneyDBHelper dbHelper = new MoneyDBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String category = radioButton.getText().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                String detail = detailText.getText().toString().replace("'","''");
                int money = Integer.parseInt(moneyText.getText().toString());

                String sqlInsert = MoneyDBCtrct.SQL_INSERT + "(" + "'" + category + "', " +  year + ", " + month + ", " + day + ", " + "'" + detail + "', " + money + ")";
                db.execSQL(sqlInsert);

                db.close();
                dbHelper.close();

                StaticVariable.year = year;
                StaticVariable.month = month;
                StaticVariable.day = day;

                setResult(INSERT_SUCCESS);
                finish();
            }
        });

        cancel.setOnClickListener(view -> {
            setResult(ADD_CANCEL);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        setResult(ADD_CANCEL);
        finish();
    }
}
