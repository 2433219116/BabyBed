package babybed.hang.efan.babybed.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.MusicLocationRelistAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.MusicLocRelistBO;
import babybed.hang.efan.babybed.fragment.BedFragment;
import babybed.hang.efan.babybed.weight.MyDividerItemDecoration;

/**
 * Created by Efan on 2018/3/28.
 */

public class MusicLocationActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MusicLocRelistBO mListBO;
    private List<MusicLocRelistBO> mList;
    private MusicLocationRelistAdapter mAdapter;

    private int cover[] = {R.mipmap.img_music_one, R.mipmap.img_music_two, R.mipmap.img_music_three, R.mipmap.img_music_four, R.mipmap.img_music_five, R.mipmap.img_music_six, R.mipmap.img_music_two, R.mipmap.img_music_two, R.mipmap.img_music_seven};
    private String name[] = {"未完成的歌", "我害怕", "演员", "夏影（karaoke）-instrumental", "欢乐颂", "夜空的寂静", "暧昧", "动物世界", "你还要我怎样"};
    private String come[] = {"未完成的歌", "渡 (The Crossing)", "绅士", "夏影 ~あの飞行机云を超えた、その先へ~", "欢乐颂 电视原声带", "热门华语246", "渡 (The Crossing)", "渡 (The Crossing)", "意外"};
    private String category[] = {"首推", "原生", "原生", "原生", "原生", "原生", "原生", "原生", "原生"};

    Object myMethod = null;
    Method method;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_music_loaction;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("本地音乐");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.relist_music_location);

    }

    @Override
    protected void initData() {
        setRelistData();
        LinearLayoutManager manager = new LinearLayoutManager(MusicLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MusicLocationRelistAdapter(R.layout.item_relist_music_loaction, mList);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(MusicLocationActivity.this, MyDividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
        //获得这个类

    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    private void setRelistData() {
        mList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            mListBO = new MusicLocRelistBO();
            mListBO.category = category[i];
            mListBO.cover = cover[i];
            mListBO.name = name[i];
            mListBO.come = come[i];
            mList.add(mListBO);
        }
    }
}
