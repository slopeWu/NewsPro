package wp.newspro.Splash.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AdsDetail implements Serializable {
    private Action action_params;
    private List<String> res_url;//广告图片


    /**
     * 请求链接
     *
     * @return
     */
    public Action getAction_params() {
        return action_params;
    }


    public void setAction_params(Action action_params) {
        this.action_params = action_params;
    }

    /**
     * 图片链接
     *
     * @return
     */
    public List<String> getRes_url() {
        return res_url;
    }

    public void setRes_url(List<String> res_url) {
        this.res_url = res_url;
    }

    @Override
    public String toString() {
        return "AdsDetail{" +
                "action_params=" + action_params +
                ", res_url=" + res_url +
                '}';
    }
}
