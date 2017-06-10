package wp.newspro.Fragment.NewsInfoFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/10.
 */

public class CaiJingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caijing_layout, container, false);
        return view;
    }
}
