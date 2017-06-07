package wp.newspro.Splash.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import wp.newspro.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String WEBVIEWACTIVITY_URL = "WebViewActivity_Url";//存储数据的flag
    private static WebView webView;
    private static String action_params;//广告详情的url
    private static LinearLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        action_params = intent.getStringExtra(WEBVIEWACTIVITY_URL);
        innitView();
        showPage();
    }

    //展示page
    private static void showPage() {
        //启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(action_params);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(action_params);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空子view,防止leaks
        relativeLayout.removeAllViews();
        relativeLayout=null;
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    private void innitView() {
        //动态创建webView防止内存溢出
        relativeLayout = (LinearLayout) findViewById(R.id.wp_ll_webView);
        webView = new WebView(getApplicationContext());
        relativeLayout.addView(webView);
    }
}
