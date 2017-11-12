package cn.edu.hfut.xc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.edu.hfut.xc.hfut.R;

/**
 * Created by MarksLin on 2015/10/24 0024.
 */
public class LoadListView extends ListView {
    private boolean loading = false;
    private ColorProgressBar colorProgressBar;
    private OnLoadListener listener;
    private View footer;

    public LoadListView(Context context) {
        super(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        footer = View.inflate(getContext(), R.layout.load_list_view_footer, null);
        colorProgressBar = (ColorProgressBar) footer.findViewById(R.id.color_progress_bar);
        addFooterView(footer);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (getCount() == getLastVisiblePosition() + 1 && !loading) {
                    if (listener != null) {
                        setLoading(true);
                        listener.onLoad();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.listener = listener;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        if (!loading) {
            footer.setVisibility(GONE);
            setFooterBottomPadding(-footer.getHeight());
        } else {
            footer.setVisibility(VISIBLE);
            setFooterBottomPadding(0);
        }
    }

    private void setFooterBottomPadding(int padding) {
        footer.setPadding(footer.getPaddingLeft(), padding, footer.getPaddingRight(), footer.getPaddingBottom());
    }


    public void setFooterColor(int color) {
        colorProgressBar.setIndeterminateDrawableColor(color);
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
