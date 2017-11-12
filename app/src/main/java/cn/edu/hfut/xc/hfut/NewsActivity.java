package cn.edu.hfut.xc.hfut;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.hfut.xc.utilitis.NewsDetailUtil;
import cn.edu.hfut.xc.view.ColorProgressBar;

/**
 * Created by MarksLin on 2015/10/27 0027.
 */
public class NewsActivity extends BaseActivity implements NewsDetailUtil.ThreadStatusListener {
    private LinearLayout actionBar;
    private ColorProgressBar colorProgressBar;
    private TextView title;
    private WebView webView;
    private String url = "";
    private String news = "";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        findViews();
        title.setText(intent.getStringExtra("TITLE"));
        url = intent.getStringExtra("URL");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    webView.loadDataWithBaseURL(url, "<html><head><style type=\"text/css\">h1{text-align: center;font-size: 20px; border-bottom-width: 1px; border-bottom-color: #ccc; border-bottom-style: solid; min-height: 30px} .postmeta{color: #999; text-align: center; font-size: 13px}</style></head><body bgcolor=\"#ffffff\" >" + news + "</body></html>", "text/html", "UTF-8", null);
                    colorProgressBar.setVisibility(View.INVISIBLE);
                } else if (msg.what == 0) {
                    colorProgressBar.setVisibility(View.VISIBLE);
                } else {
                    colorProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        };
        init();
    }

    private void init() {
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        NewsDetailUtil newsDetailUtil = new NewsDetailUtil(url);
        newsDetailUtil.setOnThreadStatusListener(this);
        newsDetailUtil.start();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*if (url.contains(".doc") || url.contains(".docx") || url.contains(".xlsx") || url.contains(".xls") || url.contains(".ppt") || url.contains(".pptx")|| url.contains(".pdf")|| url.contains(".jpg")||url.contains(".png")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                }*/
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                } catch (Exception e) {

                }
                return true;
            }
        });
    }

    private void findViews() {
        title = (TextView) findViewById(R.id.activity_news_title);
        actionBar = (LinearLayout) findViewById(R.id.activity_news_action_bar);
        webView = (WebView) findViewById(R.id.activity_news_web_view);
        colorProgressBar = (ColorProgressBar) findViewById(R.id.activity_news_color_progress_bar);
    }

    @Override
    public void setThemeColor(int color) {
        color = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        actionBar.setBackgroundColor(color);
        colorProgressBar.setIndeterminateDrawableColor(color);
        super.setThemeColor(color);
    }

    /*@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key=="colorPrimary"){
            int color=sharedPreferences.getInt(key, getResources().getColor(R.color.colorPrimary));
            setThemeColor(Color.red(color),Color.green(color),Color.blue(color));
        }
    }*/
    public void onNewsBackClicked(View view) {
        finish();
    }

    @Override
    public void threadStart() {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void threadEnd(String news) {
        this.news = news;
        handler.sendEmptyMessage(1);
    }

    @Override
    public void threadError() {
        handler.sendEmptyMessage(3);
    }
}
