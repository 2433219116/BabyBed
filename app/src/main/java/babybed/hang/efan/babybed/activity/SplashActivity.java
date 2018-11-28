package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;

public class SplashActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtnSkip, mBtnLogin;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_ready_splash;
    }

    @Override
    protected void setToolBar() {

    }

    @Override
    protected void initView() {
        mBtnLogin = findViewById(R.id.btn_splash_login);
        mBtnSkip = findViewById(R.id.btn_splash_skip);
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void initListener() {
        mBtnSkip.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_splash_login:
                toLoginActivity();
                break;
            case R.id.btn_splash_skip:
                toMainActivity();
                break;
        }
    }

    private void toLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
