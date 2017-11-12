package cn.edu.hfut.xc.hfut;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import cn.edu.hfut.xc.utilitis.SystemBarTintManager;

/**
 * Created by MarksLin on 2015/10/22 0022.
 */
public class BaseActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    View rootView;
    int COLOR_R, COLOR_G, COLOR_B;
    float X;
    private SystemBarTintManager mTintManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.style.popupWindow_animation);
        rootView = View.inflate(this, R.layout.activity_base, null);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        int color;
        if (sharedPreferences.getInt("night", 0) == 1)
            color = getResources().getColor(R.color.colorNight);
        else
            color = sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary));
        COLOR_R = Color.red(color);
        COLOR_G = Color.green(color);
        COLOR_B = Color.blue(color);
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                X = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                rootView.scrollTo((int) (X - event.getX()) <= 0 ? (int) (X - event.getX()) : 0, 0);
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() - X > rootView.getWidth() / 2) {
                    finish();
                } else {
                    ValueAnimator animator = ValueAnimator.ofInt((int) (X - event.getX()), 0);
                    animator.setDuration((int) (event.getX() - X) > 0 ? (int) ((event.getX() - X) * 0.6f) : 0);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            rootView.scrollTo((int) animation.getAnimatedValue(), 0);
                        }
                    });
                    animator.start();
                }
                break;
        }
        return true;
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activity_base);
        View.inflate(this, layoutResID, layout);
        setContentView(rootView);
    }

    public void setThemeColor(int color) {
        color = Color.argb(0xee, Color.red(color), Color.green(color), Color.blue(color));
        mTintManager.setTintColor(color);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == "introduce") {
            return;
        }
        if (sharedPreferences.getInt("night", 0) == 1) {
            setThemeColor(getResources().getColor(R.color.colorNight));
            return;
        }
        setThemeColor(sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary)));
    }
}
