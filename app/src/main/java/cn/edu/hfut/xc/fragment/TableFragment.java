package cn.edu.hfut.xc.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.hfut.xc.database.DataBaseHelper;
import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.utilitis.ClassTableUtility;

/**
 * Created by MarksLin on 2015/10/28 0028.
 */
public class TableFragment extends BaseFragment implements ClassTableUtility.ThreadStatusListener {
    private final int LOAD_DATA_OK = 1, LOAD_DATA_FAIL = 2;
    private View lines[] = new View[12];
    private TextView tables[] = new TextView[35];
    private SwipeRefreshLayout swipeRefreshLayout;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private Handler handler;
    private int count = 0;

    public TableFragment getInstance(String... args) {
        TableFragment fragment = new TableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", args[0]);
        bundle.putString("PASSWORD", args[1]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, null);
        findViews(view);
        context = getActivity();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorDeepPurple, R.color.colorPurple, R.color.colorGreen, R.color.colorLime, R.color.colorIndigo);
        setThemeColor(colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ClassTableUtility classTableUtility = new ClassTableUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
                classTableUtility.setOnThreadStatusListener(TableFragment.this);
                classTableUtility.start();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                swipeRefreshLayout.setRefreshing(false);
                switch (msg.what) {
                    case LOAD_DATA_OK:
                        getData();
                        break;
                    case LOAD_DATA_FAIL:
                        count++;
                        if (count < 6) {
                            ClassTableUtility classTableUtility = new ClassTableUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
                            classTableUtility.setOnThreadStatusListener(TableFragment.this);
                            classTableUtility.start();
                        } else {
                            Toast.makeText(context, "请检查网络设置", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;
                }
            }
        };
        if (!getData()) {
            ClassTableUtility classTableUtility = new ClassTableUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
            classTableUtility.setOnThreadStatusListener(this);
            classTableUtility.start();
        }
        return view;
    }

    private boolean getData() {
        dataBaseHelper = new DataBaseHelper(context, "public_class", null, 1);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from class_table", null);
        if (!cursor.moveToNext()) {
            cursor.close();
            database.close();
            return false;
        }
        cursor.moveToPrevious();
        String[] texts = new String[35];
        int j = 0;
        while (cursor.moveToNext()) {
            for (int i = 1; i <= 7; i++) {
                texts[j] = cursor.getString(i).replaceAll("/", "");
                j++;
            }
        }
        cursor.close();
        database.close();
        setText(texts);
        return true;
    }

    private void setText(String[] texts) {
        for (int i = 0; i < 35; i++) {
            tables[i].setText(texts[i]);
        }
        setThemeColor(colorPrimary);
    }

    private void findViews(View view) {
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.fragment_table_swipe_refresh_layout);
        int lineIds[] = new int[]{
                R.id.horizontal_line_1, R.id.horizontal_line_2, R.id.horizontal_line_3,
                R.id.horizontal_line_4, R.id.horizontal_line_5, R.id.vertical_line_1,
                R.id.vertical_line_2, R.id.vertical_line_3, R.id.vertical_line_4,
                R.id.vertical_line_5, R.id.vertical_line_6, R.id.vertical_line_7};
        for (int i = 0; i < lineIds.length; i++)
            lines[i] = view.findViewById(lineIds[i]);
        int tableIds[] = new int[]{
                R.id.table_11, R.id.table_12, R.id.table_13, R.id.table_14, R.id.table_15, R.id.table_16, R.id.table_17,
                R.id.table_21, R.id.table_22, R.id.table_23, R.id.table_24, R.id.table_25, R.id.table_26, R.id.table_27,
                R.id.table_31, R.id.table_32, R.id.table_33, R.id.table_34, R.id.table_35, R.id.table_36, R.id.table_37,
                R.id.table_41, R.id.table_42, R.id.table_43, R.id.table_44, R.id.table_45, R.id.table_46, R.id.table_47,
                R.id.table_51, R.id.table_52, R.id.table_53, R.id.table_54, R.id.table_55, R.id.table_56, R.id.table_57};

        for (int i = 0; i < tableIds.length; i++) {
            tables[i] = (TextView) view.findViewById(tableIds[i]);
        }
    }

    @Override
    public void setThemeColor(int color) {
        color = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        for (int i = 0; i < lines.length; i++)
            lines[i].setBackgroundColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        for (int i = 0; i < tables.length; i++) {
            if (tables[i].getText() != "")
                tables[i].setBackgroundColor(color);
            else
                tables[i].setBackgroundColor(Color.TRANSPARENT);
        }
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
}