package cn.edu.hfut.xc.utilitis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.struct.Book;

/**
 * Created by MarksLin on 2015/11/1 0001.
 */
public class LibraryUtil extends Thread {
    private String passWord;
    private String userName;
    private String passurl = "http://210.45.242.5:8080/reader/redr_verify.php";
    private ThreadStatusListener listener;
    private String cookie;
    private Bitmap bitmap;
    private List<Book> books = new ArrayList<>();

    public LibraryUtil(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
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
            Connection.Response response = Jsoup.connect(passurl).data("select", "cert_no", "number", userName, "passwd", passWord).method(Connection.Method.POST).timeout(6 * 1000).execute();
            if (response.url().toString().equals("http://210.45.242.5:8080/reader/redr_info.php")) {
                cookie = response.cookie("PHPSESSID");
                Document document = Jsoup.connect("http://210.45.242.5:8080/reader/book_lst.php").cookie("PHPSESSID", cookie).timeout(6 * 1000).post();
                Elements tbody = document.select("tbody");//div#container\
                Elements trs = tbody.select("tr");
                Elements tds;
                for (int i = 1; i < trs.size() - 2; i++) {
                    tds = trs.get(i).select("td");
                    Book book = new Book(tds.get(0).html(), tds.get(1).select("a").html(), tds.get(2).html() + "至" + tds.get(3).select("font").html(), tds.get(4).html(), tds.get(6).html(), tds.get(7).select("div").get(0).html().split("'")[3]);
                    books.add(book);
                }

                URLConnection connection = new URL("http://210.45.242.5:8080/reader/captcha.php").openConnection();
                connection.addRequestProperty("Cookie", "PHPSESSID=" + cookie);
                connection.connect();
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
            if (listener != null) {
                if (response.url().toString().equals("http://210.45.242.5:8080/reader/redr_info.php"))
                    listener.threadEnd(bitmap, books);
                else
                    listener.threadError();
            }
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
        //Looper.loop();
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd(Bitmap bitmap, List<Book> books);

        void threadError();
    }
}
