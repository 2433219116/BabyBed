package babybed.hang.efan.babybed.adapter.recycler;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.StoreTrolleyRelistBO;

/**
 * Created by Efan on 2018/3/17.
 */

public class StoreTrolleyRelistAdapter extends BaseQuickAdapter<StoreTrolleyRelistBO, BaseViewHolder> {

    public StoreTrolleyRelistAdapter(int layoutResId, @Nullable List<StoreTrolleyRelistBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreTrolleyRelistBO item) {
        helper.setText(R.id.txt_store_trolley_price, item.getPrice());
        helper.setText(R.id.txt_store_trolley_name, item.getName());
        helper.setText(R.id.txt_store_trolley_number, item.getNumber());
        Glide.with(mContext).load(item.getImgurl()).into((ImageView) helper.getView(R.id.img_store_trolley_imgurl));
        helper.addOnClickListener(R.id.txt_store_trolley_reduce);
        helper.addOnClickListener(R.id.txt_store_trolley_plus);
    }
}
