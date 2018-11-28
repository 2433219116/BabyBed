package babybed.hang.efan.babybed.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.GuideLoginPagerAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.MyUser;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Efan on 2018/3/13.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager              mViewPager;
    private GuideLoginPagerAdapter mAdapter;
    private List<View>             mViewList;

    private ImageView mImgSwitch;
    private int POSITION = 1;

    private EditText mEdtLoginFastPhone, mEdtLoginFastMessage, mEdtLoginPhone, mEdtLoginPassword;
    private Button mBtnLoginFastSend, mBtnLoginFast, mBtnLogin;

    boolean isWork = false;
    int     i      = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                i = i - 1;
                if (i <= 60 && i > 0)
                    mBtnLoginFastSend.setText(i + "");
                if (i == 0) {
                    setTimeStop();
                    mBtnLoginFastSend.setText("重新发送");
                    mBtnLoginFastSend.setClickable(true);
                }
            }
        }
    };


    @Override
    public int getLayoutRes() {
        return R.layout.activity_ready_login;
    }

    @Override
    protected void setToolBar() {

    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewpager_login);

        mImgSwitch = findViewById(R.id.img_login_switch_viewpager);

        initViewpagerChildView();

    }


    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        mBtnLoginFast.setOnClickListener(this);
        mBtnLoginFastSend.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        mImgSwitch.setOnClickListener(this);
        setViewPagerListener();
    }

    @Override
    protected void initEvent() {
        setViewpagerAdapter();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login_switch_viewpager:
                mViewPager.setCurrentItem(POSITION);
                break;
            case R.id.btn_login_fast_send:
                sendMessage();
                break;
            case R.id.btn_login:
                isLogin();
                break;
            case R.id.btn_login_fast:
                isLoginFast();
                break;
        }
    }


    private void initViewpagerChildView() {
        View view = LayoutInflater.from(this).inflate(R.layout.viewpager_login_first, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.viewpager_login_second, null);
        mViewList = new ArrayList<>();
        mViewList.add(view);
        mViewList.add(view2);

        mEdtLoginFastPhone = view.findViewById(R.id.edt_login_fast_phone);
        mEdtLoginFastMessage = view.findViewById(R.id.edt_login_fast_message);
        mBtnLoginFastSend = view.findViewById(R.id.btn_login_fast_send);
        mBtnLoginFast = view.findViewById(R.id.btn_login_fast);

        mEdtLoginPhone = view2.findViewById(R.id.edt_login_phone);
        mEdtLoginPassword = view2.findViewById(R.id.edt_login_password);
        mBtnLogin = view2.findViewById(R.id.btn_login);
    }

    private void setViewpagerAdapter() {
        mAdapter = new GuideLoginPagerAdapter(LoginActivity.this, mViewList);
        mViewPager.setAdapter(mAdapter);
    }

    private void setViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                POSITION = 1 - position;
                switch (position) {
                    case 0:
                        mImgSwitch.setImageResource(R.mipmap.ic_login_right);
                        break;
                    case 1:
                        mImgSwitch.setImageResource(R.mipmap.ic_login_left);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void sendMessage() {
        String phone = mEdtLoginFastPhone.getText().toString().trim();
        BmobSMS.requestSMSCode(phone, "message", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    Log.i("smile", "短信id：" + smsId);//用于后续的查询本次短信发送状态
                    ToastUtils.showShort(LoginActivity.this, "验证码发送成功，请注意接收");
                    mBtnLoginFastSend.setClickable(false);
                    setTimeStop();
                    //启动线程
                    new Thread(new MyThread()).start();
                }
            }
        });
    }


    private void toArchivesActivity() {
        Intent intent = new Intent(LoginActivity.this, ArchivesActivity.class);
        startActivity(intent);
        finish();
    }

    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void isLoginFast() {

        final String phonefast = mEdtLoginFastPhone.getText().toString().trim();
        int length = mEdtLoginFastPhone.getText().toString().length();
        final String number = mEdtLoginFastMessage.getText().toString().trim();
        if (phonefast.equals("") || number.equals("")) {
            ToastUtils.showShort(LoginActivity.this, "请填写有效信息");
            mEdtLoginFastPhone.setText("");
            mEdtLoginFastMessage.setText("");
        } else if (length == 11) {
            showProgress(false, "请稍后...");
            BmobUser.signOrLoginByMobilePhone(phonefast, number, new LogInListener<MyUser>() {

                @Override
                public void done(MyUser user, BmobException e) {
                    if (user != null) {
                        hideProgress();
                        Log.i("smile", "用户登陆成功");
                        ToastUtils.showShort(LoginActivity.this, "用户登录成功");
                        toArchivesActivity();
                        setSharedPreferences(phonefast);
                    } else {
                        hideProgress();
                        Log.i("smile", phonefast + number);
                        ToastUtils.showShort(LoginActivity.this, "验证码错误");
                        mEdtLoginFastMessage.setTextColor(getResources().getColor(R.color.colorRed));
                    }
                }
            });
        } else {
            ToastUtils.showShort(LoginActivity.this, "请输入正确电话号码");
        }
    }

    private void setSharedPreferences(String phone) {
        //获得SharedPreferences的实例 sp_name是文件名
        SharedPreferences sp = getSharedPreferences("user_name", Context.MODE_PRIVATE);
        //获得Editor 实例
        SharedPreferences.Editor editor = sp.edit();
        //以key-value形式保存数据
        editor.putString("name", phone);
        //apply()是异步写入数据
        editor.apply();
        //commit()是同步写入数据
        //editor.commit();
    }

    private void isLogin() {
        final String phone = mEdtLoginPhone.getText().toString().trim();
        String password = mEdtLoginPassword.getText().toString().trim();
        if (phone.equals("") || password.equals("")) {
            ToastUtils.showShort(LoginActivity.this, "请填写有效信息");
            mEdtLoginPassword.setText("");
            mEdtLoginPhone.setText("");
        } else {
            showProgress(false, "请稍后...");
            BmobUser.loginByAccount(phone, password, new LogInListener<MyUser>() {

                @Override
                public void done(MyUser user, BmobException e) {
                    if (user != null) {
                        hideProgress();
                        Log.i("smile", "用户登陆成功");
                        ToastUtils.showShort(LoginActivity.this, "用户登录成功");
                        setSharedPreferences(phone);
                        toArchivesActivity();
                    } else {
                        hideProgress();
                        ToastUtils.showShort(LoginActivity.this, "账号密码错误");
                        mEdtLoginPassword.setText("");
                        mEdtLoginPhone.setText("");
                    }
                }
            });

        }
    }

    public class MyThread implements Runnable {
        public void run() {
            while (isWork) {
                try {
                    Message message = new Message();
                    message.what = 1;
                    Thread.sleep(1000);   //间隔时间在这里定
                    //通过handler把这个线程的线程体放到主线程的消息队列中，使得该线程在主线程中执行
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setTimeStop() {
        if (isWork == true) {
            isWork = false;
        } else {
            isWork = true;
        }
    }
}
