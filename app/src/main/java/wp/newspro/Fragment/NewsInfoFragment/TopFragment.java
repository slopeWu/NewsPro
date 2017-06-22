package wp.newspro.Fragment.NewsInfoFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wp.newspro.Adapter.BanneryAdapter;
import wp.newspro.Adapter.FragmentTopAdapter;
import wp.newspro.Constance.Constant;
import wp.newspro.Fragment.NewsInfoFragment.Bean.Banner;
import wp.newspro.Fragment.NewsInfoFragment.Bean.Top;
import wp.newspro.Fragment.NewsInfoFragment.Bean.TopDetail;
import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/10.
 */

public class TopFragment extends Fragment implements AbsListView.OnScrollListener {
    private static Context mContext;
    private static ListView listView;
    private static List<TopDetail> mTopDetails;
    private static List<Banner> mBanners;
    private static List<View> mViews;
    private static List<ImageView> doc_images;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    topAdapter = new FragmentTopAdapter(mTopDetails, mContext);
                    listView.setAdapter(topAdapter);
                    LoadBannery();
                    break;
                case 1:
                    int curindex = viewPager.getCurrentItem() + 1;
                    viewPager.setCurrentItem(curindex);
                    break;
                case 2:
                    topAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private static LayoutInflater inflater;
    private static ViewPager viewPager;
    private static LinearLayout docs_layout;
    private static TextView wp_bannery_title;
    private static int sIndex = 0;
    private static int eIndex = 0;
    private static int pageSize = 20;
    private static int requestTime = 0;
    private boolean isLoadMore;
    private static FragmentTopAdapter topAdapter;
    private static boolean isRequest=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_layout, container, false);
        listView = (ListView) view.findViewById(R.id.wp_top_lv);
        listView.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.w("wp", "onStoponStoponStoponStoponStop");
        mHandler.removeCallbacks(myr);
        requestTime = 0;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.w("wp", "onStartonStartonStartonStartonStart");
        mHandler.postDelayed(myr, 4000);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        inflater = LayoutInflater.from(mContext);
        doc_images = new ArrayList<>();
        mBanners = new ArrayList<>();
        mTopDetails = new ArrayList<>();
        innitData(true);
        innitBanner();
    }

    private void innitBanner() {
        View view = inflater.inflate(R.layout.top_bannery, null);
        viewPager = (ViewPager) view.findViewById(R.id.wp_top_vp);
        docs_layout = (LinearLayout) view.findViewById(R.id.wp_doc);
        wp_bannery_title = (TextView) view.findViewById(R.id.wp_bannery_title);
        listView.addHeaderView(view);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDoc(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mHandler.removeCallbacks(myr);
                        return false;

                    case MotionEvent.ACTION_UP:
                        mHandler.postDelayed(myr, 4000);
                        return false;
                }
                return true;
            }
        });
    }

    private static void setCurrentDoc(int position) {
        for (int i = 0; i < doc_images.size(); i++) {
            ImageView imageView = doc_images.get(i);
            if (position % doc_images.size() == i) {
                imageView.setImageResource(R.drawable.banner_doc_big);
            } else {
                imageView.setImageResource(R.drawable.banner_doc_normal);
            }

        }
        wp_bannery_title.setText(mBanners.get(position % doc_images.size()).getTitle());
    }


    private static void LoadBannery() {
        if (null != mBanners) {
            mViews = new ArrayList<>();
            for (int i = 0; i < mBanners.size(); i++) {
                View itemView = inflater.inflate(R.layout.top_item_bannery, null);
                mViews.add(itemView);
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.drawable.banner_doc_normal);
                LinearLayout.LayoutParams layoutParams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 10, 0);
                docs_layout.addView(imageView, layoutParams);
                wp_bannery_title.setText(mBanners.get(0).getTitle());
                doc_images.add(imageView);
            }
        }
        BanneryAdapter banneryAdapter = new BanneryAdapter(mViews, mBanners);
        viewPager.setAdapter(banneryAdapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mBanners.size());
        setCurrentDoc(0);
    }


    //初始化数据
    private static void innitData(final boolean first) {
        //处理每次请求条数
        doRequsetStartIndexAndEndIndex();
        if (isRequest) return;
        isRequest = true;
//        Log.w("wp", "加载更多....");

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url(Constant.getTOP_URL(sIndex, eIndex)).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                isRequest = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) return;
                requestTime++;
//                try {
//                    Thread.sleep(Long.valueOf(4000));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                isRequest = false;
                String json = response.body().string();
                if (null == json || json.isEmpty()) return;
                Top top = new Gson().fromJson(json, Top.class);
                List<TopDetail> list = top.getT1348647909107();
                if (first) {

                    List<Banner> ads = list.get(0).getAds();
                    mBanners.addAll(ads);
                    list.remove(0);
                    mTopDetails.addAll(list);
                    mHandler.sendEmptyMessage(0);
                } else {

                    mTopDetails.addAll(list);
                    mHandler.sendEmptyMessage(2);
                }


//                mHandler.sendEmptyMessage(1);
                mHandler.removeCallbacks(myr);
                mHandler.post(myr);
            }
        });
    }

    private static void doRequsetStartIndexAndEndIndex() {
        if (requestTime == 0) {
            sIndex = 0;
            eIndex = sIndex + pageSize;
        } else {
            sIndex = eIndex;
            eIndex = eIndex + pageSize;
        }
    }

    static Runnable myr = new Runnable() {
        @Override
        public void run() {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            mHandler.sendMessage(message);//同步
            mHandler.postDelayed(this, 4000);//异步
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isLoadMore) {

            innitData(false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.w("wp", "firstVisibleItem=" + firstVisibleItem + "----visibleItemCount="
//                + visibleItemCount + "---totalItemCount" + totalItemCount + "---getLastVisiblePosition()==" + view.getLastVisiblePosition());
        if (view.getLastVisiblePosition() == totalItemCount - 1) {
            isLoadMore = true;
        } else {
            isLoadMore = false;
        }
    }
}
