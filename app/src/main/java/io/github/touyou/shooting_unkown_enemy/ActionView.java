package io.github.touyou.shooting_unkown_enemy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by touyou on 2015/12/29.
 */
public class ActionView extends View {

    private Paint paint;
    private ArrayList<PointF> enemy;
    private ArrayList<Integer> eColor;
    private ArrayList<Float> eSize;
    private int numEnemy, firstEnemy;
    private float screenWidth, screenHeight;
    private long nTime;
    private Context context;
    private MySe mySe;
   /*public Camera.Face[] faces;
    public Camera.Size optSize;*/

    public ActionView(Context context) {
        super(context);
        this.context = context;
        enemy = new ArrayList<PointF>();
        eColor = new ArrayList<Integer>();
        eSize = new ArrayList<Float>();
        numEnemy = 0;
        nTime = 0L;
        mySe = new MySe(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        screenHeight = getHeight();
        screenWidth = getWidth();
        /* debug
        Log.d("actionView", String.valueOf(screenHeight) + "," + String.valueOf(screenWidth));
        Log.d("ActionView", "drawing");
        paint.setColor(Color.argb(255,0,0,255));
        canvas.drawCircle(100.0f, 100.0f, 100.0f, paint);
        */
        /*if (faces != null) {
            for (Camera.Face face : faces) {
                if (face.score >= 30) {
                    Matrix matrix = new Matrix();
                    boolean mirror = false;
                    matrix.setScale(mirror ? -1 : 1, 1);
                    matrix.postScale(optSize.width / 2000f, optSize.height / 2000f);
                    matrix.postTranslate(optSize.width / 2f, optSize.height / 2f);
                    int saveCount = canvas.save();
                    canvas.concat(matrix);
                    canvas.drawRect(face.rect, paint);
                    canvas.restoreToCount(saveCount);
                }
            }
        }*/
        drawEnemy(canvas, paint);
    }

    private void drawEnemy(Canvas canvas, Paint paint) {
        if (numEnemy == 0) {
            Log.d("Start", String.valueOf(nTime));
            /* if (nTime == 0L) {
                Toast.makeText(context, "Start!", Toast.LENGTH_LONG);
            } else {
                String res = String.valueOf(System.currentTimeMillis()-nTime);
                Toast.makeText(context, "Last battle time: " + res + " ms", Toast.LENGTH_LONG);
            }*/
            nTime = System.currentTimeMillis();
            numEnemy = (int)(Math.random() * 15) + 1;
            firstEnemy = numEnemy;
            for (int i=0; i < numEnemy; i++) {
                // 乱数指定で敵の座標を指定
                float x = (float)(Math.random() * screenWidth);
                float y = (float)(Math.random() * screenHeight);
                enemy.add(new PointF(x, y));
                eColor.add(Color.argb((int)(Math.random() * 200) + 55, (int)(Math.random() * 255),
                        (int)(Math.random() * 255), (int)(Math.random() * 255)));
                eSize.add((float)(Math.random() * 30.0f) + 20.0f);
            }
        }
        // Log.d("drawEnemy", "Enemy" + String.valueOf(Math.min(screenHeight, screenWidth) / 20.0f));
        int c = 0;
        for (PointF e : enemy) {
            // 座標eに敵画像を描画する
            // とりあえずは丸にする
            paint.setColor(eColor.get(c));
            canvas.drawCircle(e.x, e.y, eSize.get(c), paint);
            c++;
        }
   }

    public int drawScreen(float x, float y) {
        ArrayList<Integer> removeList = new ArrayList<Integer>();
        for (int i=0; i < enemy.size(); i++) {
            if (judgeEnemy(enemy.get(i), eSize.get(i), x, y)) {
                removeList.add(i);
            }
        }
        int c = 0;
        for (int r : removeList) {
            enemy.remove(r - c);
            eColor.remove(r - c);
            eSize.remove(r - c);
            mySe.playHitSound();
            c++;
        }
        numEnemy = enemy.size();
        if (numEnemy <= 0) return 1;
        // Log.d("DrawScreen", String.valueOf(x)+","+String.valueOf(y));
        invalidate();
        return 0;
    }

    public long getTime() {
        return System.currentTimeMillis() - nTime;
    }
    public int getFirstEnemy() {
        return firstEnemy;
    }

    private boolean judgeEnemy(PointF p, float s, float x, float y) {
        float dist = (float)Math.pow(Math.pow(p.x - x, 2.0) + Math.pow(p.y - y, 2.0), 0.5);
        // Log.d("judge", String.valueOf(dist)+","+String.valueOf(Math.min(screenHeight, screenWidth) / 18.0f));
        if (dist <= s + 30.0f) {
            return true;
        }
        return false;
    }
}
