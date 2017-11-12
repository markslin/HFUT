package cn.edu.hfut.xc.hfut;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.adapter.InfoAdapter;
import cn.edu.hfut.xc.database.DataBaseHelper;
import cn.edu.hfut.xc.utilitis.InfoUtil;

/**
 * Created by MarksLin on 2015/10/29 0029.
 */
public class InfoActivity extends BaseActivity implements InfoUtil.ThreadStatusListener {
    private final int LOAD_DATA_OK = 1, LOAD_DATA_FAIL = 2;
    private View infoLine;
    private LinearLayout actionBar;
    private TextView username;
    private TextView departments;
    private TextView professional;
    private ImageView headImg;
    private ListView listView;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private List<String> infos = new ArrayList<>();
    private InfoAdapter adapter;
    private Handler handler;
    private String userName;
    private String passWord;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        userName = intent.getStringExtra("USERNAME");
        passWord = intent.getStringExtra("PASSWORD");
        findViews();
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOAD_DATA_OK:
                        getData();
                        break;
                    case LOAD_DATA_FAIL:
                        count++;
                        if (count < 6) {
                            InfoUtil infoUtil = new InfoUtil(InfoActivity.this, userName, passWord);
                            infoUtil.setOnThreadStatusListener(InfoActivity.this);
                            infoUtil.start();
                        } else {
                            Toast.makeText(InfoActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        if (!getData()) {
            InfoUtil infoUtil = new InfoUtil(this, userName, passWord);
            infoUtil.setOnThreadStatusListener(this);
            infoUtil.start();
        }
    }

    private boolean getData() {
        infos.clear();
        String str = "";
        String number = "";
        dataBaseHelper = new DataBaseHelper(getApplicationContext(), "public_class", null, 1);
        database = dataBaseHelper.getReadableDatabase();
        cursor = database.rawQuery("select * from info_table", null);
        if (!cursor.moveToNext()) {
            cursor.close();
            database.close();
            return false;
        }
        cursor.moveToPrevious();
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("Info"));
            infos.add(str);
            if (str.contains("班级简称:"))
                professional.setText(str.replaceAll("简称", ""));
            else if (str.contains("学院简称"))
                departments.setText(str.replaceAll("简称", ""));
            else if (str.contains("姓名"))
                username.setText(str);
            else if (str.contains("学号"))
                number = str.replaceAll("学号:", "");
        }
        cursor.close();
        database.close();
        Bitmap bitmap = BitmapFactory.decodeFile(getCacheDir() + "/" + number + ".jpg");
        headImg.setImageBitmap(bitmap);
        adapter = new InfoAdapter(infos);
        listView.setAdapter(adapter);
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        return true;
    }

    private void findViews() {
        actionBar = (LinearLayout) findViewById(R.id.activity_info_action_bar);
        username = (TextView) findViewById(R.id.info_name_text_view);
        departments = (TextView) findViewById(R.id.info_departments_text_view);
        professional = (TextView) findViewById(R.id.info_professional_text_view);
        headImg = (ImageView) findViewById(R.id.info_head_img);
        listView = (ListView) findViewById(R.id.info_list_view);
        infoLine = findViewById(R.id.info_head_line);
    }

    @Override
    public void setThemeColor(int color) {
        actionBar.setBackgroundColor(color);
        super.setThemeColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        infoLine.setBackgroundColor(color);
        listView.setDivider(new ColorDrawable(color));
        listView.setDividerHeight(1);
    }

    @Override
    public void threadStart() {
    }

    @Override
    public void threadEnd() {
        handler.sendEmptyMessage(LOAD_DATA_OK);
    }

    @Override
    public void threadError() {
        handler.sendEmptyMessage(LOAD_DATA_FAIL);
    }

    public void onInfoBackClicked(View view) {
        finish();
    }
}
