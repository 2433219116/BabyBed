package babybed.hang.efan.babybed.adapter.recycler;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.CommunityRelistBO;

/**
 * Created by Efan on 2018/3/20.
 */

public class CommunityRelistAdapter extends BaseQuickAdapter<CommunityRelistBO, BaseViewHolder> {
    public CommunityRelistAdapter(int layoutResId, @Nullable List<CommunityRelistBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityRelistBO item) {
        helper.setText(R.id.txt_community_number, item.getNumber());
        Glide.with(mContext).load(item.getImgurl()).into((ImageView) helper.getView(R.id.img_community_imgurl));
        helper.setText(R.id.txt_community_date, item.getDate());
        helper.setText(R.id.txt_community_tittle, item.getTittle());
        if (item.getCategory().equals("成长")) {
            helper.setText(R.id.txt_community_category, item.getCategory());
            helper.getView(R.id.txt_community_category).setBackgroundColor(Color.parseColor("#AEE7A2"));
        } else if (item.getCategory().equals("食品")) {
            helper.setText(R.id.txt_community_category, item.getCategory());
            helper.getView(R.id.txt_community_category).setBackgroundColor(Color.parseColor("#F4B83E"));
        } else if (item.getCategory().equals("常识")) {
            helper.setText(R.id.txt_community_category, item.getCategory());
            helper.getView(R.id.txt_community_category).setBackgroundColor(Color.parseColor("#7C2ACA"));
        } else if (item.getCategory().equals("衣物")) {
            helper.setText(R.id.txt_community_category, item.getCategory());
            helper.getView(R.id.txt_community_category).setBackgroundColor(Color.parseColor("#FB3252"));
        } else if (item.getCategory().equals("医疗")) {
            helper.setText(R.id.txt_community_category, item.getCategory());
            helper.getView(R.id.txt_community_category).setBackgroundColor(Color.parseColor("#3F51B5"));
        }
    }
}
