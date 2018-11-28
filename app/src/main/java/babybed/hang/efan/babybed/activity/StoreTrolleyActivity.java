package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.StoreTrolleyRelistAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.GrowHiWeRelistBO;
import babybed.hang.efan.babybed.bean.StoreLikeRelistBO;
import babybed.hang.efan.babybed.bean.StoreTrolleyId;
import babybed.hang.efan.babybed.bean.StoreTrolleyRelistBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Efan on 2018/3/20.
 */

public class StoreTrolleyActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private StoreTrolleyRelistAdapter mAdapter;
    private List<StoreTrolleyRelistBO> mList, mListBefore;
    private StoreTrolleyRelistBO mRelistBO;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int pay = 1;
    private TextView mTxtMoney;
    private TextView number;

    private int moneyAll;
    private String objectid;
    private RelativeLayout mRlTrolleyPay;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mTxtMoney.setText(String.valueOf(moneyAll) + ".00￥");
                    break;
                case 1:
                    mTxtMoney.setText( "0.00￥");
                    break;
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_store_trolley;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("购物车");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setToolbarRightIcon(R.mipmap.ic_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBmobData();
            }


        });
    }


    @Override
    protected void initView() {
        mRlTrolleyPay = findViewById(R.id.rl_trolley_pay);
        mRecyclerView = findViewById(R.id.relist_store_trolley);
        mTxtMoney = findViewById(R.id.txt_store_trolley_money);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_trolley);
    }

    @Override
    protected void initData() {
        queryObjectBmobData();
    }


    @Override
    protected void initListener() {
        mRlTrolleyPay.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initEvent() {
        setRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_trolley_pay:
                toStoreOrderInfor();
                break;
        }
    }

    private void setRefresh() {
        //设置圆圈进度条的背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                getResources().getColor(R.color.colorWhite));

        //设置进度条变化的颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorViolet,
                R.color.colorRed,
                R.color.colorYellow);
    }

    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                //获取最新的list数据
                queryPurchaseBmobData();
                //通知界面显示，
                mAdapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    private void queryPurchaseBmobData() {
        BmobQuery<StoreTrolleyRelistBO> query = new BmobQuery<>();
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<StoreTrolleyRelistBO>() {
            @Override
            public void done(List<StoreTrolleyRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreTrolleyActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (StoreTrolleyRelistBO gameScore : object) {
                        setRelistData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }


    private void queryObjectBmobData() {
        BmobQuery<StoreTrolleyId> query = new BmobQuery<StoreTrolleyId>();
        query.addWhereEqualTo("objectId", "e258e33037");
        query.setLimit(50);
        query.findObjects(new FindListener<StoreTrolleyId>() {
            @Override
            public void done(List<StoreTrolleyId> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreTrolleyActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (StoreTrolleyId gameScore : object) {
                        objectid = object.get(0).getShoppingid();
                        BmobQuery<StoreLikeRelistBO> query = new BmobQuery<StoreLikeRelistBO>();
                        query.addWhereEqualTo("objectId", objectid);
                        query.setLimit(50);
                        query.findObjects(new FindListener<StoreLikeRelistBO>() {
                            @Override
                            public void done(List<StoreLikeRelistBO> object, BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShort(StoreTrolleyActivity.this, "查询成功：共" + object.size() + "条数据。");
                                    for (StoreLikeRelistBO gameScore : object) {
                                        setRelistDataBefore(object);
                                        saveBmobTrolleyData();
                                    }
                                } else {
                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setRelistDataBefore(List<StoreLikeRelistBO> object) {
        mListBefore = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            mRelistBO = new StoreTrolleyRelistBO();
            mRelistBO.imgurl = object.get(i).getImgurl();
            mRelistBO.price = object.get(i).getPrice();
            mRelistBO.name = object.get(i).getName();
            mRelistBO.number = "1";
            mRelistBO.isselect = true;
            mListBefore.add(mRelistBO);
        }
    }

    private void deleteBmobData() {
        BmobQuery<StoreTrolleyRelistBO> query = new BmobQuery<>();
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<StoreTrolleyRelistBO>() {
            @Override
            public void done(List<StoreTrolleyRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreTrolleyActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (StoreTrolleyRelistBO gameScore : object) {
                        //获得数据的objectId信息
                        String str = object.get(0).getObjectId();
                        StoreTrolleyRelistBO gameScore2 = new StoreTrolleyRelistBO();
                        gameScore2.setObjectId(str);
                        gameScore2.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i("bmob", "成功");
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                } else {
                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    private void saveBmobTrolleyData() {
        StoreTrolleyRelistBO gameScore = new StoreTrolleyRelistBO();
        //注意：不能调用gameScore.setObjectId("")方法
        gameScore.setImgurl(mListBefore.get(0).getImgurl());
        gameScore.setName(mListBefore.get(0).getName());
        gameScore.setNumber(mListBefore.get(0).getNumber());
        gameScore.setIsselect(mListBefore.get(0).getIsselect());
        gameScore.setPrice(mListBefore.get(0).getPrice());
        gameScore.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreTrolleyActivity.this, "创建数据成功：" + objectId);
                    queryPurchaseBmobData();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void toStoreOrderInfor() {
        Intent intent = new Intent(this, StoreOrderInforActivity.class);
        intent.putExtra("pay", String.valueOf(moneyAll));
        intent.putExtra("id", objectid);
        startActivity(intent);
    }

    private void setRelistData(List<StoreTrolleyRelistBO> relistData) {
        moneyAll = 0;
        mList = new ArrayList<>();
        for (int i = 0; i < relistData.size(); i++) {
            mRelistBO = new StoreTrolleyRelistBO();
            mRelistBO.imgurl = relistData.get(i).getImgurl();
            mRelistBO.price = relistData.get(i).getPrice();
            mRelistBO.name = relistData.get(i).getName();
            mRelistBO.number = "1";
            moneyAll = moneyAll + Integer.parseInt(mRelistBO.price);
            mRelistBO.isselect = true;
            mList.add(mRelistBO);
        }
        setRelist();
        Log.i("sss", String.valueOf(moneyAll));
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
    }

    private void setRelist() {
//        View view = getLayoutInflater().inflate(R.layout.item_relist_store_trolley, null);
//        number = view.findViewById(R.id.txt_store_trolley_number);
//        number.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ToastUtils.showLong(StoreTrolleyActivity.this, 111);
//            }
//        });
//        TextView textView = view.findViewById(R.id.txt_store_trolley_plus);
//        final int plus = Integer.valueOf(number.getText().toString()) + 1;
//        if (number.getText().toString().equals("0")) {
//            textView.setClickable(false);
//        } else {
//            textView.setClickable(true);
//        }
//        final int reduce = Integer.valueOf(number.getText().toString()) - 1;
//        mAdapter = new StoreTrolleyRelistAdapter(R.layout.item_relist_store_trolley, mList);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (view.getId()) {
//                    case R.id.txt_store_trolley_plus:
//                        number.setText(String.valueOf(plus));
//                        ToastUtils.show(StoreTrolleyActivity.this, String.valueOf(plus), 1000);
//                        break;
//                    case R.id.txt_store_trolley_reduce:
//                        number.setText(String.valueOf(reduce));
//                        break;
//                }
//            }
//        });
        mAdapter = new StoreTrolleyRelistAdapter(R.layout.item_relist_store_trolley, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreTrolleyActivity.this, LinearLayoutManager.VERTICAL, false));
    }

}
