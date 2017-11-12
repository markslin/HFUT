package cn.edu.hfut.xc.utilitis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.edu.hfut.xc.database.DataBaseHelper;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class InfoUtil extends Thread {
    private ThreadStatusListener listener = null;
    private String userName;
    private String passWord;
    private Context context;
    private String passurl = "http://222.195.8.201/pass.asp";
    private String scoreurl = "http://222.195.8.201/student/asp/xsxxxxx.asp";
    private Map<String, String> cookie;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public InfoUtil(Context context, String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context, "public_class", null, 1);
        db = dataBaseHelper.getWritableDatabase();
    }

    public void setOnThreadStatusListener(ThreadStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        try {
            if (listener != null)
                listener.threadStart();
            db.execSQL("delete from info_table");
            Connection.Response response = Jsoup.connect(passurl).data("UserStyle", "student", "user", userName, "password", passWord).method(Connection.Method.POST).timeout(6 * 1000).execute();
            cookie = response.cookies();
            Document document = Jsoup.connect(scoreurl).cookies(cookie).timeout(6 * 1000).post();
            Elements table = document.select("table");
            Elements trs = table.select("tr");
            Elements tds;
            for (int i = 1; i < trs.size() - 1; i++) {
                tds = trs.get(i).select("td");
                if (i == 1 || i == 2) {
                    for (int j = 0; j < 3; j++)
                        db.execSQL("insert into info_table(Info)values('" + tds.get(j).html() + "')");
                } else if (i < 7) {
                    for (int j = 0; j < 3; j++) {
                        tds = trs.get(i).select("td");
                        String s = tds.get(j).html();
                        tds = trs.get(i + 1).select("td");
                        s = s + ": " + tds.get(j).html().toString();
                        s = s.replaceAll("&nbsp;", "");
                        db.execSQL("insert into info_table(Info)values('" + s + "')");
                    }
                    i++;
                } else {
                    for (int j = 0; j < 4; j++) {
                        tds = trs.get(i).select("td");
                        String s = tds.get(j).html();
                        tds = trs.get(i + 1).select("td");
                        s = s + ": " + tds.get(j).html().toString();
                        s = s.replaceAll("&nbsp;", "");
                        db.execSQL("insert into info_table(Info)values('" + s + "')");
                    }
                    i++;
                }
            }
            db.close();

            URL url = new URL("http://222.195.8.201/student/photo/201" + userName.charAt(3) + "/" + userName + ".JPG");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6 * 1000);
            InputStream inputStream = connection.getInputStream();
            File file = new File(context.getCacheDir() + "/" + userName + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            inputStream.close();
            if (listener != null)
                listener.threadEnd();
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
        Looper.loop();
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd();

        void threadError();
    }
}
