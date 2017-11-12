package cn.edu.hfut.xc.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.ScoreData;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class ScoreAdapter extends BaseAdapter {
    List<ScoreData> scoreDatas;
    private List<TextView> textViews = new ArrayList<>();
    private int color = 0x87ff6600;

    public ScoreAdapter(List<ScoreData> scoreDatas) {
        this.scoreDatas = scoreDatas;
    }

    public void setThemeColor(int color) {
        this.color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        for (TextView textView : textViews) {
            textView.setBackground(getDrawable(this.color, textView.getBackground()));
        }
    }

    private Drawable getDrawable(int color, Drawable drawable) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    @Override
    public int getCount() {
        return scoreDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (scoreDatas.get(position).getCourseCode() != null) {
            convertView = View.inflate(parent.getContext(), R.layout.score_list_item, null);
            TextView courseCode = (TextView) convertView.findViewById(R.id.course_code);
            TextView courseTitle = (TextView) convertView.findViewById(R.id.course_title);
            TextView courseScore = (TextView) convertView.findViewById(R.id.course_score);
            TextView courseCredit = (TextView) convertView.findViewById(R.id.course_credit);
            TextView courseMakeUp = (TextView) convertView.findViewById(R.id.course_make_up);
            courseCode.setText(Html.fromHtml(scoreDatas.get(position).getCourseCode()));
            courseTitle.setText(Html.fromHtml(scoreDatas.get(position).getCourseTitle()));
            courseScore.setText(Html.fromHtml(scoreDatas.get(position).getScore()));
            courseMakeUp.setText(Html.fromHtml(scoreDatas.get(position).getMakeUp()));
            courseCredit.setText(Html.fromHtml(scoreDatas.get(position).getCredit()));
            courseCode.setBackground(getDrawable(color, courseCode.getBackground()));
            courseTitle.setBackground(getDrawable(color, courseTitle.getBackground()));
            courseScore.setBackground(getDrawable(color, courseScore.getBackground()));
            courseMakeUp.setBackground(getDrawable(color, courseMakeUp.getBackground()));
            courseCredit.setBackground(getDrawable(color, courseCredit.getBackground()));
            textViews.add(courseCode);
            textViews.add(courseTitle);
            textViews.add(courseScore);
            textViews.add(courseMakeUp);
            textViews.add(courseCredit);
        } else {
            convertView = View.inflate(parent.getContext(), R.layout.score_list_item_title, null);
            TextView title = (TextView) convertView.findViewById(R.id.score_list_title);
            title.setText(scoreDatas.get(position).getTerm());
            title.setBackground(getDrawable(color, title.getBackground()));
            textViews.add(title);
        }

        return convertView;
    }
}
