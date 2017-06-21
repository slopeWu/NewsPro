package wp.newspro;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.zip.Inflater;

import wp.newspro.Fragment.MineFragment;
import wp.newspro.Fragment.NewsFragment;
import wp.newspro.Fragment.ReadingFragment;
import wp.newspro.Fragment.TopicFragment;
import wp.newspro.Fragment.VedioFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost fragmentTabHost;
    private static Context mContext;
    private Class[] mClass = new Class[]{NewsFragment.class, ReadingFragment.class, VedioFragment.class, TopicFragment.class, MineFragment.class};
    private int[] mSelect = new int[]{R.drawable.select_news, R.drawable.select_reading, R.drawable.select_vedio, R.drawable.select_topic, R.drawable.select_mine};
    private String[] tabSpecArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        innitView();
    }

    private void innitView() {
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.wp_fragmentTabHost);
        //绑定容器
        fragmentTabHost.setup(mContext, getSupportFragmentManager(), R.id.content);

        //生成标签
        tabSpecArray = getResources().getStringArray(R.array.tabSpec);
        for (int i = 0; i < tabSpecArray.length; i++) {
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(i + "");
            tabSpec.setIndicator(getTabSpecView(i));
            fragmentTabHost.addTab(tabSpec, mClass[i], null);
        }
//        TabHost.TabSpec oneTabSpec = fragmentTabHost.newTabSpec("1").setIndicator(getTabSpecView());
//        TabHost.TabSpec twoTabSpec = fragmentTabHost.newTabSpec("2").setIndicator(getTabSpecView());
//        TabHost.TabSpec threeTabSpec = fragmentTabHost.newTabSpec("3").setIndicator(getTabSpecView());
//
//        //添加标签
//        fragmentTabHost.addTab(oneTabSpec, NewsFragment.class, null);
//        fragmentTabHost.addTab(twoTabSpec, ReadingFragment.class, null);
//        fragmentTabHost.addTab(threeTabSpec, TopicFragment.class, null);
    }

    private View getTabSpecView(int index) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.itemtabspec_view, null);
        ImageView wp_tabSpecPic = (ImageView) view.findViewById(R.id.wp_tabSpecPic);
        TextView wp_tabSpecName = (TextView) view.findViewById(R.id.wp_tabSpecName);
        wp_tabSpecPic.setImageResource(mSelect[index]);
        wp_tabSpecName.setText(tabSpecArray[index]);
        return view;
    }
}
