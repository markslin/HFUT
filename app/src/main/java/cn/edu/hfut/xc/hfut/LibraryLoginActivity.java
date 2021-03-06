package cn.edu.hfut.xc.hfut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.hfut.xc.utilitis.AESUtils;
import cn.edu.hfut.xc.utilitis.DeviceInfo;
import cn.edu.hfut.xc.utilitis.LibraryLoginUtil;
import cn.edu.hfut.xc.view.ColorProgressBar;

/**
 * Created by MarksLin on 2015/11/1 0001.
 */
public class LibraryLoginActivity extends BaseActivity implements LibraryLoginUtil.LoginListener {
    private final int LOGIN_START = 0, LOGIN_SUCCESS = 1, LOGIN_FAILURE = 2;
    private TextView actionBar;
    private EditText userName;
    private EditText passWord;
    private TextView loginButton;
    private TextView appName;
    private ColorProgressBar colorProgressBar;
    private LibraryLoginUtil loginUtil;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_library);
        findViews();
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOGIN_START:
                        colorProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case LOGIN_SUCCESS:
                        colorProgressBar.setVisibility(View.INVISIBLE);
                        String deviceId = DeviceInfo.getDeviceId(LibraryLoginActivity.this);
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("LIB_USERNAME", AESUtils.encrypt(deviceId, userName.getText().toString()));
                        editor.putString("LIB_PASSWORD", AESUtils.encrypt(deviceId, passWord.getText().toString()));
                        editor.commit();
                        Toast.makeText(LibraryLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case LOGIN_FAILURE:
                        colorProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LibraryLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void init() {
        colorProgressBar.setVisibility(View.INVISIBLE);
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
    }

    private void findViews() {
        actionBar = (TextView) findViewById(R.id.activity_login_library_action_bar);
        userName = (EditText) findViewById(R.id.user_name);
        passWord = (EditText) findViewById(R.id.pass_word);
        loginButton = (TextView) findViewById(R.id.activity_login_library_button);
        appName = (TextView) findViewById(R.id.activity_login_library_app_name);
        colorProgressBar = (ColorProgressBar) findViewById(R.id.activity_login_library_progress_bar);
    }

    @Override
    public void setThemeColor(int color) {
        Drawable drawable = userName.getBackground();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);//MULTIPLY/SRC_ATOP/SRC_IN
        userName.setBackground(drawable);
        passWord.setBackground(drawable);
        userName.setTextColor(color);
        passWord.setTextColor(color);
        actionBar.setBackgroundColor(color);
        loginButton.setBackgroundColor(color);
        appName.setTextColor(color);
        colorProgressBar.setIndeterminateDrawableColor(color);
        super.setThemeColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        userName.setHintTextColor(color);
        passWord.setHintTextColor(color);
    }

    private void login() {
        loginUtil = new LibraryLoginUtil(userName.getText().toString(), passWord.getText().toString());
        loginUtil.setOnLoginListener(this);
        loginUtil.start();
    }

    public void onLoginClicked(View view) {
        if (TextUtils.isEmpty(userName.getText())) {
            Toast.makeText(this, "请输入学号", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(passWord.getText())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
        } else {
            if (colorProgressBar.getVisibility() == View.INVISIBLE)
                login();
        }
    }

    @Override
    public void loginStart() {
        handler.sendEmptyMessage(LOGIN_START);
    }

    @Override
    public void loginSuccess() {
        handler.sendEmptyMessageDelayed(LOGIN_SUCCESS, 500);
    }

    @Override
    public void loginFailure(int code) {
        handler.sendEmptyMessage(LOGIN_FAILURE);
    }
}
