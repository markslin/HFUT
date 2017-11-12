package cn.edu.hfut.xc.utilitis;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by MarksLin on 2015/11/3 0003.
 */
public class ReNewUtil extends Thread {
    private String barCode;
    private String check;
    private String captcha;
    private String cookie;
    private String url = "http://210.45.242.5:8080/reader/ajax_renew.php";
    private ThreadStatusListener listener;

    public ReNewUtil(String barCode, String check, String captcha, String cookie) {
        this.barCode = barCode;
        this.check = check;
        this.captcha = captcha;
        this.cookie = cookie;
    }

    public void setOnThreadStatusListener(ThreadStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        try {
            if (listener != null)
                listener.threadStart();
            Connection.Response response = Jsoup.connect(url).data("bar_code", barCode, "check", check, "captcha", captcha).cookie("PHPSESSID", cookie).timeout(6 * 1000).execute();
            if (listener != null) {
                if (response.url().toString().equals("http://210.45.242.5:8080/reader/redr_info.php"))
                    listener.threadEnd(response.body());
                else
                    listener.threadError();
            }
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd(String msg);

        void threadError();
    }
}
