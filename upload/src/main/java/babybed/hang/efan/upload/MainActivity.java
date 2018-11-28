package babybed.hang.efan.upload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import babybed.hang.efan.upload.bean.CommunityRelistBO;
import babybed.hang.efan.upload.bean.MusicRegridBO;
import babybed.hang.efan.upload.bean.StoreLikeRelistBO;
import babybed.hang.efan.upload.bean.StoreRecRegridBO;
import babybed.hang.efan.upload.bean.StoreTrolleyRelistBO;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
    private Button mButton1, mButton2, mButton3, mButton4, mButton5, mButton6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "86c0aa1bc8667f1c3088eb2ee5f49463");
        mButton1 = findViewById(R.id.btn1);
        mButton2 = findViewById(R.id.btn2);
        mButton3 = findViewById(R.id.btn3);
        mButton4 = findViewById(R.id.btn4);
        mButton5 = findViewById(R.id.btn5);
        mButton6 = findViewById(R.id.btn6);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreTrolleyRelistBO storeTrolleyRelistBO = new StoreTrolleyRelistBO();
                storeTrolleyRelistBO.setImgurl("lucky");
                storeTrolleyRelistBO.setName("北京海淀");
                storeTrolleyRelistBO.setPrice("北京海淀");
                storeTrolleyRelistBO.setNumber("北京海淀");
                storeTrolleyRelistBO.setIsselect(true);
                storeTrolleyRelistBO.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            toast("创建数据成功：" + objectId);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreLikeRelistBO storeLikeRelistBO = new StoreLikeRelistBO();
                storeLikeRelistBO.setImgurl("lucky");
                storeLikeRelistBO.setName("北京海淀");
                storeLikeRelistBO.setPrice("北京海淀");
                storeLikeRelistBO.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            toast("创建数据成功：" + objectId);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreRecRegridBO storeRecommendRegridBO = new StoreRecRegridBO();
                storeRecommendRegridBO.setImgurl("lucky");
                storeRecommendRegridBO.setName("北京海淀");
                storeRecommendRegridBO.setPrice("北京海淀");
                storeRecommendRegridBO.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            toast("创建数据成功：" + objectId);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunityRelistBO communityRelistBO = new CommunityRelistBO();
                communityRelistBO.setCategory("健康");
                communityRelistBO.setDate("1920-10-15");
                communityRelistBO.setImgurl("kjslkd");
                communityRelistBO.setNumber("54");
                communityRelistBO.setTittle("孩子发烧");
                communityRelistBO.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            toast("创建数据成功：" + objectId);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicRegridBO musicRegridBO = new MusicRegridBO();
                musicRegridBO.setName("情歌");
                musicRegridBO.setNumber("120万");
                musicRegridBO.setCoverurl("img");
                musicRegridBO.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            toast("创建数据成功：" + objectId);
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        mButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void toast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
