package babybed.hang.efan.babybed.activity;

import android.view.View;
import android.widget.TextView;

import org.feezu.liuli.timeselector.TimeSelector;

import java.math.BigDecimal;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.GrowHiWeRelistBO;
import babybed.hang.efan.babybed.utils.FloatCalculatorUtils;
import babybed.hang.efan.babybed.utils.ToastUtils;
import babybed.hang.efan.babybed.weight.scaleruler.utils.DrawUtil;
import babybed.hang.efan.babybed.weight.scaleruler.view.DecimalScaleRulerView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Efan on 2018/3/27.
 */

public class GrowAddHighWeightActivity extends BaseActivity implements View.OnClickListener {
    private DecimalScaleRulerView mWeightRulerView;
    private DecimalScaleRulerView mHighRulerView;
    private DecimalScaleRulerView mHeadRulerView;

    private TextView mTxtWeight, mTxtHigh, mTxtHead;

    private float  mValueHead   = 40.0f;
    private float  mValueHigh   = 50.0f;
    private float  mValueWeight = 60.0f;
    private float mfBmi         = 0.0f;

    private TextView mTxtNext;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_grow_add;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("添加数据");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        mHeadRulerView = findViewById(R.id.ruler_head);
        mHighRulerView = findViewById(R.id.ruler_high);
        mWeightRulerView = findViewById(R.id.ruler_weight);

        mTxtHead = findViewById(R.id.txt_user_head_value);
        mTxtHigh = findViewById(R.id.txt_user_high_value);
        mTxtWeight = findViewById(R.id.txt_user_weight_value);

        mTxtNext = findViewById(R.id.txt_add_next);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mTxtNext.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        setHead();
        setHigh();
        setWight();
    }

    private void setHead() {
        mHeadRulerView.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(90), DrawUtil.dip2px(70),
                DrawUtil.dip2px(50), DrawUtil.dip2px(20), DrawUtil.dip2px(40));
        mHeadRulerView.initViewParam(mValueHead, 20.0f, 200.0f, 1);
        mHeadRulerView.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTxtHead.setText(value + "");

                mValueHead = value;
            }
        });
    }

    private void setHigh() {
        mHighRulerView.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(90), DrawUtil.dip2px(70),
                DrawUtil.dip2px(50), DrawUtil.dip2px(20), DrawUtil.dip2px(40));
        mHighRulerView.initViewParam(mValueHigh, 20.0f, 200.0f, 1);
        mHighRulerView.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTxtHigh.setText(value + "");

                mValueHigh = value;
            }
        });
    }

    private void setWight() {
        mWeightRulerView.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(90), DrawUtil.dip2px(70),
                DrawUtil.dip2px(50), DrawUtil.dip2px(20), DrawUtil.dip2px(40));
        mWeightRulerView.initViewParam(mValueWeight, 20.0f, 200.0f, 1);
        mWeightRulerView.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTxtWeight.setText(value + "");

                mValueWeight = value;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_add_next:
                saveBmobData();
                break;
        }
    }

    private void setBmi() {
        float first = FloatCalculatorUtils.multiply(mValueHigh, mValueHigh);
        float second = FloatCalculatorUtils.divide(first, 10000f);
        mfBmi = FloatCalculatorUtils.divide(mValueWeight, second, 1, BigDecimal.ROUND_HALF_UP);
    }

    private void saveBmobData() {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                if (!time.equals("")) {
                    showProgress(true, "正在添加宝宝数据,请稍后...");
                    String head = String.valueOf(mValueHead);
                    String high = String.valueOf(mValueHigh);
                    String weight = String.valueOf(mValueWeight);
                    GrowHiWeRelistBO relistBO = new GrowHiWeRelistBO();
                    relistBO.setHead(head);
                    relistBO.setWeight(weight);
                    relistBO.setHigh(high);
                    relistBO.setDate(time);
                    setBmi();
                    relistBO.setBmi(mfBmi);
                    relistBO.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                ToastUtils.showShort(GrowAddHighWeightActivity.this, "宝宝添加测量数据成功");
                                hideProgress();
                                finish();
                            } else {
                                ToastUtils.showShort(GrowAddHighWeightActivity.this, "创建数据失败：" + e.getMessage());
                                hideProgress();
                            }
                        }
                    });
                }
            }
        }, "2015-01-01 00:00", "2020-12-31 23:30");
        timeSelector.show();
    }

}
