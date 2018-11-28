package babybed.hang.efan.babybed.adapter.recycler;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.bean.GrowHiWeRelistBO;

/**
 * Created by Efan on 2018/3/28.
 */

public class GrowHighWeightRelistAdapter extends BaseItemDraggableAdapter<GrowHiWeRelistBO, BaseViewHolder> {
    private float mfbmi;

    public GrowHighWeightRelistAdapter(int layoutResId, @Nullable List<GrowHiWeRelistBO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GrowHiWeRelistBO item) {
        helper.setText(R.id.txt_grow_high_weight_date, item.getDate());
        helper.setText(R.id.txt_grow_high_weight_high, item.getHigh());
        helper.setText(R.id.txt_grow_high_weight_weight, item.getWeight());
        helper.setText(R.id.txt_grow_high_weight_head, item.getHead());
        helper.setText(R.id.txt_grow_high_weight_bmi, String.valueOf(item.getBmi()));
        mfbmi = item.getBmi();
        if (mfbmi < 18.5f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体较轻，请注意补充营养，均衡膳食");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#E46E62"));
        } else if (mfbmi > 18.4f && mfbmi < 23.9f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体非常健康，注意保持哦");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#AEE7A2"));
        } else if (mfbmi > 23.9f && mfbmi < 28.0f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体偏胖，可稍微注意饮食");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#E46E62"));
        } else if (mfbmi > 27.9f && mfbmi < 30.0f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体肥胖，需注意饮食");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#F4B83E"));
        } else if (mfbmi > 30.0f&& mfbmi < 39.9f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体重度肥胖，需加强锻炼，控制饮食情况");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#FB3252"));
        } else if (mfbmi > 40.0f) {
            helper.setText(R.id.txt_grow_high_weight_advice, "宝宝身体极度肥胖，应严控制饮食，保持营养均衡");
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_high)).setTextColor(Color.parseColor("#AEE7A2"));
//            ((TextView)helper.getView(R.id.txt_grow_high_weight_weight)).setTextColor(Color.parseColor("#FB3252"));
        }
    }
}
