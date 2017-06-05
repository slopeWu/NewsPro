package wp.newspro.Splash.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class Action implements Serializable {
    private String action_params;//页面链接

    @Override
    public String toString() {
        return "Action{" +
                "action_params='" + action_params + '\'' +
                '}';
    }
}
