package com.knu.moneymanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private String password;
    private String passwordHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPref = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);// 핸들 객체 얻기
        password = sharedPref.getString("password", "");//두 번째 인자는 반환 값
        passwordHint = sharedPref.getString("passwordHint", "");//두 번째 인자는 반환 값

        if(password.equals(""))
            startMainActivity();
        else
            startPasswordActivity();
    }

    private void startMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }, 300);
    }

    private void startPasswordActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("password", password);
            bundle.putString("passwordHint", passwordHint);
            intent.putExtras(bundle);

            startActivity(intent);
            finish();
        }, 300);
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
