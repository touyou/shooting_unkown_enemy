package io.github.touyou.shooting_unkown_enemy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by touyou on 2016/01/08.
 */
public class GameActivity extends AppCompatActivity {
    private ActionView actionView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        // フルスクリーン指定
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ActionView
        actionView = new ActionView(this);
        // View重ねあわせ
        setContentView(new CameraView(this));
        addContentView(actionView, new WindowManager.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int res = actionView.drawScreen(event.getX(), event.getY());
            if (res == 1) {
                long t = actionView.getTime();
                int n = actionView.getFirstEnemy();
                double score = (double)n / t * 1000.0;
                intent = new Intent();
                intent.putExtra(SUEMainActivity.INTENT_KEY_RESULT, score);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return true;
    }
}
