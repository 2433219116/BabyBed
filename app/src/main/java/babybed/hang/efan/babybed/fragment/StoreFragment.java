package babybed.hang.efan.babybed.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.activity.StoreInformationActivity;
import babybed.hang.efan.babybed.activity.StoreTrolleyActivity;
import babybed.hang.efan.babybed.adapter.StoreLooperViewpagerAdapter;
import babybed.hang.efan.babybed.adapter.recycler.StoreLikeRelistAdapter;
import babybed.hang.efan.babybed.adapter.recycler.StoreRecommendRegridAdapter;
import babybed.hang.efan.babybed.base.BaseFragment;
import babybed.hang.efan.babybed.bean.StoreLikeRelistBO;
import babybed.hang.efan.babybed.bean.StoreRecRegridBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Efan on 2018/3/13.
 */

public class StoreFragment extends BaseFragment implements OnRefreshListener {
    private FloatingActionButton mFabShoppingTrolley;
    private LoopViewPager        loopViewPager;
    private InkPageIndicator     inkPageIndicator;
    private RecyclerView         mRecyclerViewLike, mRecyclerViewRecommend;

    private SwipeRefreshLayout          swipeRefreshLayout;
    private List<StoreLikeRelistBO>     mRelist;
    private List<StoreRecRegridBO>      mRegrid;
    private StoreRecommendRegridAdapter mRegridAdapter;
    private StoreLikeRelistAdapter      mRelistAdapter;

    private static String RELIST = "1";
    private static String REGRID = "2";

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_store;
    }

    @Override
    protected void initView() {
        mFabShoppingTrolley = bindViewId(R.id.fab_store_shopping_trolley);

        loopViewPager = bindViewId(R.id.looper_viewpager_store);
        inkPageIndicator = bindViewId(R.id.inkpage_indicator_store);

        mRecyclerViewLike = bindViewId(R.id.relist_store_like);
        mRecyclerViewRecommend = bindViewId(R.id.relist_store_recommend);

        swipeRefreshLayout = bindViewId(R.id.swipe_refresh_store);
    }

    @Override
    protected void initData() {
        findBmobRelistData();
        findBmobRegridData();
    }


    @Override
    protected void initEvent() {
        setLooperAdapter();
        setFabListener();
        setRefresh();
    }


    @Override
    protected void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setLooperAdapter() {
        loopViewPager.setAdapter(new StoreLooperViewpagerAdapter(getActivity()));
        loopViewPager.setLooperPic(true);
        inkPageIndicator.setViewPager(loopViewPager);
    }

    private void setFabListener() {
        mFabShoppingTrolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addCollection(view);
                toStoreTrolleyActivity();
            }


        });
    }

    private void toStoreTrolleyActivity() {
        Intent intent = new Intent(getActivity(), StoreTrolleyActivity.class);
        startActivity(intent);

    }

    private void addCollection(View view) {

    }

    private void findBmobRelistData() {
        BmobQuery<StoreLikeRelistBO> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<StoreLikeRelistBO>() {
            @Override
            public void done(List<StoreLikeRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(getActivity(), "查询成功：共" + object.size() + "条数据。");
                    for (StoreLikeRelistBO relistBO : object) {
                        setBmobRelistData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setBmobRelistData(List<StoreLikeRelistBO> object) {
        mRelist = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            StoreLikeRelistBO relistBO = new StoreLikeRelistBO();
            relistBO.name = object.get(i).getName();
            relistBO.price = object.get(i).getPrice();
            relistBO.imgurl = object.get(i).getImgurl();
            mRelist.add(relistBO);
        }
        setRelist();
    }

    private void findBmobRegridData() {
        BmobQuery<StoreRecRegridBO> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<StoreRecRegridBO>() {
            @Override
            public void done(List<StoreRecRegridBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(getActivity(), "查询成功：共" + object.size() + "条数据。");
                    for (StoreRecRegridBO regridBO : object) {
                        setBmobRegridData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setBmobRegridData(List<StoreRecRegridBO> object) {
        mRegrid = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            StoreRecRegridBO regridBO = new StoreRecRegridBO();
            regridBO.imgurl = object.get(i).getImgurl();
            regridBO.name = object.get(i).getName();
            regridBO.price = object.get(i).getPrice();
            mRegrid.add(regridBO);
        }
        setRegrid();
    }

    private void setRelist() {
        mRelistAdapter = new StoreLikeRelistAdapter(R.layout.item_relist_store_like, mRelist);
        mRelistAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick: ");
                toStoreInformation(String.valueOf(position), RELIST);
            }
        });
        mRecyclerViewLike.setAdapter(mRelistAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewLike.setLayoutManager(manager);
    }

    private void toStoreInformation(String position, String category) {
        Intent intent = new Intent(getActivity(), StoreInformationActivity.class);
        intent.putExtra("position", position);
        Log.i("aaa", position);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void setRegrid() {
        mRegridAdapter = new StoreRecommendRegridAdapter(R.layout.item_regrid_store_recommend, mRegrid);
        mRegridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.d(TAG, "onItemClick: ");
//                toStoreInformation(position,REGRID);
            }
        });
        mRecyclerViewRecommend.setAdapter(mRegridAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerViewRecommend.setLayoutManager(manager);
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
        //因为本例中没有从网络获取数据，因此这里使用Handler演示4秒延迟来从服务器获取数据的延迟现象，以便于大家
        // 能够看到listView正在刷新的状态。大家在现实使用时只需要使用run（）{}方法中的代码就行了。
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRelist.clear();
                mRegrid.clear();
                //获取最新的list数据
                findBmobRelistData();
                findBmobRegridData();
                //通知界面显示，
                mRegridAdapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }


}
