package cn.edu.hfut.xc.utilitis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.struct.NewsHead;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class NewsUtil extends Thread {
    private ThreadStatusListener listener = null;
    private String url;
    private List<NewsHead> newsHeads = new ArrayList<>();

    public NewsUtil(String url) {
        this.url = url;
        newsHeads.clear();
    }

    public void setOnThreadStatusListener(ThreadStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        //Looper.prepare();
        try {
            if (listener != null)
                listener.threadStart();
            Document document = Jsoup.connect(url).timeout(6 * 1000).get();
            Elements table = document.select("table.articlelist2_tbl");
            Elements trs = table.select("tr.articlelist2_tr");
            Elements tds;
            if (url.contains("http://xc.hfut.edu.cn")) {
                for (int i = 0; i < trs.size(); i++) {
                    tds = trs.get(i).select("td");
                    NewsHead head = new NewsHead(tds.get(1).select("a").html(), tds.get(2).html(), tds.get(1).select("a").attr("href"));
                    newsHeads.add(head);
                }
            } else if (url.contains("http://xgzx.hfut.edu.cn")) {
                for (int i = 0; i < trs.size(); i++) {
                    tds = trs.get(i).select("td");
                    NewsHead head = new NewsHead(tds.get(2).select("a").html(), tds.get(3).html(), tds.get(2).select("a").attr("href"));
                    newsHeads.add(head);
                }
            }
            if (listener != null)
                listener.threadEnd(newsHeads);
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
        //Looper.loop();
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd(List<NewsHead> newsHeads);

        void threadError();
    }
}
