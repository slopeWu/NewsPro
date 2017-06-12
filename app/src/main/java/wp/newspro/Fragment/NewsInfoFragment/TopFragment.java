package wp.newspro.Fragment.NewsInfoFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wp.newspro.Adapter.FragmentTopAdapter;
import wp.newspro.Constance.Constant;
import wp.newspro.Fragment.NewsInfoFragment.Bean.Banner;
import wp.newspro.Fragment.NewsInfoFragment.Bean.Top;
import wp.newspro.Fragment.NewsInfoFragment.Bean.TopDetail;
import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/10.
 */

public class TopFragment extends Fragment {
    private static Context mContext;
    private static ListView listView;
    private static List<TopDetail> mTopDetails;
    private static List<Banner> mBanners;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    FragmentTopAdapter topAdapter = new FragmentTopAdapter(mTopDetails, mContext);
                    listView.setAdapter(topAdapter);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_layout, container, false);
        listView = (ListView) view.findViewById(R.id.wp_top_lv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        innitData();
    }

    //初始化数据
    private static void innitData() {
        mBanners = new ArrayList<>();
        mTopDetails = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url(Constant.TOP_URL).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) return;
                String json = response.body().string();
                if (null == json || json.isEmpty()) return;
                Top top = new Gson().fromJson(json, Top.class);
                List<TopDetail> list = top.getT1348647909107();
                List<Banner> ads = list.get(0).getAds();
                mBanners.addAll(ads);
                list.remove(0);
                mTopDetails.addAll(list);
                mHandler.sendEmptyMessage(0);
            }
        });
    }

}
