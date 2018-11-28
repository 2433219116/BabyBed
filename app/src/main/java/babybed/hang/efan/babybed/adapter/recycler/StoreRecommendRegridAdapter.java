package babybed.hang.efan.babybed.adapter.recycler;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.StoreRecRegridBO;

/**
 * Created by Efan on 2018/3/17.
 */

public class StoreRecommendRegridAdapter extends BaseQuickAdapter<StoreRecRegridBO, BaseViewHolder> {

    public StoreRecommendRegridAdapter(int layoutResId, @Nullable List<StoreRecRegridBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreRecRegridBO item) {
        helper.setText(R.id.txt_store_recommend_price, item.getPrice());
        helper.setText(R.id.txt_store_recommend_name, item.getName());
        Glide.with(mContext).load(item.getImgurl()).into((ImageView) helper.getView(R.id.img_store_recommend));
    }
}
