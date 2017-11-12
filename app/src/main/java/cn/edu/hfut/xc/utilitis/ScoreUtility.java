package cn.edu.hfut.xc.utilitis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

import cn.edu.hfut.xc.database.DataBaseHelper;

/**
 * Created by MarksLin on 2015/4/4 0004.
 */
public class ScoreUtility extends Thread {
    private ThreadStatusListener listener = null;
    private String userName;
    private String passWord;
    //private String passurl="http://172.18.6.16/pass.asp";
    //private String scoreurl="http://172.18.6.16/student/asp/Select_Success.asp";
    private String passurl = "http://222.195.8.201/pass.asp";
    private String scoreurl = "http://222.195.8.201/student/asp/Select_Success.asp";
    private Map<String, String> cookie;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public ScoreUtility(Context context, String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        dataBaseHelper = new DataBaseHelper(context, "public_class", null, 1);
        db = dataBaseHelper.getWritableDatabase();
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
            db.execSQL("delete from score_table");
            Connection.Response response = Jsoup.connect(passurl).data("UserStyle", "student", "user", userName, "password", passWord).method(Connection.Method.POST).timeout(6 * 1000).execute();
            cookie = response.cookies();
            Document document = Jsoup.connect(scoreurl).cookies(cookie).timeout(6 * 1000).post();
            Elements table = document.select("table");
            Elements trs = table.select("tr");
            Elements tds;
            String[] text = new String[7];
            for (int i = 1; i < trs.size() - 1; i++) {
                tds = trs.get(i).select("td");
                for (int j = 0; j < tds.size(); j++) {
                    /*if (tds.get(j).html().contains("font"))
                        text[j]=tds.get(j).select("font").html();
                    else*/
                    text[j] = tds.get(j).html();
                }
                db.execSQL("insert into score_table(Term,Course_Code,Course_Title,No_classes,Score,Make_up,Credit)values('" + text[0] + "','" + text[1] + "','" + text[2] + "','" + text[3] + "','" + text[4] + "','" + text[5] + "','" + text[6] + "')");
            }
            db.close();
            if (listener != null)
                listener.threadEnd();
        } catch (IOException e) {
            if (listener != null)
                listener.threadError();
        }
        //Looper.loop();
    }

    public interface ThreadStatusListener {
        void threadStart();

        void threadEnd();

        void threadError();
    }
}
