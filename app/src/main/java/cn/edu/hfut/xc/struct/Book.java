package cn.edu.hfut.xc.struct;

/**
 * Created by MarksLin on 2015/11/1 0001.
 */
public class Book {
    private String barCode;
    private String name;
    private String date;
    private String num;
    private String attach;
    private String check;

    public Book(String barCode, String name, String date, String num, String attach, String check) {
        this.barCode = barCode;
        this.name = name;
        this.date = date;
        this.num = num;
        this.attach = attach;
        this.check = check;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
