package cn.edu.hfut.xc.utilitis;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by MarksLin on 2015/10/30 0030.
 */
public class LibraryLoginUtil extends Thread {
    private String passWord;
    private String userName;
    //private String passurl = "http://210.45.242.5:8080/reader/login.php";
    private String passurl = "http://210.45.242.5:8080/reader/redr_verify.php";
    private LoginListener listener;

    public LibraryLoginUtil(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public void setOnLoginListener(LoginListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        //Looper.prepare();
        try {
            if (listener != null)
                listener.loginStart();
            Connection.Response response = Jsoup.connect(passurl).data("select", "cert_no", "number", userName, "passwd", passWord).method(Connection.Method.POST).timeout(6 * 1000).execute();
            if (listener != null) {
                Log.d("TAG", response.url().toString());
                if (response.url().toString().equals("http://210.45.242.5:8080/reader/redr_info.php")) {
                    listener.loginSuccess();
                } else {
                    listener.loginFailure(-1);
                }
            }
        } catch (IOException e) {
            if (listener != null) {
                listener.loginFailure(404);
            }
        }
        //Looper.loop();
    }

    public interface LoginListener {
        void loginStart();

        void loginSuccess();

        void loginFailure(int code);
    }
}
