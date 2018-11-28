package babybed.hang.efan.babybed.adapter.recycler;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.MusicLocRelistBO;

/**
 * Created by Efan on 2018/3/31.
 */

public class MusicLocationRelistAdapter extends BaseQuickAdapter<MusicLocRelistBO, BaseViewHolder> {

    public MusicLocationRelistAdapter(int layoutResId, @Nullable List<MusicLocRelistBO> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MusicLocRelistBO item) {
        Glide.with(mContext).load(item.getCover()).into((ImageView) helper.getView(R.id.img_location_cover));
        helper.setText(R.id.txt_location_category, item.getCategory());
        helper.setText(R.id.txt_location_come, item.getCome());
        helper.setText(R.id.txt_location_name, item.getName());
    }
}
