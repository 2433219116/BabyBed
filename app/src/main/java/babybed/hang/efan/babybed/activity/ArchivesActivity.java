package babybed.hang.efan.babybed.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.feezu.liuli.timeselector.TimeSelector;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.bean.BabyBean;
import babybed.hang.efan.babybed.dialog.ArchivesNameDialog;
import babybed.hang.efan.babybed.dialog.ArchivesSexDialog;
import babybed.hang.efan.babybed.utils.ToastUtils;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Efan on 2018/3/19.
 */

public class ArchivesActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTxtName, mTxtSex, mTxtAge;
    private ArchivesNameDialog mArchivesNameDialog;
    private ArchivesSexDialog  mArchivesSexDialog;

    private CircleImageView mCimgBaby;
    private String name, age, sex,phone;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_ready_archives;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("宝宝档案");
        setToolbarRightIcon(R.mipmap.ic_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBmobBabyInfo();
            }
        });
    }

    private void saveBmobBabyInfo() {
        if (!name.equals("") && !age.equals("") && !sex.equals("")) {
            showProgress(false, "请稍后...");
            BabyBean babyBean = new BabyBean();
           //注意：不能调用gameScore.setObjectId("")方法
            babyBean.setName(name);
            babyBean.setAge(age);
            babyBean.setSex(sex);
            babyBean.setPhone(phone);
            babyBean.save(new SaveListener<String>() {

                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        ToastUtils.showShort(ArchivesActivity.this, "宝宝档案创建成功！");
                        hideProgress();
                        toMainActivity();
                    } else {
                        hideProgress();
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }


    @Override
    protected void initView() {
        mTxtName = findViewById(R.id.txt_archives_name);
        mTxtAge = findViewById(R.id.txt_archives_age);
        mTxtSex = findViewById(R.id.txt_archives_sex);
        mCimgBaby=findViewById(R.id.cimg_archives_photo);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mTxtName.setOnClickListener(this);
        mTxtSex.setOnClickListener(this);
        mTxtAge.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        name=mTxtName.getText().toString();
        age=mTxtAge.getText().toString();
        sex=mTxtSex.getText().toString();
        //获得SharedPreferences的实例
        SharedPreferences sp = getSharedPreferences("user_name", Context.MODE_PRIVATE);
        //通过key值获取到相应的data，如果没取到，则返回后面的默认值
        phone= sp.getString("name", "???");
    }


    private void toMainActivity() {
        Intent intent = new Intent(ArchivesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_archives_name:
                showNameDialog();
                break;
            case R.id.txt_archives_age:
                showAgeDialog();
                break;
            case R.id.txt_archives_sex:
                showSexDialog();
                break;
        }
    }

    private void showNameDialog() {
        ArchivesNameDialog.Builder builder = new ArchivesNameDialog.Builder(ArchivesActivity.this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                name = String.valueOf(mArchivesNameDialog.getName());
                if (!name.equals(""))
                    mTxtName.setText(name);
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // cancel -> dismiss
                    }
                });
        mArchivesNameDialog = builder.build();
        mArchivesNameDialog.setCancelable(true); // 点击空白是否可以取消
        mArchivesNameDialog.show(getSupportFragmentManager());
    }


    private void showAgeDialog() {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                if (!time.equals("")) {
                    age = time;
                    mTxtAge.setText(time);

                }
            }
        }, "2015-01-01 00:00", "2020-12-31 23:30");
        timeSelector.show();
    }


    private void showSexDialog() {
        final ArchivesSexDialog.Builder builder = new ArchivesSexDialog.Builder(ArchivesActivity.this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sex = mArchivesSexDialog.getSex();
                if (!sex.equals("")) {
                    mTxtSex.setText(sex);
                    if (sex.equals("男孩")) {
                        mCimgBaby.setImageResource(R.mipmap.img_boy);
                    }else if (sex.equals("女孩")){
                        mCimgBaby.setImageResource(R.mipmap.img_girl);
                    }
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mArchivesSexDialog.dismiss();
            }
        });
        mArchivesSexDialog = builder.build();
        mArchivesSexDialog.setCancelable(true);
        mArchivesSexDialog.show(getSupportFragmentManager());
    }
}
