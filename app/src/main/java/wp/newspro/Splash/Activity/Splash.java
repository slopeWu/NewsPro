package wp.newspro.Splash.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wp.newspro.Constance.Constant;
import wp.newspro.MainActivity;
import wp.newspro.R;
import wp.newspro.Splash.Bean.Ads;
import wp.newspro.Splash.Bean.AdsDetail;
import wp.newspro.Splash.DownloadImageService;
import wp.newspro.util.Md5Helper;

public class Splash extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static Context mContext;
    private Handler mhandler;

    private ImageView wp_splash_ads;//广告
    private static final String slpash_SharedPreferences = "slpash_SharedPreferences";
    private static final String slpash_SharedPreferences_json = "json";
    private static final String slpash_SharedPreferences_outTime = "outTime";
    private static final String slpash_SharedPreferences_lastTime = "lastTime";
    private static final String slpash_SharedPreferences_pic_index = "pic_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = getApplicationContext();
        sharedPreferences = mContext.getSharedPreferences(slpash_SharedPreferences, MODE_PRIVATE);
        mhandler = new Handler();

        innitView();
        innitData();
        showImage();
    }

    private void showImage() {
        // 每次显示的图片不一样，解决办法【存到shared里面,每次 %、++】
        int show_pic_index = sharedPreferences.getInt(slpash_SharedPreferences_pic_index, 0);
        String json = sharedPreferences.getString(slpash_SharedPreferences_json, "");
        if (null != json) {
            Ads ads = new Gson().fromJson(json, Ads.class);
            if (null == ads) return;
            List<AdsDetail> adsDetails = ads.getAds();
            show_pic_index = show_pic_index % adsDetails.size();
            if (null != adsDetails && adsDetails.size() != 0) {
                final AdsDetail detail = adsDetails.get(show_pic_index);
                String image_url = detail.getRes_url().get(0);
                String md5 = Md5Helper.toMD5(image_url);
                File cacheFile = new File(Environment.getExternalStorageDirectory(), Constant.IMGCACHE);
                File file = new File(cacheFile, md5 + ".jpg");
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    wp_splash_ads.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    wp_splash_ads.setImageBitmap(bitmap);
                    show_pic_index++;
                    //保存图片索引
                    sharedPreferences.edit().putInt(slpash_SharedPreferences_pic_index, show_pic_index).commit();
                    //// TODO: 2017/6/5 点击图片进入广告详情
                    wp_splash_ads.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = detail.getAction_params().getLink_url();
                            if (null != url && !url.isEmpty()) {
                                showAdsView(mContext, url);
                            } else {
                                Toast.makeText(mContext, "页面不存在....", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else {
            // 没有缓存图片，直接跳转到mainActivity
            mhandler.postDelayed(goMainActivity, 3000);
        }
    }

    //跳转到广告页面
    private void showAdsView(Context mContext, String action_params) {
        WeakReference<Context> w = new WeakReference<Context>(mContext);
        try {
            Intent intent = new Intent();
            intent.setClass(w.get(), WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEBVIEWACTIVITY_URL, action_params);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转到mainActivity
    private Runnable goMainActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private void innitData() {
        String json = sharedPreferences.getString(slpash_SharedPreferences_json, "");
        long total = System.currentTimeMillis() - sharedPreferences.getLong(slpash_SharedPreferences_lastTime, 0);
        //如果缓存json存在，且超时了
        if (json.isEmpty() || total > sharedPreferences.getInt(slpash_SharedPreferences_outTime, 0) * 60 * 1000) {
            httpRequest();
        }
    }


    /**
     * 获取数据
     * to prevent memory leaks, so write to be [static]
     * 因为非静态方法默认是由对象引用的，okhttp请求不停止，httpRequest
     * 方法就一直在，方法在对象就被硬拉着在，但是如果这个activity已经退出的话，此时硬拉着的这个对象早就不存在了
     */
    private static void httpRequest() {
        OkHttpClient builder = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url(Constant.SPLASH_URL).build();
        Call call = builder.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();


                    Ads ads = new Gson().fromJson(json, Ads.class);
                    if (null != ads) {
                        //缓存请求json
                        cacheRequestJson(json, ads.getNext_req());

                        Intent intent = new Intent();
                        intent.setClass(mContext, DownloadImageService.class);
                        intent.putExtra(DownloadImageService.ADS_DATA, ads);//传递数据
                        mContext.startService(intent);
                    }
                }
            }
        });
    }

    /**
     * 缓存请求json,当有缓存且没有超时的时候就无需再次请求
     *
     * @param json
     * @param next_req
     */
    private static void cacheRequestJson(String json, int next_req) {
        SharedPreferences.Editor editor = sharedPreferences.edit()
                .putString(slpash_SharedPreferences_json, json)
                .putInt(slpash_SharedPreferences_outTime, next_req)
                .putLong(slpash_SharedPreferences_lastTime, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * 初始化控件
     */
    private void innitView() {
        wp_splash_ads = (ImageView) findViewById(R.id.wp_splash_ads);
    }
}
