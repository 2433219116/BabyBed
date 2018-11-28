package babybed.hang.efan.babybed.activity;

import android.view.View;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;

public class StoreDiscountActivity extends BaseActivity {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_store_discount;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("优惠选择");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initEvent() {

    }
}
