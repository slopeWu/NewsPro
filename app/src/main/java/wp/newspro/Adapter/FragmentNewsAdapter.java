package wp.newspro.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */

public class FragmentNewsAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private String[] mNewsTitles;

    public FragmentNewsAdapter(FragmentManager fm, List<Fragment> mFragmentList, String[] mNewsTitles) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mNewsTitles = mNewsTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsTitles[position];
    }
}
