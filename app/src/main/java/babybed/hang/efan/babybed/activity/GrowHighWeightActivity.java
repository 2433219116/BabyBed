package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.GrowHighWeightRelistAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.GrowHiWeRelistBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Efan on 2018/3/27.
 */

public class GrowHighWeightActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener {
    private RecyclerView                mRecyclerView;
    private GrowHighWeightRelistAdapter mAdapter;
    private List<GrowHiWeRelistBO>      mList;
    private GrowHiWeRelistBO            mRelistBO;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String mObjectId;
    private int    mPosition;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_grow_high_weight;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("历史测量");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setToolbarRightIcon(R.mipmap.ic_add_high_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddHighWeight();
            }
        });
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.relist_grow_high_weight);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_high_weight);

    }

    @Override
    protected void initData() {
        findBmobRelistData();
    }

    @Override
    protected void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initEvent() {
        setRefresh();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.txt_grow_high_weight:
//                break;
        }
    }

    private void findBmobRelistData() {
        BmobQuery<GrowHiWeRelistBO> query = new BmobQuery<>();
        query.findObjects(new FindListener<GrowHiWeRelistBO>() {
            @Override
            public void done(List<GrowHiWeRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(GrowHighWeightActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (GrowHiWeRelistBO relistBO : object) {
                        setBmobRelistData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setBmobRelistData(List<GrowHiWeRelistBO> object) {
        mList = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            mRelistBO = new GrowHiWeRelistBO();
            mRelistBO.head = object.get(i).getHead();
            mRelistBO.high = object.get(i).getHigh();
            mRelistBO.weight = object.get(i).getWeight();
            mRelistBO.date = object.get(i).getDate();
            mRelistBO.bmi = object.get(i).getBmi();
            mList.add(mRelistBO);
        }
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new GrowHighWeightRelistAdapter(R.layout.item_relist_grow_high_weight, mList);
        //滑动删除
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GrowHighWeightActivity.this));
    }


    private void toAddHighWeight() {
        Intent intent = new Intent(GrowHighWeightActivity.this, GrowAddHighWeightActivity.class);
        startActivity(intent);
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

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                //获取最新的list数据
                findBmobRelistData();
                //通知界面显示，
                mAdapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            deleteBmobData(pos);
        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };

    private void deleteBmobData(int pos) {
        mPosition = pos;
        BmobQuery<GrowHiWeRelistBO> query2 = new BmobQuery<>();
        query2.setLimit(50);
        query2.findObjects(new FindListener<GrowHiWeRelistBO>() {
            @Override
            public void done(List<GrowHiWeRelistBO> object, BmobException e) {
                if (e == null) {
                    mObjectId = object.get(mPosition).getObjectId();
                    Log.i("bmob2", String.valueOf(mPosition));
                    Log.i("bmob", mObjectId);
                    if (!mObjectId.equals("")) {
                        GrowHiWeRelistBO relistBO = new GrowHiWeRelistBO();
                        relistBO.setObjectId(mObjectId);
                        relistBO.delete(new UpdateListener() {
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
}
