package cn.edu.hfut.xc.utilitis;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by MarksLin on 2015/5/14 0014.
 */
public class LoginUtil extends Thread {
    private String passWord;
    private String userName;
    private String passurl = "http://222.195.8.201/pass.asp";
    private LoginListener listener;

    public LoginUtil(String userName, String passWord) {
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
            Connection.Response response = Jsoup.connect(passurl).data("UserStyle", "student", "user", userName, "password", passWord).method(Connection.Method.POST).timeout(6 * 1000).execute();
            if (listener != null) {
                if (response.url().toString().equals("http://222.195.8.201/student/html/s_index.htm"))
                    listener.loginSuccess();
                else
                    listener.loginFailure(-1);
            }
        } catch (IOException e) {
            if (listener != null)
                listener.loginFailure(404);
        }
        //Looper.loop();
    }

    public interface LoginListener {
        void loginStart();

        void loginSuccess();

        void loginFailure(int code);
    }
}
