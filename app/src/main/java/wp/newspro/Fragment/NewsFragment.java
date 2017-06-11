package wp.newspro.Fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import wp.newspro.Adapter.FragmentNewsAdapter;
import wp.newspro.Fragment.NewsInfoFragment.CaiJingFragment;
import wp.newspro.Fragment.NewsInfoFragment.CarFragment;
import wp.newspro.Fragment.NewsInfoFragment.HotFragment;
import wp.newspro.Fragment.NewsInfoFragment.LocalFragment;
import wp.newspro.Fragment.NewsInfoFragment.SportFragment;
import wp.newspro.Fragment.NewsInfoFragment.TechnologyFragment;
import wp.newspro.Fragment.NewsInfoFragment.TopFragment;
import wp.newspro.Fragment.NewsInfoFragment.WyNumberFragment;
import wp.newspro.Fragment.NewsInfoFragment.YuLeFragment;
import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/9.
 */

public class NewsFragment extends Fragment {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private List<Fragment> mFragmentList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.wp_newVp);
        smartTabLayout = (SmartTabLayout) view.findViewById(R.id.wp_stl);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] stringArray = getResources().getStringArray(R.array.newsTitle);
        mFragmentList = new ArrayList<>();

        mFragmentList.add(new TopFragment());
        mFragmentList.add(new YuLeFragment());
        mFragmentList.add(new HotFragment());
        mFragmentList.add(new SportFragment());
        mFragmentList.add(new LocalFragment());
        mFragmentList.add(new WyNumberFragment());
        mFragmentList.add(new CaiJingFragment());
        mFragmentList.add(new TechnologyFragment());
        mFragmentList.add(new CarFragment());

        FragmentNewsAdapter fragmentNewsAdapter = new FragmentNewsAdapter
                (getChildFragmentManager(), mFragmentList, stringArray);
        viewPager.setAdapter(fragmentNewsAdapter);


        smartTabLayout.setViewPager(viewPager);
    }
}

