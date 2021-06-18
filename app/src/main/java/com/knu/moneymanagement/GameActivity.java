package com.knu.moneymanagement;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView iv_roulette;
    TextView result;
    int degree_rand;
    float startDegree = 0f;
    float endDegree = 0f;
    long mLastClickTime = 0;

    private static class GameHandler extends Handler {

        private final WeakReference<GameActivity> activityReference;

        private GameHandler(GameActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {

            GameActivity activity = activityReference.get();

            if(activity == null || activity.isFinishing())
                return;

            if(activity.degree_rand == 0)
                activity.result.setText("산다");
            else if(activity.degree_rand > 0 && activity.degree_rand <= 90)
                activity.result.setText("안산다");
            else if(activity.degree_rand > 90 && activity.degree_rand <= 180)
                activity.result.setText("산다");
            else if(activity.degree_rand > 180 && activity.degree_rand <= 270)
                activity.result.setText("안산다");
            else if(activity.degree_rand > 270 && activity.degree_rand < 360)
                activity.result.setText("산다");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        iv_roulette = findViewById(R.id.roulette);
        result = findViewById(R.id.resultText);
    }

    public void rotate(View v) {

        if(SystemClock.elapsedRealtime() - mLastClickTime < 5000)
            return;
        mLastClickTime = SystemClock.elapsedRealtime();

        result.setText("---");

        Random rand = new Random();
        degree_rand = rand.nextInt(360);
        endDegree = startDegree + 360 * 3 + degree_rand;

        ObjectAnimator object = ObjectAnimator.ofFloat(iv_roulette, "rotation", startDegree, endDegree);
        object.setInterpolator(new AccelerateDecelerateInterpolator());
        object.setDuration(5000);
        object.start();

        GameHandler handler = new GameHandler(GameActivity.this);
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_game2home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
