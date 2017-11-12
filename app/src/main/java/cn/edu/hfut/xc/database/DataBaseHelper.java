package cn.edu.hfut.xc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MarksLin on 2015/4/2 0002.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private String name;//database的name

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
    }

    /**
     * 第一次创建的时候调用
     * 完成一个数据库初始化的操作
     * 执行getWritableDatabase()时调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {//Mon Tue Wed Thu Fri Sat Sun
        if (name.equals("public_class")) {
            db.execSQL("create table if not exists class_table(" + "id integer primary key," + "Mon text," + "Tue text," + "Wed text," + "Thu text," + "Fri text," + "Sat text," + "Sun text)");
            //for (int i=0;i<5;i++)//''和""均可
            //db.execSQL("insert into class_table(id,Mon,Tue,Wed,Thu,Fri,Sat,Sun) values("+Integer.toString(i)+",'','','','','','','')");
            //db.execSQL("insert into class_table(id,Mon,Tue,Wed,Thu,Fri,Sat,Sun) values("+Integer.toString(i)+",\"\",\"\",\"\",\"\",\"\",\"\",\"\")");
            db.execSQL("create table if not exists score_table(" + "id integer primary key," + "Term text," + "Course_Code text," + "Course_Title text," + "No_classes text," + "Score text," + "Make_up text," + "Credit text)");
            db.execSQL("create table if not exists info_table(id integer primary key,Info text)");
        }
    }

    /**
     * 版本更新的时候调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
