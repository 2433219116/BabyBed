package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.GuideLoginPagerAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.weight.ColorAnimationView;

public class GuideActivity extends BaseActivity {

    private ColorAnimationView colorAnimationView;

    private List<View>             mViewList;
    private GuideLoginPagerAdapter mGuideLoginPagerAdapter;
    private ViewPager              mViewPager;

    private Button mBtnToSplash;

    private InkPageIndicator inkPageIndicator;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_ready_guide;
    }

    @Override
    protected void setToolBar() {

    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewpager_guide);
        colorAnimationView = findViewById(R.id.color_animation_view_guide);
        mBtnToSplash = findViewById(R.id.btn_guide_to_splash);
        inkPageIndicator = findViewById(R.id.inkpage_indicator_guide);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        setViewpagerListener();
        mBtnToSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSplashActivity();
            }
        });
    }

    @Override
    protected void initEvent() {
        setViewPager();
        inkPageIndicator.setViewPager(mViewPager);
    }

    private void setViewPager() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mViewList = new ArrayList<>();
        mViewList.add(inflater.inflate(R.layout.viewpager_guide_first, null));
        mViewList.add(inflater.inflate(R.layout.viewpager_guide_second, null));
        mViewList.add(inflater.inflate(R.layout.viewpager_guide_third, null));
        mGuideLoginPagerAdapter = new GuideLoginPagerAdapter(GuideActivity.this, mViewList);
        mViewPager.setAdapter(mGuideLoginPagerAdapter);
        colorAnimationView.setmViewPager(mViewPager, mViewList.size());
    }

    private void setGuideFirstIn() {
        //先获得可编辑的SharedPreferences，获得编辑者，通过编辑者进行设置
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("mIsFirstIn", false);
        editor.commit();
    }

    private void toSplashActivity() {
        Intent intent = new Intent(GuideActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    private void setViewpagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //TODO addd动画
                if (position == 2) {
                    mBtnToSplash.setVisibility(View.VISIBLE);
                } else
                    mBtnToSplash.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
