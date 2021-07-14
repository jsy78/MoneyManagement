package com.knu.moneymanagement;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;

import com.knu.moneymanagement.setting.Setting_BackupFragment;
import com.knu.moneymanagement.setting.Setting_PasswordFragment;
import com.knu.moneymanagement.setting.Setting_ResetFragment;
import com.knu.moneymanagement.setting.Setting_RestoreFragment;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private final ArrayList<Fragment> mFragmentItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        TabLayout mTabLayout = findViewById(R.id.tabLayout4);

        mViewPager2 = findViewById(R.id.viewPager4);
        mViewPager2.setOffscreenPageLimit(3);

        ViewPagerFragmentAdapter mFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle(), new FragmentDiffUtil());
        mFragmentItem.add(new Setting_PasswordFragment());
        mFragmentItem.add(new Setting_BackupFragment());
        mFragmentItem.add(new Setting_RestoreFragment());
        mFragmentItem.add(new Setting_ResetFragment());
        mFragmentAdapter.submitList(mFragmentItem);
        mViewPager2.setAdapter(mFragmentAdapter);

        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0 :
                    tab.setText("비밀번호 설정");
                    break;
                case 1 :
                    tab.setText("데이터 백업");
                    break;
                case 2 :
                    tab.setText("데이터 복원");
                    break;
                case 3 :
                    tab.setText("데이터 초기화");
                    break;
                default:
                    Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }).attach();
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
        switch (mViewPager2.getCurrentItem()) {
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
