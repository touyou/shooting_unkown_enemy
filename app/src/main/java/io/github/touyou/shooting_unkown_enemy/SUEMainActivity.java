package io.github.touyou.shooting_unkown_enemy;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.ReferenceQueue;

public class SUEMainActivity extends AppCompatActivity {

    private Button startBtn;
    private TextView resultTv;
    private Intent intent;

    public final static String INTENT_KEY_RESULT = "intentKeyResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suemain);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        startBtn = (Button) findViewById(R.id.startButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SUEMainActivity.this, GameActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        resultTv = (TextView) findViewById(R.id.resultTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    double res = data.getDoubleExtra(INTENT_KEY_RESULT,-1.0);
                    resultTv.setText("Last speed: "+String.valueOf(res) + " /s");
                } else {
                    resultTv.setText("Error");
                }
                break;
        }
    }
}
