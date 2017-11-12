package cn.edu.hfut.xc.hfut;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by MarksLin on 2015/11/7 0007.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this, "900012236", true);
    }
}
