package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.StoreInfoLooperViewpagerAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.StoreLikeRelistBO;
import babybed.hang.efan.babybed.bean.StoreTrolleyId;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Efan on 2018/4/2.
 */

public class StoreInformationActivity extends BaseActivity implements View.OnClickListener {
    private LoopViewPager mLoopViewPager;
    private InkPageIndicator mInkPageIndicator;

    private String mPosition;
    private String mCategory;

    private TextView mTxtPurchase;

    private String ObjectId;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_store_information;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("商品详情");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setToolbarLeftIcon(R.mipmap.ic_more);
    }

    @Override
    protected void initView() {
        mLoopViewPager = findViewById(R.id.looper_viewpager_store_information);
        mInkPageIndicator = findViewById(R.id.inkpage_indicator_store_information);

        mTxtPurchase = findViewById(R.id.txt_store_purchase);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPosition = intent.getStringExtra("position");
        Log.i("aaaaa", mPosition);
        mCategory = intent.getStringExtra("category");
        Log.i("aaaaa", mCategory);
        queryBmobPurchaseData();
    }


    @Override
    protected void initListener() {
        mLoopViewPager.setLooperPic(true);
        mLoopViewPager.setAdapter(new StoreInfoLooperViewpagerAdapter(StoreInformationActivity.this));
        mInkPageIndicator.setViewPager(mLoopViewPager);
        mTxtPurchase.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_store_purchase:
                updataBmobPurchaseData();
//                Snackbar.make(v, "商品已经添加购物车中", Snackbar.LENGTH_LONG)
//                        .setAction("撤销", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                ToastUtils.show(StoreInformationActivity.this, "撤销成功", 1000);
//                            }
//                        }).show();
                toStoreTrolley();
                break;
        }
    }

    private void toStoreTrolley() {
        finish();
        Intent intent = new Intent(StoreInformationActivity.this, StoreTrolleyActivity.class);
        startActivity(intent);
    }

    private void queryBmobPurchaseData() {
        if (mCategory.equals("1")) {
            BmobQuery<StoreLikeRelistBO> query = new BmobQuery<>();
            query.setLimit(50);
            //执行查询方法
            query.findObjects(new FindListener<StoreLikeRelistBO>() {
                @Override
                public void done(List<StoreLikeRelistBO> object, BmobException e) {
                    if (e == null) {
                        ToastUtils.showShort(StoreInformationActivity.this, "查询成功：共" + object.size() + "条数据。");
                        for (StoreLikeRelistBO gameScore : object) {
                            ObjectId = object.get(0).getObjectId();
                            Log.i("aaaa", mPosition);
                        }
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        } else if (mCategory.equals("2")) {

        }
    }

    private void updataBmobPurchaseData() {
        StoreTrolleyId gameScore = new StoreTrolleyId();
        gameScore.setShoppingid(ObjectId);
        gameScore.update("e258e33037", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "成功");
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
