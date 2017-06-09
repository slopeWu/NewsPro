package wp.newspro.Splash;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import wp.newspro.Constance.Constant;
import wp.newspro.Splash.Bean.Ads;
import wp.newspro.Splash.Bean.AdsDetail;
import wp.newspro.Util.Md5Helper;

/**
 * Created by Administrator on 2017/5/31.
 * 1、可以处理耗时操作
 * 2、处理完自己关闭，无需程序员管理
 */

public class DownloadImageService extends IntentService {
    public static final String ADS_DATA = "ads";//存取对象的flag
    private Ads ads;//广告对象

    public DownloadImageService() {
        super("DownloadImageService");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //启动service执行的方法
        ads = (Ads) intent.getSerializableExtra(ADS_DATA);
        //下载图片
        List<AdsDetail> adsDetails = this.ads.getAds();
        for (int i = 0; i < adsDetails.size(); i++) {
            AdsDetail adsDetail = adsDetails.get(i);

            List<String> res_url = adsDetail.getRes_url();
            if (null != res_url) {
                //只要第一张图片
                String url = res_url.get(0);
                if (!url.isEmpty()) {
                    //图片地址不为空，下载
                    String md5 = Md5Helper.toMD5(url);
                    //校验，已经存在的就不要下载
                    //下载图片
                    if (!checkImageExist(md5)) {
                        downLoadImage(url, md5);
                    }
                }
            }
        }
    }

    /**
     * 判断图片是否存在
     *
     * @param md5
     */
    private boolean checkImageExist(String md5) {
        File SD = Environment.getExternalStorageDirectory();
        File cacheFile = new File(SD, Constant.IMGCACHE);
        File file = new File(cacheFile, md5 + ".jpg");
        if (file.exists() || BitmapFactory.decodeFile(file.getAbsolutePath()) != null) {
            return true;
        } else {
            return false;
        }
    }

    //下载图片
    private void downLoadImage(String pic_url, String md5Name) {
        try {
            URL url = new URL(pic_url);
            URLConnection urlConnection = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
            //保存图片
            saveToSdCard(bitmap, md5Name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到sd卡
     *
     * @param bitmap
     * @param md5Name
     */
    private void saveToSdCard(Bitmap bitmap, String md5Name) throws Exception {
        if (null == bitmap) return;
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File SD = Environment.getExternalStorageDirectory();
            File cachefile = new File(SD, Constant.IMGCACHE);//.开头的是隐藏文件
            if (!cachefile.exists()) {
                cachefile.mkdirs();
            }
            File file = new File(cachefile, md5Name + ".jpg");
            if (file.exists()) return;

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);//质量60，压缩掉40%
            outputStream.flush();
            outputStream.close();

        }
    }
}
