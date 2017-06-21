package wp.newspro.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import wp.newspro.Fragment.NewsInfoFragment.Bean.Banner;
import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/17.
 */

public class BanneryAdapter extends PagerAdapter {
    private List<View> mViews;
    private static List<Banner> mBanners;

    public BanneryAdapter(List<View> mViews, List<Banner> mBanners) {
        this.mViews = mViews;
        this.mBanners = mBanners;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realposition = position % mViews.size();
        View view = mViews.get(realposition);
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.wp_bannery_image);
//        TextView textView = (TextView) view.findViewById(R.id.wp_bannery_title);
        Banner banner = mBanners.get(realposition);
        imageView.setImageURI(banner.getImgsrc());
//        textView.setText(banner.getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
