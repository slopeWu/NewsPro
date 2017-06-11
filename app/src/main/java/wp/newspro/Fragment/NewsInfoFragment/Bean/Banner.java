package wp.newspro.Fragment.NewsInfoFragment.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/11.
 */

public class Banner implements Serializable {
    private String imgsrc;  //图片
    private String subtitle;
    private String tag;
    private String title;   //标题
    private String url;

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "imgsrc='" + imgsrc + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
