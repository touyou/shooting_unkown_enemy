package io.github.touyou.shooting_unkown_enemy;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by touyou on 2016/01/10.
 */
public class MySe {
    private SoundPool soundPool;
    private int hitSound;

    public MySe(Context context) {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        // SE　魔王魂 ttp://maoudamashii.jokersounds.com
        hitSound = soundPool.load(context, R.raw.se_maoudamashii_explosion05, 1);
    }
    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
