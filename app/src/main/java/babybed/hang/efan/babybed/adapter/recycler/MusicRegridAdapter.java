package babybed.hang.efan.babybed.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.MusicRegridBO;

/**
 * Created by Efan on 2018/3/20.
 */

public class MusicRegridAdapter extends BaseQuickAdapter<MusicRegridBO, BaseViewHolder> {

    public MusicRegridAdapter(int layoutResId, @Nullable List<MusicRegridBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicRegridBO item) {
        helper.setText(R.id.txt_music_number, item.getNumber());
        helper.setText(R.id.txt_music_name, item.getName());
        Glide.with(mContext).load(item.getCoverurl()).into((ImageView) helper.getView(R.id.img_music_cover));
    }
}
