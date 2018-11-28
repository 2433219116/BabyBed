package babybed.hang.efan.babybed.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.MusicLooperViewpagerAdapter;
import babybed.hang.efan.babybed.adapter.recycler.MusicRegridAdapter;
import babybed.hang.efan.babybed.base.BaseFragment;
import babybed.hang.efan.babybed.bean.MusicRegridBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Efan on 2018/3/13.
 */

public class MusicFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private LoopViewPager    LoopViewPager;
    private InkPageIndicator InkPageIndicator;

    private RecyclerView        mRecyclerView;
    private MusicRegridAdapter  mAdapter;
    private List<MusicRegridBO> mList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_music;
    }


    @Override
    protected void initView() {
        mRecyclerView = bindViewId(R.id.regrid_music);
        LoopViewPager = bindViewId(R.id.looper_viewpager_music);
        InkPageIndicator = bindViewId(R.id.inkpage_indicator_music);

        swipeRefreshLayout=bindViewId(R.id.swipe_refresh_music);
    }

    @Override
    protected void initData() {
        findBmobRegridData();
    }


    @Override
    protected void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initEvent() {
        setLooperViewpager();
        setRegrid();
        setRefresh();
    }


    @Override
    public void onClick(View view) {

    }


    private void findBmobRegridData() {
        BmobQuery<MusicRegridBO> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<MusicRegridBO>() {
            @Override
            public void done(List<MusicRegridBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(getActivity(), "查询成功：共" + object.size() + "条数据。");
                    for (MusicRegridBO regridBO : object) {
                        setBmobRegridData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setBmobRegridData(List<MusicRegridBO> object) {
        mList = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            MusicRegridBO regridBO = new MusicRegridBO();
            regridBO.coverurl = object.get(i).getCoverurl();
            regridBO.name = object.get(i).getName();
            regridBO.number = object.get(i).getNumber();
            mList.add(regridBO);
        }
        setRegrid();
    }

    private void setRegrid() {
        mAdapter = new MusicRegridAdapter(R.layout.item_regrid_music, mList);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
    }

    private void setLooperViewpager() {
        MusicLooperViewpagerAdapter adapter = new MusicLooperViewpagerAdapter(getActivity());
        LoopViewPager.setAdapter(adapter);
        LoopViewPager.setLooperPic(true);
        InkPageIndicator.setViewPager(LoopViewPager);
    }

    public void setRefresh() {
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
                //获取最新的list数据
                mList.clear();
                findBmobRegridData();
                //通知界面显示，
                mAdapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
}