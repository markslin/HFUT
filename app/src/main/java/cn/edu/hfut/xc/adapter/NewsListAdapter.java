package cn.edu.hfut.xc.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.NewsHead;
import cn.edu.hfut.xc.view.ColorImageView;
import cn.edu.hfut.xc.view.ListRippleView;

/**
 * Created by MarksLin on 2015/10/23 0023.
 */
public class NewsListAdapter extends BaseAdapter {
    private List<NewsHead> newsHeads;
    private List<ColorImageView> colorImageViews = new ArrayList<>();
    private List<ListRippleView> rippleViews = new ArrayList<>();
    private int color = 0xff6600;
    private String TIME = "";

    public NewsListAdapter(List<NewsHead> newsHeads) {
        this.newsHeads = newsHeads;
        Date date = new Date(System.currentTimeMillis());
        TIME = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
    }

    @Override
    public int getCount() {
        return newsHeads.size();
    }

    @Override
    public Object getItem(int position) {
        return newsHeads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setThemeColor(int color) {
        this.color = color;
        for (ColorImageView colorImageView : colorImageViews) {
            colorImageView.setImageColor(color);
        }
        for (ListRippleView rippleView : rippleViews) {
            rippleView.setRippleColor(Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.fragment_news_list_view_item, null);
        rippleViews.add((ListRippleView) convertView);
        ((ListRippleView) convertView).setRippleColor(Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color)));
        TextView title = (TextView) convertView.findViewById(R.id.fragment_news_list_view_item_title);
        TextView time = (TextView) convertView.findViewById(R.id.fragment_news_list_view_item_time);
        String timeText = newsHeads.get(position).getTime();
        title.setText(Html.fromHtml(newsHeads.get(position).getTitle()));
        time.setText(timeText);
        ColorImageView colorImageView = (ColorImageView) convertView.findViewById(R.id.fragment_news_list_view_item_color_image_view);
        colorImageView.setImageColor(color);
        if (timeText.equals(TIME)) {
            colorImageView.setVisibility(View.VISIBLE);
            colorImageViews.add(colorImageView);
        }
        return convertView;
    }

}
