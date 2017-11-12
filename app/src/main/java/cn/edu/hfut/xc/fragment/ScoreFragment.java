package cn.edu.hfut.xc.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.adapter.ScoreAdapter;
import cn.edu.hfut.xc.database.DataBaseHelper;
import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.ScoreData;
import cn.edu.hfut.xc.utilitis.ScoreUtility;

/**
 * Created by MarksLin on 2015/10/31 0031.
 */
public class ScoreFragment extends BaseFragment implements ScoreUtility.ThreadStatusListener {
    private final int LOAD_DATA_OK = 1, LOAD_DATA_FAIL = 2;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView statistics, courseCode, courseName, courseScore, makeUp, courseCredit;
    private DataBaseHelper dataBaseHelper;
    private ScoreAdapter adapter;
    private List<ScoreData> scoreDatas = new ArrayList<>();
    private Context context;
    private Handler handler;
    private int count = 0;

    public ScoreFragment getInstance(String... args) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", args[0]);
        bundle.putString("PASSWORD", args[1]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, null);
        findViews(view);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorDeepPurple, R.color.colorPurple, R.color.colorGreen, R.color.colorLime, R.color.colorIndigo);
        context = getActivity();
        setThemeColor(colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ScoreUtility scoreUtility = new ScoreUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
                scoreUtility.setOnThreadStatusListener(ScoreFragment.this);
                scoreUtility.start();
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
                            ScoreUtility scoreUtility = new ScoreUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
                            scoreUtility.setOnThreadStatusListener(ScoreFragment.this);
                            scoreUtility.start();
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
            ScoreUtility scoreUtility = new ScoreUtility(context, getArguments().getString("USERNAME"), getArguments().getString("PASSWORD"));
            scoreUtility.setOnThreadStatusListener(this);
            scoreUtility.start();
        }
        return view;
    }

    @Override
    public void setThemeColor(int color) {
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        courseCode.setBackground(getDrawable(color, courseCode.getBackground()));
        courseName.setBackground(getDrawable(color, courseName.getBackground()));
        courseCredit.setBackground(getDrawable(color, courseCredit.getBackground()));
        courseScore.setBackground(getDrawable(color, courseScore.getBackground()));
        makeUp.setBackground(getDrawable(color, makeUp.getBackground()));
        if (adapter != null)
            adapter.setThemeColor(color);
    }

    private Drawable getDrawable(int color, Drawable drawable) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    private boolean getData() {
        dataBaseHelper = new DataBaseHelper(context, "public_class", null, 1);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from score_table", null);
        if (!cursor.moveToNext()) {
            cursor.close();
            database.close();
            return false;
        }
        cursor.moveToPrevious();
        String term = "";
        float credit = 0;
        float gpa = 0;
        float gp = 0;
        scoreDatas.clear();
        while (cursor.moveToNext()) {
            if (!term.equals(cursor.getString(cursor.getColumnIndex("Term")))) {
                term = cursor.getString(cursor.getColumnIndex("Term"));
                ScoreData data = new ScoreData(term, null, null, null, null, null, null);
                scoreDatas.add(data);
            }

            ScoreData scoreData = new ScoreData(null,
                    cursor.getString(cursor.getColumnIndex("Course_Code")),
                    cursor.getString(cursor.getColumnIndex("Course_Title")),
                    cursor.getString(cursor.getColumnIndex("No_classes")),
                    cursor.getString(cursor.getColumnIndex("Score")),
                    cursor.getString(cursor.getColumnIndex("Make_up")),
                    cursor.getString(cursor.getColumnIndex("Credit")));
            scoreDatas.add(scoreData);
            String s = cursor.getString(cursor.getColumnIndex("Score"));
            boolean skip = false;
            if (s.equals("优"))
                gp = 3.9f;
            else if (s.equals("良"))
                gp = 3.0f;
            else if (s.equals("中"))
                gp = 2.0f;
            else if (s.equals("及格"))
                gp = 1.2f;
            else if (s.contains("免修"))
                gp = 0f;
            else if (s.contains("不及格") || s.contains("未考")) {
                gp = 0f;
                skip = true;
            } else {//<font color="#FF0000">32 </font>
                if (s.contains("font")) {
                    s = s.replaceAll("<font color=\"#FF0000\">", "");
                    s = s.replaceAll("</font>", "");
                }
                float score = Float.parseFloat(s);
                if (score >= 95)
                    gp = 4.3f;
                else if (score >= 90)
                    gp = 4.0f;
                else if (score >= 85)
                    gp = 3.7f;
                else if (score >= 82)
                    gp = 3.3f;
                else if (score >= 78)
                    gp = 3.0f;
                else if (score >= 75)
                    gp = 2.7f;
                else if (score >= 72)
                    gp = 2.3f;
                else if (score >= 68)
                    gp = 2.0f;
                else if (score >= 66)
                    gp = 1.7f;
                else if (score >= 64)
                    gp = 1.3f;
                else if (score >= 60)
                    gp = 1.0f;
                else {
                    gp = 0f;
                    skip = true;
                }
            }
            if (skip)
                continue;
            credit = credit + Float.parseFloat(cursor.getString(cursor.getColumnIndex("Credit")));

            if (cursor.getString(cursor.getColumnIndex("Course_Code")).contains("B"))
                gpa = gpa + Float.parseFloat(cursor.getString(cursor.getColumnIndex("Credit"))) * 1.2f * gp;
            else
                gpa = gpa + Float.parseFloat(cursor.getString(cursor.getColumnIndex("Credit"))) * 1.0f * gp;
        }
        cursor.close();
        database.close();
        statistics.setText("已完成学分:" + credit + " 绩点:" + gpa / credit);
        adapter = new ScoreAdapter(scoreDatas);
        adapter.setThemeColor(colorPrimary);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return true;
    }

    private void findViews(View view) {
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.fragment_score_swipe_refresh_layout);
        listView = (ListView) view.findViewById(R.id.score_list_view);
        statistics = (TextView) view.findViewById(R.id.score_statistics);
        courseCode = (TextView) view.findViewById(R.id.score_course_code);
        courseName = (TextView) view.findViewById(R.id.score_course_name);
        courseCredit = (TextView) view.findViewById(R.id.score_course_credit);
        courseScore = (TextView) view.findViewById(R.id.score_course_score);
        makeUp = (TextView) view.findViewById(R.id.score_course_make_up);
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
