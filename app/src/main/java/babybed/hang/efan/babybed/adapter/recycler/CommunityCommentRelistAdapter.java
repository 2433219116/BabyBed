package babybed.hang.efan.babybed.adapter.recycler;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.bean.CommunityComRelistBO;

/**
 * Created by Efan on 2018/4/1.
 */

public class CommunityCommentRelistAdapter extends BaseQuickAdapter<CommunityComRelistBO, BaseViewHolder> {
    public CommunityCommentRelistAdapter(int layoutResId, @Nullable List<CommunityComRelistBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityComRelistBO item) {

    }
}
