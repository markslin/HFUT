package cn.edu.hfut.xc.utilitis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;

import cn.edu.hfut.xc.database.DataBaseHelper;

/**
 * Created by MarksLin on 2015/4/4 0004.
 */
public class ClassTableUtility extends Thread {
    private ThreadStatusListener listener = null;
    private String userName;
    private String passWord;
    //private String passurl="http://172.18.6.16/pass.asp";
    //private String grkburl="http://172.18.6.16/student/asp/grkb1.asp";
    private String passurl = "http://222.195.8.201/pass.asp";
    private String grkburl = "http://222.195.8.201/student/asp/grkb1.asp";
    private Map<String, String> cookie;

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public ClassTableUtility(Context context, String userName, String passWord) {
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
        Looper.prepare();
        try {
            if (listener != null)
                listener.threadStart();
            db.execSQL("delete from class_table");
            Connection.Response response = Jsoup.connect(passurl).data("UserStyle", "student", "user", userName, "password", passWord).method(Method.POST).timeout(6 * 1000).execute();
            cookie = response.cookies();
            Document document = Jsoup.connect(grkburl).cookies(cookie).timeout(6 * 1000).post();
            Elements trs = document.select("tr");
            String[] text = new String[7];
            for (int i = 1; i < 10; i = i + 2) {
                for (int j = 1; j <= 7; j++) {
                    Elements td = trs.get(2 + i).select("td");
                    text[j - 1] = td.get(j).html().toString();
                }
                db.execSQL("insert into class_table(Mon,Tue,Wed,Thu,Fri,Sat,Sun)values('" + text[0] + "','" + text[1] + "','" + text[2] + "','" + text[3] + "','" + text[4] + "','" + text[5] + "','" + text[6] + "')");
                //db.execSQL("update class_table set Mon='"+text[0]+"',Tue='"+text[1]+"',Wed='"+text[2]+"',Thu='"+text[3]+"',Fri='"+text[4]+"',Sat='"+text[5]+"',Sun='"+text[6]+"'where id="+Integer.toString(i/2));
            }
            db.close();
            if (listener != null)
                listener.threadEnd();

        } catch (Exception e) {
            if (listener != null)
                listener.threadError();
        }
        Looper.loop();

    }

    public interface ThreadStatusListener {
        public void threadStart();

        public void threadEnd();

        public void threadError();
    }
}
