package cn.edu.hfut.xc.hfut;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.hfut.xc.database.DataBaseHelper;
import cn.edu.hfut.xc.view.RippleView;

public class SettingsActivity extends BaseActivity {
    private LinearLayout actionBar;
    private View line1, line2, line3, line4, line5;
    private RippleView rippleView1, rippleView2, rippleView3;
    private TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        versionName.setText(getAppVersionName(this));
    }

    private void findViews() {
        actionBar = (LinearLayout) findViewById(R.id.activity_settings_action_bar);
        line1 = findViewById(R.id.activity_settings_line1);
        line2 = findViewById(R.id.activity_settings_line2);
        line3 = findViewById(R.id.activity_settings_line3);
        line4 = findViewById(R.id.activity_settings_line4);
        line5 = findViewById(R.id.activity_settings_line5);
        rippleView1 = (RippleView) findViewById(R.id.activity_settings_ripple_view1);
        rippleView2 = (RippleView) findViewById(R.id.activity_settings_ripple_view2);
        rippleView3 = (RippleView) findViewById(R.id.activity_settings_ripple_view3);
        versionName = (TextView) findViewById(R.id.version_name);
    }

    private String getAppVersionName(Context context) {
        String versionName = "版本:v1.0";
        try {
            // ---get the package info---
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = "版本:v" + pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "版本:v1.0";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    @Override
    public void setThemeColor(int color) {
        actionBar.setBackgroundColor(color);
        super.setThemeColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        line1.setBackgroundColor(color);
        line2.setBackgroundColor(color);
        line3.setBackgroundColor(color);
        line4.setBackgroundColor(color);
        line5.setBackgroundColor(color);
        rippleView1.setRippleColor(color);
        rippleView2.setRippleColor(color);
        rippleView3.setRippleColor(color);
    }

    public void onThemeSettingsClicked(View view) {
        startActivity(new Intent(this, ThemeSettingsActivity.class));
    }

    public void onSettingsBackClicked(View view) {
        finish();
    }

    public void onLogout1SettingsClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LIB_PASSWORD", "");
        editor.commit();

        Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onLogout2SettingsClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PASSWORD", "");
        editor.commit();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "public_class", null, 1);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.execSQL("delete from class_table");
        db.execSQL("delete from info_table");
        db.execSQL("delete from score_table");
        Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == "colorPrimary") {
            setThemeColor(sharedPreferences.getInt(key, getResources().getColor(R.color.colorPrimary)));
        }
    }
}
