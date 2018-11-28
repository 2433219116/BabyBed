package babybed.hang.efan.babybed.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.album.Album;

import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.activity.GrowHighWeightActivity;
import babybed.hang.efan.babybed.base.BaseFragment;
import babybed.hang.efan.babybed.bean.BabyBean;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Efan on 2018/3/13.
 */

public class GrowFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Button mButton;
    public static int ACTIVITY_REQUEST_SELECT_PHOTO = 100;

    private SwipeRefreshLayout swipeRefreshLayout;
    private CircleImageView mCimgPortrait;
    private TextView mTxtName, mTxtDate;
    private ImageView mImgSex;

    private String sex, name, date;

    private TextView mTxtEvent, mTxtBmi, mTxtVaccine;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mTxtDate.setText(date);
                    mTxtName.setText(name);
                    if (sex.equals("男")) {
                        mCimgPortrait.setImageResource(R.mipmap.img_boy);
                        mImgSex.setImageResource(R.mipmap.ic_man);
                    } else if (sex.equals("女")) {
                        mCimgPortrait.setImageResource(R.mipmap.img_girl);
                        mImgSex.setImageResource(R.mipmap.ic_woman);
                    }
                    // 通知ListView刷新数据完毕,让ListView停止刷新
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_grow;
    }

    @Override
    protected void initView() {
        mButton = bindViewId(R.id.btn_grow_uploading);

        swipeRefreshLayout = bindViewId(R.id.swipe_refresh_grow);
        mCimgPortrait = bindViewId(R.id.cimg_grow_baby);
        mTxtName = bindViewId(R.id.txt_grow_name);
        mTxtDate = bindViewId(R.id.txt_grow_date);
        mImgSex = bindViewId(R.id.img_grow_sex);

        mTxtEvent = bindViewId(R.id.txt_grow_event);
        mTxtBmi = bindViewId(R.id.txt_grow_bmi);
        mTxtVaccine = bindViewId(R.id.txt_grow_vaccine);
    }


    @Override
    protected void initData() {
        findBmobData();
    }

    @Override
    protected void initListener() {
        mButton.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        mTxtEvent.setOnClickListener(this);
        mTxtBmi.setOnClickListener(this);
        mTxtVaccine.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        setRefresh();
    }

    private void findBmobData() {
        BmobQuery<BabyBean> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", "18524513595");
        query.setLimit(1);
        query.findObjects(new FindListener<BabyBean>() {
            @Override
            public void done(List<BabyBean> object, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(getActivity(), "查询成功：共" + object.size() + "条数据。");
                    for (BabyBean gameScore : object) {
                        sex = gameScore.getSex();
                        date = gameScore.getAge();
                        name = gameScore.getName();
                    }
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
//                List<String> pathList = Album.parseResult(data);
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_grow_uploading:
                uploadPhoto();
                break;
            case R.id.txt_grow_event:
                toGrowHighWeight();
                break;
            case R.id.txt_grow_vaccine:
                toGrowHighWeight();
                break;
            case R.id.txt_grow_bmi:
                toGrowHighWeight();
                break;
        }
    }

    private void uploadPhoto() {
        Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO
                , 9                                                         // 指定选择数量。
                , ContextCompat.getColor(getActivity(), R.color.colorViolet)        // 指定Toolbar的颜色。
                , ContextCompat.getColor(getActivity(), R.color.colorViolet));  // 指定状态栏的颜色。
    }

    private void toGrowHighWeight() {
        Intent intent = new Intent(getActivity(), GrowHighWeightActivity.class);
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
                //获取最新的list数据
                findBmobData();
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }, 1500);
    }
}
