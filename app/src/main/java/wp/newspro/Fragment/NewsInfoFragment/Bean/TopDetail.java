package wp.newspro.Fragment.NewsInfoFragment.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/11.
 */

public class TopDetail implements Serializable {
    private List<Banner> ads; //轮播图
    private String img;     //新闻图片
    private String source;//来源
    private String title;//标题
    private int replyCount;//回复数
    private String specialID;//专题id

    public List<Banner> getAds() {
        return ads;
    }

    public void setAds(List<Banner> ads) {
        this.ads = ads;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    @Override
    public String toString() {
        return "TopDetail{" +
                "ads=" + ads +
                ", img='" + img + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", replyCount=" + replyCount +
                ", specialID='" + specialID + '\'' +
                '}';
    }
}
