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

public class ModifyActivity extends AppCompatActivity implements Constant {

    RadioGroup radioGroup;
    RadioButton radioButton, radioButtonIncome, radioButtonExpen;
    DatePicker datePicker;
    EditText detailText, moneyText;
    FloatingActionButton dateReset, detailReset, moneyReset;
    Button modify, delete, cancel;

    int oID;
    String oCategory = "수입";
    int oYear = 1997;
    int oMonth = 2;
    int oDay = 19;
    String oDetail = "개발자 생일";
    int oMoney = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        radioGroup = findViewById(R.id.radioGroupModify);
        radioButtonIncome = findViewById(R.id.rbtn_income_modify);
        radioButtonExpen = findViewById(R.id.rbtn_expen_modify);
        datePicker = findViewById(R.id.datePickerModify);
        detailText = findViewById(R.id.detailEditModify);
        moneyText = findViewById(R.id.moneyEditModify);
        dateReset = findViewById(R.id.dateResetModify);
        detailReset = findViewById(R.id.detailResetModify);
        moneyReset = findViewById(R.id.moneyResetModify);
        modify = findViewById(R.id.btn_modify);
        delete = findViewById(R.id.btn_delete);
        cancel = findViewById(R.id.btn_modifyCancel);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            oID = bundle.getInt("id");
            oCategory = bundle.getString("category");
            oYear = bundle.getInt("year");
            oMonth = bundle.getInt("month");
            oDay = bundle.getInt("day");
            oDetail = bundle.getString("detail");
            oMoney = bundle.getInt("money");
        }

        if(oCategory.equals("수입")){
            radioButtonIncome.setChecked(true);
            radioButtonExpen.setChecked(false);
        }
        else if(oCategory.equals("지출")){
            radioButtonIncome.setChecked(false);
            radioButtonExpen.setChecked(true);
        }
        else{
            radioButtonIncome.setChecked(false);
            radioButtonExpen.setChecked(false);
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

        datePicker.updateDate(oYear, oMonth-1, oDay);
        detailText.setText(oDetail);
        moneyText.setText(getString(R.string.contents,oMoney+""));

        dateReset.setOnClickListener(v -> datePicker.updateDate(oYear, oMonth-1, oDay));

        detailReset.setOnClickListener(v -> detailText.setText(oDetail));

        moneyReset.setOnClickListener(v -> moneyText.setText(getString(R.string.contents,oMoney+"")));

        modify.setOnClickListener(view -> {
            if (detailText.getText().toString().equals("") || moneyText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else if(Integer.parseInt(moneyText.getText().toString()) <= 0){
                moneyText.setText("");
                Toast.makeText(getApplicationContext(), "금액은 0보다 커야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                String category = radioButton.getText().toString();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                String detail = detailText.getText().toString().replace("'","''");
                int money = Integer.parseInt(moneyText.getText().toString());

                MoneyDBHelper dbHelper = new MoneyDBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sqlUpdate =
                        MoneyDBCtrct.SQL_UPDATE +
                            MoneyDBCtrct.COL_CATEGORY + "=" + "'" + category + "'" + ", " +
                            MoneyDBCtrct.COL_YEAR + "=" + year + ", " +
                            MoneyDBCtrct.COL_MONTH + "=" + month + ", " +
                            MoneyDBCtrct.COL_DAY + "="  + day + ", " +
                            MoneyDBCtrct.COL_DETAIL + "=" + "'" + detail + "'" + ", " +
                            MoneyDBCtrct.COL_MONEY + "=" + money +
                            " WHERE " + MoneyDBCtrct.COL_ID + "=" + oID;

                db.execSQL(sqlUpdate);

                db.close();
                dbHelper.close();

                StaticVariable.year = year;
                StaticVariable.month = month;
                StaticVariable.day = day;

                setResult(UPDATE_SUCCESS);
                finish();
            }
        });

        delete.setOnClickListener(view -> {
            MoneyDBHelper dbHelper = new MoneyDBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sqlDelete = MoneyDBCtrct.SQL_DELETE + " WHERE " + MoneyDBCtrct.COL_ID + "=" + oID;

            db.execSQL(sqlDelete);

            db.close();
            dbHelper.close();

            setResult(DELETE_SUCCESS);
            finish();
        });

        cancel.setOnClickListener(view -> {
            setResult(MODIFY_CANCEL);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        setResult(MODIFY_CANCEL);
        finish();
    }
}
