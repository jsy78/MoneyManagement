package com.knu.moneymanagement;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.knu.moneymanagement.setting.Setting_BackupFragment;
import com.knu.moneymanagement.setting.Setting_PasswordFragment;
import com.knu.moneymanagement.setting.Setting_ResetFragment;
import com.knu.moneymanagement.setting.Setting_RestoreFragment;

public class SettingActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        TabLayout mTabLayout = findViewById(R.id.tabLayout4);

        mTabLayout.addTab(mTabLayout.newTab().setText("비밀번호 설정"));
        mTabLayout.addTab(mTabLayout.newTab().setText("데이터 백업"));
        mTabLayout.addTab(mTabLayout.newTab().setText("데이터 복원"));
        mTabLayout.addTab(mTabLayout.newTab().setText("데이터 초기화"));

        mViewPager = findViewById(R.id.viewPager4);
        mViewPager.setOffscreenPageLimit(3);

        ViewPagerFragmentAdapter mFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        mFragmentAdapter.addFragment(new Setting_PasswordFragment());
        mFragmentAdapter.addFragment(new Setting_BackupFragment());
        mFragmentAdapter.addFragment(new Setting_RestoreFragment());
        mFragmentAdapter.addFragment(new Setting_ResetFragment());
        mViewPager.setAdapter(mFragmentAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting2home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                Toast.makeText(this, "비밀번호 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 1:
                Toast.makeText(this, "백업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 2:
                Toast.makeText(this, "복원이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 3:
                Toast.makeText(this, "초기화가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
