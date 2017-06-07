package wp.newspro.Splash.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class Action implements Serializable {
    private String link_url;//页面链接

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "Action{" +
                "link_url='" + link_url + '\'' +
                '}';
    }
}
