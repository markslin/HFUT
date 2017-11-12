package cn.edu.hfut.xc.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.hfut.xc.hfut.R;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class InfoAdapter extends BaseAdapter {
    List<String> infos;

    public InfoAdapter(List<String> infos) {
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.info_list_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.info_text_view);
        textView.setText(infos.get(position));
        return convertView;
    }
}
