package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.CommunityCommentRelistAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.CommunityComRelistBO;
import babybed.hang.efan.babybed.bean.CommunityRelistBO;
import babybed.hang.efan.babybed.utils.ToastUtils;
import babybed.hang.efan.babybed.weight.MyDividerItemDecoration;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Efan on 2018/3/27
 */

public class CommunityArticleActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTxtTittle, mTxtCategory, mTxtDate, mTxtNumber, mTxtContent;
    private ImageView mImgUrl;

    private RecyclerView                  mRecyclerView;
    private int                           mPosition;
    private List<CommunityComRelistBO>    mList;
    private CommunityComRelistBO          mRelistBO;
    private CommunityCommentRelistAdapter mAdapter;

    private ImageView mImgShare, mImgComment, mImgCollect;

    private String mObjectId;
    private boolean isCollect = false;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_community_article;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("社区文章");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setToolbarRightIcon(R.mipmap.ic_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initView() {
        mTxtTittle = findViewById(R.id.txt_article_tittle);
        mTxtCategory = findViewById(R.id.txt_article_category);
        mTxtDate = findViewById(R.id.txt_article_date);
        mTxtNumber = findViewById(R.id.txt_article_number);
        mTxtContent = findViewById(R.id.txt_article_content);
        mImgUrl = findViewById(R.id.img_article_url);

        mRecyclerView = findViewById(R.id.relist_community_comment);

        mImgCollect = findViewById(R.id.img_article_collect);
    }

    @Override
    protected void initData() {
        findBmobData();
    }


    @Override
    protected void initListener() {
        mImgCollect.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    private void findBmobData() {
        showProgress(false, "正在加载...");
        BmobQuery<CommunityRelistBO> query = new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(new FindListener<CommunityRelistBO>() {
            @Override
            public void done(List<CommunityRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(CommunityArticleActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (CommunityRelistBO gameScore : object) {
                        setArticleContent(object);
                    }
                } else {
                    hideProgress();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        BmobQuery<CommunityComRelistBO> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("position", mPosition);
        query2.setLimit(50);
        query2.findObjects(new FindListener<CommunityComRelistBO>() {
            @Override
            public void done(List<CommunityComRelistBO> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(CommunityArticleActivity.this, "查询成功：共" + object.size() + "条数据。");
                    for (CommunityComRelistBO relistBO : object) {
                        setRelistData(object);
                    }
                } else {
                    hideProgress();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    private void setArticleContent(List<CommunityRelistBO> object) {
        Intent intent = getIntent();
        if (intent != null) {
            final int position = Integer.valueOf(intent.getStringExtra("key"));
            mPosition = position;
            for (int i = 0; i <= position; i++) {
                //获得playerName的信息
                mTxtTittle.setText(object.get(position).tittle);
                mTxtCategory.setText(object.get(position).category);
                mTxtDate.setText(object.get(position).date);
                mTxtNumber.setText("收藏人数："+object.get(position).number);
                mTxtContent.setText(object.get(position).content);
                Glide.with(CommunityArticleActivity.this).load(object.get(position).imgurl).into(mImgUrl);
                mObjectId = object.get(position).getObjectId();
                isCollect = object.get(position).isCollect();
                setCollectIcon(isCollect);
            }
        }

    }

    private void setRelistData(List<CommunityComRelistBO> object) {
        mList = new ArrayList<>();
        for (int i = 0; i < object.size(); i++) {
            mRelistBO = new CommunityComRelistBO();
            mRelistBO.content = object.get(i).getContent();
            mRelistBO.name = object.get(i).getName();
            mRelistBO.date = object.get(i).getDate();
            mRelistBO.imgurl = object.get(i).getImgurl();
        }
        setRelistAdapter();
        hideProgress();
    }

    private void setRelistAdapter() {
        mAdapter = new CommunityCommentRelistAdapter(R.layout.item_relist_community_comment, mList);
        LinearLayoutManager manager = new LinearLayoutManager(CommunityArticleActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(CommunityArticleActivity.this, MyDividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_article_collect:
                updateBmobCollect();
                break;
        }
    }

    private void updateBmobCollect() {
        showProgress(true, "收藏中，请稍后...");
        if (isCollect == true) {
            isCollect = false;
        } else if (isCollect == false) {
            isCollect = true;
        }
        CommunityRelistBO p2 = new CommunityRelistBO();
        p2.setValue("collect", isCollect);
        p2.update(mObjectId, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (isCollect == true) {
                        ToastUtils.showShort(CommunityArticleActivity.this, "收藏成功");
                    } else if (isCollect == false) {
                        ToastUtils.showShort(CommunityArticleActivity.this, "取消收藏成功");
                    }
                    setCollectIcon(isCollect);
                    hideProgress();
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                    ToastUtils.showShort(CommunityArticleActivity.this, "收藏失败");
                    hideProgress();
                }
            }
        });

    }


    private void setCollectIcon(boolean isCollect) {
        if (isCollect == true) {
            mImgCollect.setImageResource(R.mipmap.ic_collect_full);
        } else if (isCollect == false) {
            mImgCollect.setImageResource(R.mipmap.ic_collect);
        }
    }

}
