package babybed.hang.efan.babybed.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import babybed.hang.efan.babybed.R;
import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2016/7/8 0008.
 */

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public abstract class BaseActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    Toolbar        mToolbar;
    ImageView      mImgLeft, mImgRight;
    TextView mTxtTitle;

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    protected abstract void setToolBar();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void initEvent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        Bmob.initialize(this, "86c0aa1bc8667f1c3088eb2ee5f49463");
        setToolBar();
        initView();
        initData();
        initListener();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog == null)
            return;
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 设置支持toolbar方法
     * 可选可不许选
     */
    protected void setSupportToolbar() {
        initToolbarId();
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }
    }


    protected void initToolbarId() {
        mToolbar = findViewById(R.id.toolbar);
        mTxtTitle = findViewById(R.id.txt_toolbar_title);
        mImgLeft = findViewById(R.id.img_toolbar_left);
        mImgRight = findViewById(R.id.img_toolbar_right);
    }


    protected void initToolbarContent() {
        mToolbar.setTitle("");
        removeToolbarTittle();
        removeToolbarLeftIcon();
        removeToolbarRightIcon();
    }


    /**
     * toolbar 添加tittle
     *
     * @param text
     */
    protected void setToolbarTitle(String text) {
        if (mToolbar != null) {
            if (mTxtTitle != null) {
                if (mTxtTitle.getVisibility() == View.GONE) {
                    mTxtTitle.setVisibility(View.VISIBLE);
                    mTxtTitle.setText(text);
                }
            }
        }
    }

    /**
     * toolbar 添加左面icon
     *
     * @param resId
     */
    protected ImageView setToolbarLeftIcon(int resId) {
        if (mToolbar != null) {
            if (mImgLeft != null) {
                if (mImgLeft.getVisibility() == View.GONE) {
                    mImgLeft.setVisibility(View.VISIBLE);
                    mImgLeft.setImageResource(resId);
                    return mImgLeft;
                }
            }
        }
        return null;
    }

    /**
     * toolbar 添加右面icon
     *
     * @param resId
     */
    protected ImageView setToolbarRightIcon(int resId) {
        if (mToolbar != null) {
            if (mImgRight != null) {
                if (mImgRight.getVisibility() == View.GONE) {
                    mImgRight.setVisibility(View.VISIBLE);
                    mImgRight.setImageResource(resId);
                    return mImgRight;
                }
            }
        }
        return null;
    }

    /**
     * toolbar 移除tittle
     */
    protected void removeToolbarTittle() {
        if (mToolbar != null) {
            if (mTxtTitle != null) {
                if (mTxtTitle.getVisibility() == View.VISIBLE) {
                    mTxtTitle.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * toolbar 移除左面icon
     */
    protected void removeToolbarLeftIcon() {
        if (mToolbar != null) {
            if (mImgLeft != null) {
                if (mImgLeft.getVisibility() == View.VISIBLE) {
                    mImgLeft.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * toolbar 移除右面icon
     */
    protected void removeToolbarRightIcon() {
        if (mToolbar != null) {
            if (mImgRight != null) {
                if (mImgRight.getVisibility() == View.VISIBLE) {
                    mImgRight.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * 检查是否拥有权限
     *
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission(Activity thisActivity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {

        }
    }
    /**
     * 退出方法
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getApplication());
            localBuilder.setIcon(R.drawable.ic_launcher_background).setTitle("友情提示...")
                    .setMessage("你确定要退出吗？");
            localBuilder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            finish();
                        }
                    });
            localBuilder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            paramDialogInterface.cancel();
                        }
                    }).create();
            localBuilder.show();

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }
        return true;
    }

}
