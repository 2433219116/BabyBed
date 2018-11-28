package babybed.hang.efan.babybed.activity;

import android.view.View;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;

/**
 * Created by Efan on 2018/4/3.
 */

public class StoreShellActivity extends BaseActivity {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_store_shell;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("商品发布");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
