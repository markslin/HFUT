package cn.edu.hfut.xc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.adapter.NewsListAdapter;
import cn.edu.hfut.xc.adapter.NewsPagerAdapter;
import cn.edu.hfut.xc.hfut.NewsActivity;
import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.NewsHead;
import cn.edu.hfut.xc.utilitis.NewsUtil;
import cn.edu.hfut.xc.view.LoadListView;

/**
 * Created by MarksLin on 2015/10/23 0023.
 */
public class NewsFragment extends BaseFragment {
    private final int LOAD_NEWS = 1, LOAD_NOTICE = 2, LOAD_LECTURE = 3, REFRESH_NEWS = 4, REFRESH_NOTICE = 5, REFRESH_LECTURE = 6, LOAD_FAIL = 404;
    private LinearLayout indicatorTop, indicatorBottom;
    private TextView tab;
    private SwipeRefreshLayout swipeRefreshLayout1, swipeRefreshLayout2, swipeRefreshLayout3;
    private Handler handler;
    private NewsListAdapter newsListAdapter1, newsListAdapter2, newsListAdapter3;
    private LoadListView listView1, listView2, listView3;
    private List<NewsHead> newsHeads1 = new ArrayList<>();
    private List<NewsHead> newsHeads2 = new ArrayList<>();
    private List<NewsHead> newsHeads3 = new ArrayList<>();
    private int news1Page = 1, news2Page = 1, news3Page = 1;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        initView(view);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOAD_NEWS:
                        listView1.setLoading(false);
                        newsListAdapter1.notifyDataSetChanged();
                        break;
                    case LOAD_NOTICE:
                        listView2.setLoading(false);
                        newsListAdapter2.notifyDataSetChanged();
                        break;
                    case LOAD_LECTURE:
                        listView3.setLoading(false);
                        newsListAdapter3.notifyDataSetChanged();
                        break;
                    case REFRESH_NEWS:
                        swipeRefreshLayout1.setRefreshing(false);
                        newsListAdapter1.notifyDataSetChanged();
                        break;
                    case REFRESH_NOTICE:
                        swipeRefreshLayout2.setRefreshing(false);
                        newsListAdapter2.notifyDataSetChanged();
                        break;
                    case REFRESH_LECTURE:
                        swipeRefreshLayout3.setRefreshing(false);
                        newsListAdapter3.notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        };
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews(REFRESH_NEWS);
            }
        });
        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews(REFRESH_NOTICE);
            }
        });
        swipeRefreshLayout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews(REFRESH_LECTURE);
            }
        });

        listView1.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getNews(LOAD_NEWS);
            }
        });
        listView2.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getNews(LOAD_NOTICE);
            }
        });
        listView3.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getNews(LOAD_LECTURE);
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("TITLE", "新闻详情");
                intent.putExtra("URL", "http://xc.hfut.edu.cn" + newsHeads1.get(position).getUrl());
                startActivity(intent);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("TITLE", "公告详情");
                intent.putExtra("URL", "http://xc.hfut.edu.cn" + newsHeads2.get(position).getUrl());
                startActivity(intent);
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("TITLE", "讲座详情");

                if (newsHeads3.get(position).getUrl().contains("http://xc.hfut.edu.cn"))
                    intent.putExtra("URL", newsHeads3.get(position).getUrl());
                else
                    intent.putExtra("URL", "http://xc.hfut.edu.cn" + newsHeads3.get(position).getUrl());
                startActivity(intent);
            }
        });
        getNews(REFRESH_NEWS);
        getNews(REFRESH_NOTICE);
        getNews(REFRESH_LECTURE);
        return view;
    }

    private void getNews(final int type) {
        String url;
        if (type == LOAD_NEWS || type == REFRESH_NEWS)
            url = "http://xc.hfut.edu.cn/120/list" + news1Page + ".htm";
        else if (type == LOAD_NOTICE || type == REFRESH_NOTICE)
            url = "http://xc.hfut.edu.cn/121/list" + news2Page + ".htm";
        else
            url = "http://xc.hfut.edu.cn/xsdt/list" + news3Page + ".htm";
        NewsUtil newsUtil = new NewsUtil(url);
        newsUtil.setOnThreadStatusListener(new NewsUtil.ThreadStatusListener() {
            @Override
            public void threadStart() {

            }

            @Override
            public void threadEnd(List<NewsHead> newsHeads) {
                addNews(type, newsHeads);
                handler.sendEmptyMessage(type);
            }

            @Override
            public void threadError() {
                //handler.sendEmptyMessage(LOAD_FAIL);
            }
        });
        newsUtil.start();
    }

    private void initView(View view) {
        indicatorTop = (LinearLayout) view.findViewById(R.id.fragment_news_indicator_top);
        indicatorBottom = (LinearLayout) view.findViewById(R.id.fragment_news_indicator_bottom);
        final TextView tab1 = (TextView) view.findViewById(R.id.fragment_news_tab1);
        final TextView tab2 = (TextView) view.findViewById(R.id.fragment_news_tab2);
        final TextView tab3 = (TextView) view.findViewById(R.id.fragment_news_tab3);
        final TextView indicator1 = (TextView) view.findViewById(R.id.fragment_news_indicator_1);
        final TextView indicator2 = (TextView) view.findViewById(R.id.fragment_news_indicator_2);
        final TextView indicator3 = (TextView) view.findViewById(R.id.fragment_news_indicator_3);
        tab = tab1;
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_news_view_pager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tab2.setBackgroundColor(Color.TRANSPARENT);
                    tab3.setBackgroundColor(Color.TRANSPARENT);
                    tab = tab1;
                } else if (position == 1) {
                    tab1.setBackgroundColor(Color.TRANSPARENT);
                    tab3.setBackgroundColor(Color.TRANSPARENT);
                    tab = tab2;
                } else if (position == 2) {
                    tab1.setBackgroundColor(Color.TRANSPARENT);
                    tab2.setBackgroundColor(Color.TRANSPARENT);
                    tab = tab3;
                }
                setThemeColor(colorPrimary);
            }
        });
        indicator1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });
        indicator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });
        indicator3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
            }
        });
        List<View> views = new ArrayList<>();
        View view1 = View.inflate(getActivity(), R.layout.fragment_news_item, null);
        View view2 = View.inflate(getActivity(), R.layout.fragment_news_item, null);
        View view3 = View.inflate(getActivity(), R.layout.fragment_news_item, null);
        swipeRefreshLayout1 = (SwipeRefreshLayout) view1.findViewById(R.id.fragment_news_swipe_refresh_layout);
        swipeRefreshLayout2 = (SwipeRefreshLayout) view2.findViewById(R.id.fragment_news_swipe_refresh_layout);
        swipeRefreshLayout3 = (SwipeRefreshLayout) view3.findViewById(R.id.fragment_news_swipe_refresh_layout);
        swipeRefreshLayout1.setColorSchemeResources(R.color.colorDeepPurple, R.color.colorPurple, R.color.colorGreen, R.color.colorLime, R.color.colorIndigo);
        swipeRefreshLayout2.setColorSchemeResources(R.color.colorDeepPurple, R.color.colorPurple, R.color.colorGreen, R.color.colorLime, R.color.colorIndigo);
        swipeRefreshLayout3.setColorSchemeResources(R.color.colorDeepPurple, R.color.colorPurple, R.color.colorGreen, R.color.colorLime, R.color.colorIndigo);
        listView1 = (LoadListView) view1.findViewById(R.id.fragment_news_list_view);
        listView2 = (LoadListView) view2.findViewById(R.id.fragment_news_list_view);
        listView3 = (LoadListView) view3.findViewById(R.id.fragment_news_list_view);
        newsListAdapter1 = new NewsListAdapter(newsHeads1);
        newsListAdapter2 = new NewsListAdapter(newsHeads2);
        newsListAdapter3 = new NewsListAdapter(newsHeads3);
        setThemeColor(colorPrimary);
        listView1.setAdapter(newsListAdapter1);
        listView2.setAdapter(newsListAdapter2);
        listView3.setAdapter(newsListAdapter3);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPager.setAdapter(new NewsPagerAdapter(views));
    }

    @Override
    public void setThemeColor(int color) {
        color = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        tab.setBackgroundColor(color);
        listView1.setFooterColor(color);
        listView2.setFooterColor(color);
        listView3.setFooterColor(color);
        newsListAdapter1.setThemeColor(color);
        newsListAdapter2.setThemeColor(color);
        newsListAdapter3.setThemeColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        indicatorTop.setBackgroundColor(color);
        indicatorBottom.setBackgroundColor(color);
    }

    private boolean newsHasExist(List<NewsHead> heads, String news) {
        for (NewsHead head : heads) {
            if (head.getTitle().equals(news))
                return true;
        }
        return false;
    }

    private void addNews(int type, List<NewsHead> newsHeads) {
        List<NewsHead> heads = new ArrayList<>();
        if (type == LOAD_NEWS || type == REFRESH_NEWS) {
            for (NewsHead newsHead : newsHeads) {
                if (!newsHasExist(newsHeads1, newsHead.getTitle()))
                    heads.add(newsHead);
            }
            if ((type == LOAD_NEWS && heads.size() > 0) || (type == REFRESH_NEWS && news1Page == 1 && heads.size() > 0)) {
                news1Page++;
                newsHeads1.addAll(heads);
            }
        } else if (type == LOAD_NOTICE || type == REFRESH_NOTICE) {
            for (NewsHead newsHead : newsHeads) {
                if (!newsHasExist(newsHeads2, newsHead.getTitle()))
                    heads.add(newsHead);
            }

            if ((type == LOAD_NOTICE && heads.size() > 0) || (type == REFRESH_NOTICE && news2Page == 1 && heads.size() > 0)) {
                news2Page++;
                newsHeads2.addAll(heads);
            }
        } else {
            for (NewsHead newsHead : newsHeads) {
                if (!newsHasExist(newsHeads3, newsHead.getTitle()))
                    heads.add(newsHead);
            }
            newsHeads3.addAll(heads);
            if (type == LOAD_LECTURE && heads.size() > 0 || (type == REFRESH_LECTURE && news3Page == 1 && heads.size() > 0)) {
                news3Page++;
                newsHeads3.addAll(heads);
            }
        }
    }
}
