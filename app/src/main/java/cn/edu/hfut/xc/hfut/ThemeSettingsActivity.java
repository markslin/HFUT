package cn.edu.hfut.xc.hfut;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by MarksLin on 2015/10/22 0022.
 */
public class ThemeSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    private SeekBar seekBarR, seekBarG, seekBarB;
    private TextView textViewR, textViewG, textViewB;
    private FrameLayout actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_settings);
        findViews();
        seekBarR.setOnSeekBarChangeListener(this);
        seekBarG.setOnSeekBarChangeListener(this);
        seekBarB.setOnSeekBarChangeListener(this);
        textViewR.setText(Integer.toString(seekBarR.getProgress()));
        textViewG.setText(Integer.toString(seekBarG.getProgress()));
        textViewB.setText(Integer.toString(seekBarB.getProgress()));
        actionBar.setBackgroundColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        setSeekBarProgress(COLOR_R, COLOR_G, COLOR_B);
    }

    private void findViews() {
        seekBarR = (SeekBar) findViewById(R.id.seek_bar_r);
        seekBarG = (SeekBar) findViewById(R.id.seek_bar_g);
        seekBarB = (SeekBar) findViewById(R.id.seek_bar_b);
        textViewR = (TextView) findViewById(R.id.text_view_r);
        textViewG = (TextView) findViewById(R.id.text_view_g);
        textViewB = (TextView) findViewById(R.id.text_view_b);
        actionBar = (FrameLayout) findViewById(R.id.activity_theme_settings_action_bar);
    }

    private void setSeekBarProgress(int r, int g, int b) {
        seekBarR.setProgress(r);
        seekBarG.setProgress(g);
        seekBarB.setProgress(b);
    }

    public void onPurpleClicked(View view) {
        setSeekBarProgress(156, 39, 176);
    }

    public void onDeepPurpleClicked(View view) {
        setSeekBarProgress(103, 58, 183);
    }

    public void onGreenClicked(View view) {
        setSeekBarProgress(37, 155, 36);
    }

    public void onLimeClicked(View view) {
        setSeekBarProgress(205, 220, 57);
    }

    public void onIndigoClicked(View view) {
        setSeekBarProgress(63, 81, 181);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_bar_r:
                textViewR.setText(Integer.toString(progress));
                COLOR_R = progress;
                break;
            case R.id.seek_bar_g:
                textViewG.setText(Integer.toString(progress));
                COLOR_G = progress;
                break;
            case R.id.seek_bar_b:
                textViewB.setText(Integer.toString(progress));
                COLOR_B = progress;
                break;
            default:
                break;
        }
        actionBar.setBackgroundColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        super.setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onThemeSettingsBackClicked(View view) {
        finish();
    }

    public void onDefaultThemeClicked(View view) {
        setSeekBarProgress(255, 102, 0);
    }

    public void onThemeSettingsOkClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("colorPrimary", Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        editor.putInt("night", 0);
        editor.commit();
        finish();
    }
}
