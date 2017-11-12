package cn.edu.hfut.xc.struct;

/**
 * Created by MarksLin on 2015/5/15 0015.
 */
public class ScoreData {
    private String term;
    private String courseCode;
    private String courseTitle;
    private String classNumber;
    private String score;
    private String makeUp;
    private String credit;

    public ScoreData(String term, String courseCode, String courseTitle, String classNumber, String score, String makeUp, String credit) {
        this.term = term;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.classNumber = classNumber;
        this.score = score;
        this.makeUp = makeUp;
        this.credit = credit;
    }

    public String getTerm() {
        return term;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public String getScore() {
        return score;
    }

    public String getMakeUp() {
        return makeUp;
    }

    public String getCredit() {
        return credit;
    }
}
