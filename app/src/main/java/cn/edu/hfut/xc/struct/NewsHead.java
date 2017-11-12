package cn.edu.hfut.xc.struct;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class NewsHead {
    private String title;
    private String time;
    private String url;

    public NewsHead(String title, String time, String url) {
        this.title = title;
        this.time = time;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
