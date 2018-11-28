package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eagle.pay66.Pay66;
import com.eagle.pay66.listener.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.StoreTrolleyRelistAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.StoreLikeRelistBO;
import babybed.hang.efan.babybed.bean.StoreTrolleyRelistBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class StoreOrderInforActivity extends BaseActivity implements View.OnClickListener {
    private String ShoppingName = "儿童毛衣", shoppingDec = "适合儿童穿戴", shoppingOrderId = "1", shoppingTime = "1", payWay = "AliPay", payAll = "0.01", objectId;

    private RecyclerView mRecyclerView;
    private StoreTrolleyRelistAdapter mAdapter;
    private List<StoreTrolleyRelistBO> mList;
    private StoreTrolleyRelistBO mRelistBO;

    private Button mBtnConfirm;
    private TextView mTxtOrderId, mTxtOrderTime, mTxtOrderState, mTxtOrderPay;
    private CheckBox mCbAlipay, mCbWxpay, mCbBabyPay;

    private RelativeLayout mRlDiscount;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("AAAAAA", "position");
                    mTxtOrderId.setText(shoppingOrderId);
                    mTxtOrderTime.setText(shoppingTime);
                    break;
                case 2:
                    mTxtOrderPay.setText(payAll + ".00￥");
                    break;
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_store_order_infor;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setToolbarTitle("订单详情");
    }

    @Override
    protected void initView() {
        Pay66.init("7a2a8765b3d64f7883abd0a01e6493f2", getApplicationContext());

        mBtnConfirm = findViewById(R.id.btn_order_confirm);
        mTxtOrderId = findViewById(R.id.txt_order_id);
        mTxtOrderTime = findViewById(R.id.txt_order_time);
        mTxtOrderState = findViewById(R.id.txt_order_state);
        mTxtOrderPay = findViewById(R.id.txt_order_pay);
        mCbAlipay = findViewById(R.id.cb_order_alipay);
        mCbWxpay = findViewById(R.id.cb_order_wxpay);
        mCbBabyPay = findViewById(R.id.cb_order_babypay);
        mRlDiscount = findViewById(R.id.rl_order_discount);

        mRecyclerView = findViewById(R.id.relist_confirm_infor);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        payAll = intent.getStringExtra("pay");
        Log.i("asdasd", payAll);
        objectId = intent.getStringExtra("id");
        queryBmobData();
    }


    @Override
    protected void initListener() {
        mBtnConfirm.setOnClickListener(this);
        mCbAlipay.setOnClickListener(this);
        mCbWxpay.setOnClickListener(this);
        mCbBabyPay.setOnClickListener(this);
        mRlDiscount.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        Log.i("AAAAAA", "1");
        PayCrateOrder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_order_confirm:
                payWay();
                deleteBmobData();
                break;
            case R.id.cb_order_alipay:
                mCbWxpay.setChecked(false);
                payWay = "AliPay";
                break;
            case R.id.cb_order_wxpay:
                mCbAlipay.setChecked(false);
                payWay = "WxPay";
                break;
            case R.id.cb_order_babypay:
                ToastUtils.show(this, "本地钱包还未开发", Toast.LENGTH_SHORT);
                break;
            case R.id.rl_order_discount:
                toDiscount();
                break;
        }
    }

    private void queryBmobData() {
        BmobQuery<StoreLikeRelistBO> query = new BmobQuery<StoreLikeRelistBO>();
        query.addWhereEqualTo("objectId", objectId);
        query.setLimit(50);
        query.findObjects(new FindListener<StoreLikeRelistBO>() {
            @Override
            public void done(List<StoreLikeRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreOrderInforActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (StoreLikeRelistBO gameScore : object) {
                        setRelistData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setRelistData(List<StoreLikeRelistBO> relistData) {
        mList = new ArrayList<>();
        for (int i = 0; i < relistData.size(); i++) {
            mRelistBO = new StoreTrolleyRelistBO();
            mRelistBO.imgurl = relistData.get(i).getImgurl();
            mRelistBO.price = relistData.get(i).getPrice();
            mRelistBO.name = relistData.get(i).getName();
            mRelistBO.number = "1";
            mRelistBO.isselect = true;
            mList.add(mRelistBO);
        }
        setRelist();
        Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);
    }

    private void setRelist() {
        mAdapter = new StoreTrolleyRelistAdapter(R.layout.item_relist_store_trolley, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreOrderInforActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void deleteBmobData() {
        BmobQuery<StoreTrolleyRelistBO> query = new BmobQuery<StoreTrolleyRelistBO>();
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<StoreTrolleyRelistBO>() {
            @Override
            public void done(List<StoreTrolleyRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(StoreOrderInforActivity.this, "查询成功：共" + object.size() + "条数据。");
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

    private void toDiscount() {
        Intent intent = new Intent(StoreOrderInforActivity.this, StoreDiscountActivity.class);
        startActivity(intent);
    }

    private void PayCrateOrder() {
        Log.i("AAAAAA", "2");
        if (!ShoppingName.equals("") && !shoppingDec.equals("")) {
            Pay66.createOrder(Integer.parseInt(payAll) * 100, ShoppingName, shoppingDec, new CommonListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(int i, String s) {
                    Log.i("AAAAAA", s);
                }

                @Override
                public void onSuccess(String s) {
                    JsonOrder(s);
                    Log.i("AAAAAA", "3");
                }

                @Override
                public void onCompleted() {

                }
            });
        }
    }

    private void JsonOrder(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String Status = jsonObject.getString("status");

            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
            String orderId = jsonObject2.getString("orderId");
            int consume = jsonObject2.getInt("consume");
            String goodsName = jsonObject2.getString("goodsName");
            String createTime = jsonObject2.getString("createTime");

            if (Status.equals("true")) {
                shoppingOrderId = orderId;
                shoppingTime = createTime;

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                Log.i("AAAAAA", shoppingOrderId + shoppingTime);
            } else {
                ToastUtils.show(this, "服务器出现问题", Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void payWay() {
        if (!payWay.equals("") && !shoppingOrderId.equals("") && !shoppingOrderId.equals("")) {
            /**
             * 将把支付结果通过CommonListener回调函数传递，不要过度依赖于回调函数返回结果。
             * activity 必须是调用支付接口的页面
             * orderId 必须由创建订单接口生成
             * 支付方式 只支持微信和支付宝，支付宝使用AliPay，微信使用WxPay
             */
            Pay66.pay(StoreOrderInforActivity.this, shoppingOrderId, Integer.parseInt(payAll) * 100, payWay, new CommonListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(int i, String s) {
                    Log.i("AAAAAA", s);
                    if (s.equals("内嵌APP不存在")) {
                        ToastUtils.showShort(StoreOrderInforActivity.this, "请确保安装支付软件");
                    }
                }

                @Override
                public void onSuccess(String s) {
                    JsonPayWay(s);
                    //todo
                }

                @Override
                public void onCompleted() {

                }
            });
        }
    }

    private void JsonPayWay(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String Status = jsonObject.getString("status");
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");

            Log.i("AAAAAA", Status + code + msg);
            if (Status.equals("false")) {
                ToastUtils.showShort(this, "状态码" + code + "；信息" + msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
