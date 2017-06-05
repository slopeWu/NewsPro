package wp.newspro.Splash.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wp.newspro.Constance.Constant;
import wp.newspro.R;
import wp.newspro.Splash.Bean.Ads;
import wp.newspro.Splash.Bean.AdsDetail;
import wp.newspro.Splash.DownloadImageService;
import wp.newspro.util.Md5Helper;

public class Splash extends AppCompatActivity {
    private ImageView wp_splash_ads;//广告

    private static final String slpash_SharedPreferences = "slpash_SharedPreferences";
    private static final String slpash_SharedPreferences_json = "json";
    private static final String slpash_SharedPreferences_outTime = "outTime";
    private static final String slpash_SharedPreferences_lastTime = "lastTime";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        sharedPreferences = this.getSharedPreferences(slpash_SharedPreferences, MODE_PRIVATE);
        innitView();
        innitData();
        showImage();

    }

    private void showImage() {
        String json = sharedPreferences.getString(slpash_SharedPreferences_json, "");
        if (null != json) {
            Ads ads = new Gson().fromJson(json, Ads.class);
            if (null == ads) return;
            List<AdsDetail> adsDetails = ads.getAds();
            for (int i = 0; i < adsDetails.size(); i++) {
                if (null != adsDetails && adsDetails.size() != 0) {
                    AdsDetail detail = adsDetails.get(0);
                    String image_url = detail.getRes_url().get(0);
                    String md5 = Md5Helper.toMD5(image_url);
                    File cacheFile = new File(Environment.getExternalStorageDirectory(), Constant.IMGCACHE);
                    File file = new File(cacheFile, md5 + ".jpg");
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        wp_splash_ads.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        wp_splash_ads.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    private void innitData() {
        String json = sharedPreferences.getString(slpash_SharedPreferences_json, "");
        long total = System.currentTimeMillis() - sharedPreferences.getLong(slpash_SharedPreferences_lastTime, 0);
        //如果缓存json存在，且超时了
        if (json.isEmpty()) {
            if (total > sharedPreferences.getInt(slpash_SharedPreferences_outTime, 0) * 60 * 1000) {
                httpRequest();
            }
        }
    }


    /**
     * 获取数据
     */
    private void httpRequest() {
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
                        intent.setClass(Splash.this, DownloadImageService.class);
                        intent.putExtra(DownloadImageService.ADS_DATA, ads);//传递数据
                        startService(intent);
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
    private void cacheRequestJson(String json, int next_req) {

        SharedPreferences.Editor editor = sharedPreferences.edit().putString(slpash_SharedPreferences_json, json)
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
