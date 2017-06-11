package wp.newspro.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;

import wp.newspro.Fragment.NewsInfoFragment.Bean.TopDetail;
import wp.newspro.R;

/**
 * Created by Administrator on 2017/6/11.
 */

public class FragmentTopAdapter extends BaseAdapter {
    private List<TopDetail> mTopDetail;
    private Context mContext;

    public FragmentTopAdapter(List<TopDetail> mTopDetail, Context mContext) {
        WeakReference<Context> weakReference = new WeakReference<Context>(mContext);
        Context tempContext = weakReference.get();
        if (null == tempContext) return;
        this.mTopDetail = mTopDetail;
        this.mContext = tempContext;
    }

    @Override
    public int getCount() {
        return mTopDetail.size();
    }

    @Override
    public TopDetail getItem(int position) {
        return mTopDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.item_top, null);
            TopViewHolder viewHolder = new TopViewHolder();
            viewHolder.wp_itme_top_image = (SimpleDraweeView) view.findViewById(R.id.wp_itme_top_image);
            viewHolder.wp_itme_top_title = (TextView) view.findViewById(R.id.wp_itme_top_title);
            viewHolder.wp_item_top_resouse = (TextView) view.findViewById(R.id.wp_item_top_resouse);
            viewHolder.wp_item_top_replyCount = (TextView) view.findViewById(R.id.wp_item_top_replyCount);
            view.setTag(viewHolder);
            convertView = view;
        }

        TopDetail topDetail = mTopDetail.get(position);
        TopViewHolder viewHolder = (TopViewHolder) convertView.getTag();

        viewHolder.wp_itme_top_image.setImageURI(Uri.parse(topDetail.getImg()));
        viewHolder.wp_itme_top_title.setText(topDetail.getTitle());
        viewHolder.wp_item_top_resouse.setText(topDetail.getSource());
        viewHolder.wp_item_top_replyCount.setText(topDetail.getReplyCount() + "");

        return convertView;
    }

    public class TopViewHolder {
        SimpleDraweeView wp_itme_top_image;
        TextView wp_itme_top_title;
        TextView wp_item_top_resouse;
        TextView wp_item_top_replyCount;
    }
}
