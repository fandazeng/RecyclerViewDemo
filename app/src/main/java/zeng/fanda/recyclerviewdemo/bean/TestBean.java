package zeng.fanda.recyclerviewdemo.bean;

/**
 * @author 曾凡达
 * @date 2018/5/29
 */
public class TestBean {
    private String title ;
    private String content;
    private int rank;

    public TestBean(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
