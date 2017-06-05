package wp.newspro.Splash.Bean;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class Ads implements Serializable {
    private int next_req;
    private List<AdsDetail> ads;

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public List<AdsDetail> getAds() {
        return ads;
    }

    public void setAds(List<AdsDetail> ads) {
        this.ads = ads;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "next_req=" + next_req +
                ", actions=" + ads +
                '}';
    }
}
