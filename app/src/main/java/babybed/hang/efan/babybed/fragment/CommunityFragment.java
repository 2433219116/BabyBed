package babybed.hang.efan.babybed.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.activity.CommunityArticleActivity;
import babybed.hang.efan.babybed.adapter.recycler.CommunityRelistAdapter;
import babybed.hang.efan.babybed.base.BaseFragment;
import babybed.hang.efan.babybed.bean.CommunityRelistBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import babybed.hang.efan.babybed.weight.MyDividerItemDecoration;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Efan on 2018/3/13.
 */

public class CommunityFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView            mRecyclerView;
    private CommunityRelistAdapter  mAdapter;
    private CommunityRelistBO       mRelistBO;
    private List<CommunityRelistBO> mList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView() {
        mRecyclerView = bindViewId(R.id.relist_community);
        swipeRefreshLayout = bindViewId(R.id.swipe_refresh_community);
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
        setRelist();
        setRefresh();
    }


    @Override
    public void onClick(View view) {

    }

    private void findBmobRelistData() {
        BmobQuery<CommunityRelistBO> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
//        query.addWhereEqualTo("playerName", "比目");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<CommunityRelistBO>() {
            @Override
            public void done(List<CommunityRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(getActivity(), "查询成功：共" + object.size() + "条数据。");
                    for (CommunityRelistBO relistBO : object) {
                        setBmobRelistData(object);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void setBmobRelistData(List<CommunityRelistBO> object) {
        mList = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            mRelistBO = new CommunityRelistBO();
            mRelistBO.tittle = object.get(i).getTittle();
            mRelistBO.number = object.get(i).getNumber();
            mRelistBO.imgurl = object.get(i).getImgurl();
            mRelistBO.date = object.get(i).getDate();
            mRelistBO.category = object.get(i).getCategory();
            mRelistBO.content = object.get(i).getContent();
            mList.add(mRelistBO);
        }
        setRelist();
    }

    private void setRelist() {
        mAdapter = new CommunityRelistAdapter(R.layout.item_relist_community, mList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toCommunityArticle(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), MyDividerItemDecoration.VERTICAL_LIST));
    }

    private void toCommunityArticle(int position) {
        Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
        intent.putExtra("key", String.valueOf(position));
        startActivity(intent);
    }


    private void setRefresh() {
        //设置圆圈进度条的背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        //设置进度条变化的颜色
        swipeRefreshLayout.setColorSchemeResources( R.color.colorViolet,
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
}
