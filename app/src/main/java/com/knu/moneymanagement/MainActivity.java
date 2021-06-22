package com.knu.moneymanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.ref.WeakReference;

import com.knu.moneymanagement.database.Constant;

public class MainActivity extends AppCompatActivity implements Constant {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private LoadingDialog mLoadingDialog;

    private static class InitTask extends AsyncTask<Fragment, Void, Void> {

        private final WeakReference<MainActivity> activityReference;

        private InitTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {

            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;

            activity.mLoadingDialog.setTitle("초기 설정 중...");
            activity.mLoadingDialog.show();
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Fragment... voids) {

            MainActivity activity = activityReference.get();

            if(activity != null && !activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, voids[0]).commit();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;

            activity.mLoadingDialog.dismiss();
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }
    }

    private static class ModeChangeTask extends AsyncTask<Fragment, Void, Void> {

        private final WeakReference<MainActivity> activityReference;

        private ModeChangeTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;

            activity.mLoadingDialog.setTitle("모드 전환 중...");
            activity.mLoadingDialog.show();
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Fragment... voids) {

            MainActivity activity = activityReference.get();

            if(activity != null && !activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, voids[0]).commit();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            MainActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;

            activity.mLoadingDialog.dismiss();
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBarDrawerToggle toggle;
        NavigationView navigationView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        }

        mLoadingDialog = new LoadingDialog(MainActivity.this, R.style.Loading_Dialog_Theme);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        if(findViewById(R.id.frameLayout) != null)
            if(savedInstanceState != null)
                return;

        new InitTask(MainActivity.this).execute(new CalendarFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btn_calendar:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId())
                        new ModeChangeTask(MainActivity.this).execute(new CalendarFragment());
                    break;
                case R.id.btn_list:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId())
                        new ModeChangeTask(MainActivity.this).execute(new ListFragment());
                    break;
                case R.id.btn_graph:
                    if(bottomNavigationView.getSelectedItemId() != item.getItemId())
                        new ModeChangeTask(MainActivity.this).execute(new GraphFragment());
                    break;
            }
            return true;
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_calendar:
                    if(bottomNavigationView.getSelectedItemId() != R.id.btn_calendar)
                        bottomNavigationView.setSelectedItemId(R.id.btn_calendar);
                    break;
                case R.id.nav_list:
                    if(bottomNavigationView.getSelectedItemId() != R.id.btn_list)
                        bottomNavigationView.setSelectedItemId(R.id.btn_list);
                    break;
                case R.id.nav_graph:
                    if(bottomNavigationView.getSelectedItemId() != R.id.btn_graph)
                        bottomNavigationView.setSelectedItemId(R.id.btn_graph);
                    break;
                case R.id.nav_game:
                    Intent it = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(it);
                    break;
                case R.id.nav_setting:
                    Intent it2 = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(it2);
                    break;
                case R.id.nav_exit:
                    System.runFinalization();
                    System.exit(0);
                    finish();
                    break;
            }
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_game:
                Intent it = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(it);
                return true;
            case R.id.action_settings:
                Intent it2 = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(it2);
                return true;
            case R.id.action_exit:
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
            toast = Toast.makeText(getApplicationContext(), " '뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            System.runFinalization();
            System.exit(0);
            finish();
            toast.cancel();
        }
    }
}


