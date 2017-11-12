package cn.edu.hfut.xc.utilitis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by MarksLin on 2015/5/25 0025.
 */
public class NewsDetailUtil extends Thread {
    private ThreadStatusListener listener = null;
    private String url;
    private String news = "";

    public NewsDetailUtil(String url) {
        this.url = url;
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
            Elements infobox = null;
            if (url.contains("http://xc.hfut.edu.cn"))
                infobox = document.select("div.infobox");
            /*
            else if (url.contains("http://xgzx.hfut.edu.cn")){
                Log.d("TAG",url);
                infobox = document.select("div[portletmode]").get(6).select("div[portletmode]");
            }*/
            news = infobox.html();
            if (listener != null)
                listener.threadEnd(news);
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
        //Looper.loop();
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd(String news);

        void threadError();
    }
}
