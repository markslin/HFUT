package cn.edu.hfut.xc.hfut;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.hfut.xc.fragment.LibraryFragment;
import cn.edu.hfut.xc.fragment.NewsFragment;
import cn.edu.hfut.xc.fragment.ScoreFragment;
import cn.edu.hfut.xc.fragment.TableFragment;
import cn.edu.hfut.xc.utilitis.AESUtils;
import cn.edu.hfut.xc.utilitis.DeviceInfo;
import cn.edu.hfut.xc.utilitis.SystemBarTintManager;
import cn.edu.hfut.xc.view.CircleImageView;
import cn.edu.hfut.xc.view.ColorImageView;
import cn.edu.hfut.xc.view.DrawerArrowDrawable;
import cn.edu.hfut.xc.view.RippleView;


public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final int MENU_SETTINGS = 0, MENU_1 = 1, MENU_2 = 2, MENU_3 = 3, MENU_4 = 4, MENU_5 = 5, MENU_6 = 6, MENU_INFO = 7, MENU_NIGHT = 8;
    private final int DELAY = 25;
    private DrawerArrowDrawable drawerArrowDrawable;
    private boolean flipped;
    private DrawerLayout drawer;
    private FrameLayout actionBar;
    private LinearLayout menuBar;
    private CircleImageView headImage;
    private RippleView rippleView1, rippleView2, rippleView3, rippleView4, rippleView5, rippleView6, rippleView7, rippleView8;
    private ColorImageView colorImage1, colorImage2;
    private TextView titleTextView, introduceTextView;
    private Handler handler;
    private int MENU = 0;
    private int CURRENT_MENU = 1;
    private String userName, passWord;
    private String libUserName, libPassWord;
    private SystemBarTintManager mTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (sharedPreferences.getInt("night", 0) == 1)
            setThemeColor(getResources().getColor(R.color.colorNight));
        else
            setThemeColor(sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary)));
        String value = sharedPreferences.getString("introduce", "(添加一句话自我介绍)");
        introduceTextView.setText(value.equals("") ? "(添加一句话自我介绍)" : value);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_content, new NewsFragment());
        transaction.commit();
        rippleView1.setSelected(true);
    }

    private void init() {
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        final ImageView indicator = (ImageView) findViewById(R.id.activity_content_indicator);
        drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.colorLightGray));
        indicator.setImageDrawable(drawerArrowDrawable);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            Bitmap bitmap = BitmapFactory.decodeFile(getCacheDir() + "/" + AESUtils.decrypt(DeviceInfo.getDeviceId(this), sharedPreferences.getString("USERNAME", null)) + ".jpg");
            if (bitmap != null)
                headImage.setImageBitmap(bitmap);
        } catch (Exception e) {

        }

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (MENU != CURRENT_MENU) {
                    FragmentTransaction transaction;
                    rippleView1.setSelected(false);
                    rippleView2.setSelected(false);
                    rippleView3.setSelected(false);
                    rippleView4.setSelected(false);
                    rippleView5.setSelected(false);
                    rippleView6.setSelected(false);
                    rippleView7.setSelected(false);
                    rippleView8.setSelected(false);
                    switch (MENU) {
                        case MENU_1:
                            rippleView1.setSelected(true);
                            titleTextView.setText("首页");
                            transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.activity_content, new NewsFragment());
                            transaction.commit();
                            CURRENT_MENU = MENU;
                            break;
                        case MENU_2:
                            rippleView2.setSelected(true);
                            if (validate()) {
                                titleTextView.setText("课表");
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.activity_content, new TableFragment().getInstance(userName, passWord));
                                transaction.commit();
                                CURRENT_MENU = MENU;
                            } else {
                                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                            break;
                        case MENU_3:
                            rippleView3.setSelected(true);
                            if (validate()) {
                                titleTextView.setText("成绩");
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.activity_content, new ScoreFragment().getInstance(userName, passWord));
                                transaction.commit();
                                CURRENT_MENU = MENU;
                            } else {
                                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                            break;
                        case MENU_4:
                            rippleView4.setSelected(true);
                            if (libValidate()) {
                                titleTextView.setText("图书");
                                transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.activity_content, new LibraryFragment().getInstance(libUserName, libPassWord));
                                transaction.commit();
                                CURRENT_MENU = MENU;
                            } else {
                                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, LibraryLoginActivity.class));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                MENU = msg.what;
                drawer.closeDrawers();
                if (MENU == MENU_INFO) {
                    if (validate()) {
                        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                        intent.putExtra("USERNAME", userName);
                        intent.putExtra("PASSWORD", passWord);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                } else if (MENU == MENU_SETTINGS)
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        };
    }

    private boolean validate() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        userName = sharedPreferences.getString("USERNAME", null);
        passWord = sharedPreferences.getString("PASSWORD", null);
        String deviceId = DeviceInfo.getDeviceId(this);
        userName = AESUtils.decrypt(deviceId, userName);
        passWord = AESUtils.decrypt(deviceId, passWord);
        if (userName == null || passWord == null)
            return false;
        else {
            return true;
        }
    }

    private boolean libValidate() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        libUserName = sharedPreferences.getString("LIB_USERNAME", null);
        libPassWord = sharedPreferences.getString("LIB_PASSWORD", null);
        String deviceId = DeviceInfo.getDeviceId(this);
        libUserName = AESUtils.decrypt(deviceId, libUserName);
        libPassWord = AESUtils.decrypt(deviceId, libPassWord);
        if (libUserName == null || libPassWord == null)
            return false;
        else {
            return true;
        }
    }

    private void findViews() {
        actionBar = (FrameLayout) findViewById(R.id.activity_main_action_bar);
        titleTextView = (TextView) findViewById(R.id.activity_main_title);
        menuBar = (LinearLayout) findViewById(R.id.activity_menu_title_bar);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        headImage = (CircleImageView) findViewById(R.id.menu_head_img);
        rippleView1 = (RippleView) findViewById(R.id.activity_menu_ripple_view1);
        rippleView2 = (RippleView) findViewById(R.id.activity_menu_ripple_view2);
        rippleView3 = (RippleView) findViewById(R.id.activity_menu_ripple_view3);
        rippleView4 = (RippleView) findViewById(R.id.activity_menu_ripple_view4);
        rippleView5 = (RippleView) findViewById(R.id.activity_menu_ripple_view5);
        rippleView6 = (RippleView) findViewById(R.id.activity_menu_ripple_view6);
        rippleView7 = (RippleView) findViewById(R.id.activity_menu_ripple_view7);
        rippleView8 = (RippleView) findViewById(R.id.activity_menu_ripple_view8);
        rippleView1.setSelected(rippleView1.isSelected());
        rippleView2.setSelected(rippleView2.isSelected());
        rippleView3.setSelected(rippleView3.isSelected());
        rippleView4.setSelected(rippleView4.isSelected());
        rippleView5.setSelected(rippleView5.isSelected());
        rippleView6.setSelected(rippleView6.isSelected());
        rippleView7.setSelected(rippleView7.isSelected());
        rippleView8.setSelected(rippleView8.isSelected());
        colorImage1 = (ColorImageView) findViewById(R.id.activity_menu_color_image1);
        colorImage2 = (ColorImageView) findViewById(R.id.activity_menu_color_image2);
        introduceTextView = (TextView) findViewById(R.id.activity_main_introduce);
    }

    public void onSettingsClicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_SETTINGS, DELAY);
    }

    public void onNightClicked(View view) {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getInt("night", 0) == 0)
            editor.putInt("night", 1);
        else
            editor.putInt("night", 0);
        editor.commit();
    }

    private void setThemeColor(int color) {
        //color = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        actionBar.setBackgroundColor(color);
        mTintManager.setTintColor(Color.argb(0xee, Color.red(color), Color.green(color), Color.blue(color)));
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        menuBar.setBackgroundColor(color);
        rippleView1.setRippleColor(color);
        rippleView2.setRippleColor(color);
        rippleView3.setRippleColor(color);
        rippleView4.setRippleColor(color);
        rippleView5.setRippleColor(color);
        rippleView6.setRippleColor(color);
        rippleView7.setRippleColor(color);
        rippleView8.setRippleColor(color);
        colorImage1.setImageColor(color);
        colorImage2.setImageColor(color);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == "introduce") {
            String value = sharedPreferences.getString("introduce", "(添加一句话自我介绍)");
            introduceTextView.setText(value.equals("") ? "(添加一句话自我介绍)" : value);
            return;
        }
        if (sharedPreferences.getInt("night", 0) == 1) {
            setThemeColor(getResources().getColor(R.color.colorNight));
            return;
        }
        setThemeColor(sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary)));
    }

    public void onMenuIntroduceClicked(View view) {
        startActivity(new Intent(this, IntroduceActivity.class));
    }

    public void onMenuInfoClicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_INFO, DELAY);
    }

    public void onMenu1Clicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_1, DELAY);
    }

    public void onMenu2Clicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_2, DELAY);
    }

    public void onMenu3Clicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_3, DELAY);
    }

    public void onMenu4Clicked(View view) {
        handler.sendEmptyMessageDelayed(MENU_4, DELAY);
    }
}
