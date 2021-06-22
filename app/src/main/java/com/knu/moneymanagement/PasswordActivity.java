package com.knu.moneymanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    EditText passwordInput;
    Button submit, exit;
    private String password = "";
    private String passwordHint = "";
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            password = bundle.getString("password");
            passwordHint = bundle.getString("passwordHint");
        }

        passwordInput = findViewById(R.id.passwordInput);
        submit = findViewById(R.id.btn_submit);
        exit = findViewById(R.id.btn_exit);

        submit.setOnClickListener(view -> {
            if(passwordInput.getText().toString().equals(password)){
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                passwordInput.setText("");
            }
        });

        exit.setOnClickListener(view -> {
            finishAffinity(); // 부모 액티비티 종료
            System.runFinalization();
            System.exit(0); // 시스템 자체를 강제종료
            finish();
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_password, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hint:
                Toast.makeText(getApplicationContext(), passwordHint, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_password2exit:
                finishAffinity();
                System.runFinalization();
                System.exit(0);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            finish();
            toast.cancel();
        }
    }
}
