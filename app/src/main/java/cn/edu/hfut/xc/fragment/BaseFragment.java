package cn.edu.hfut.xc.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import cn.edu.hfut.xc.hfut.R;

/**
 * Created by MarksLin on 2015/10/23 0023.
 */
public class BaseFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    int colorPrimary;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (sharedPreferences.getInt("night", 0) == 1)
            colorPrimary = getResources().getColor(R.color.colorNight);
        else
            colorPrimary = sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary));
        context = getActivity();
    }

    public void setThemeColor(int color) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == "introduce") {
            return;
        }
        if (sharedPreferences.getInt("night", 0) == 1) {
            setThemeColor(context.getResources().getColor(R.color.colorNight));
            return;
        }
        setThemeColor(sharedPreferences.getInt("colorPrimary", getResources().getColor(R.color.colorPrimary)));
    }
}
