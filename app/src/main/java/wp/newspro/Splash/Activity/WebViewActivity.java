package wp.newspro.Splash.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import wp.newspro.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String WEBVIEWACTIVITY_URL = "WebViewActivity_Url";//存储数据的flag
    private WebView webView;
    private String action_params;//广告详情的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        action_params = intent.getStringExtra(WEBVIEWACTIVITY_URL);
        innitView();
        showPage();
    }

    private void showPage() {
        webView.loadUrl(action_params);

    }

    private void innitView() {
        webView = (WebView) findViewById(R.id.wp_wv);
    }
}
