package babybed.hang.efan.babybed.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.adapter.recycler.DrawerLayoutFooterRegridAdapter;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.fragment.BedFragment;
import babybed.hang.efan.babybed.fragment.CommunityFragment;
import babybed.hang.efan.babybed.fragment.FragmentManagerWrapper;
import babybed.hang.efan.babybed.fragment.GrowFragment;
import babybed.hang.efan.babybed.fragment.MineFragment;
import babybed.hang.efan.babybed.fragment.MusicFragment;
import babybed.hang.efan.babybed.fragment.StoreFragment;
import babybed.hang.efan.babybed.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private DrawerLayout mDrawerLayout;

    private RecyclerView mRecyclerView;
    private int[] simg = {R.mipmap.ic_bed, R.mipmap.ic_music, R.mipmap.ic_grow, R.mipmap.ic_community, R.mipmap.ic_store, R.mipmap.ic_setting};

    private Toolbar               mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private CircleImageView       mCimgPortrait;
    private String[] stxt      = {"婴儿床", "音乐库", "成长足迹", "育儿社区", "宝贝商店", "我的宝贝"};
    private int[]    simgRight = {R.mipmap.ic_bed_conection_state, R.mipmap.ic_music_right, R.mipmap.ic_grow_right, R.mipmap.ic_community_right, R.mipmap.ic_store_right, R.mipmap.ic_mine_right};

    public static final int    PHOTOZOOM         = 2; // 缩放
    public static final int    PHOTORESOULT      = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public String picPath;

    private FragmentManager mFragmentManager;
    private Fragment        mFragment;
    private List<Integer>   mImgList;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            setFragment();
        }
    }

    @Override
    protected void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.regrid_drawerlayout_footer);
        mCimgPortrait = findViewById(R.id.cimg_portrait);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mCimgPortrait.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        setToolbarRightListener(0);
        setRelistDrawerLayout();
        setToolBarDrawerToggle();
        setDrawerLayoutListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cimg_portrait:
                toMediaImage();
                break;
        }
    }

    private void toMediaImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        //打开文件
        startActivityForResult(intent, PHOTOZOOM);

    }

    private void setDrawerLayoutListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * @param drawerView  侧滑的view
             * @param slideOffset 滑动距离
             */
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //注销与全部取消则是缩放抽屉效果
                View mContent = mDrawerLayout.getChildAt(0);
                //drawerlayout的getChild（0）方法获取到内容view
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.6f + scale * 0.4f;
                float leftScrale = 0.5f + slideOffset * 0.5f;
                mMenu.setAlpha(leftScrale);
                mContent.setPivotX(0);// 水平方向偏转量
                mContent.setPivotY(mContent.getHeight() / 2);
                mContent.setTranslationX(mMenu.getWidth() * slideOffset);//平移一个View
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    private void setFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragment = FragmentManagerWrapper.getInstance().createFragment(BedFragment.class);
        mFragmentManager.beginTransaction().add(R.id.fl_main_content, mFragment).commit();
    }

    private void setRelistDrawerLayout() {

        mImgList = new ArrayList<>();
        for (int i = 0; i < simg.length; i++) {
            mImgList.add(simg[i]);
        }
        DrawerLayoutFooterRegridAdapter mAdapter = new DrawerLayoutFooterRegridAdapter(mImgList);
        mAdapter.setOnItemClickListener(new DrawerLayoutFooterRegridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toFragment(position);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1, OrientationHelper.VERTICAL, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    protected void setToolBarDrawerToggle() {
        //ActionBarDrawerToggle是一个开关，用于打开/关闭DrawerLayout抽屉
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        //ActionBarDrawerToggle与DrawerLayout的状态同步
        mActionBarDrawerToggle.syncState();
        //监听ActionBarDrawerToggle
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    private void toFragment(final int position) {
        switch (position) {
            case 0:
                setToolbarRightListener(position);
                switchFragment(BedFragment.class);
                break;
            case 1:
                setToolbarRightListener(position);
                switchFragment(MusicFragment.class);
                break;
            case 2:
                setToolbarRightListener(position);
                switchFragment(GrowFragment.class);
                break;
            case 3:
                setToolbarRightListener(position);
                switchFragment(CommunityFragment.class);
                break;
            case 4:
                setToolbarRightListener(position);
                switchFragment(StoreFragment.class);
                break;
            case 5:
                setToolbarRightListener(position);
                switchFragment(MineFragment.class);
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setToolbarRightListener(final int position) {
        initToolbarContent();
        setToolbarTitle(stxt[position]);
        setToolbarRightIcon(simgRight[position]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        toBlueToothActivity();
                        break;
                    case 1:
                        toMusicLocationActivity();
                        break;
                    case 2:
                        ToastUtils.showShort(MainActivity.this, position);
                        break;
                    case 3:
                        ToastUtils.showShort(MainActivity.this, position);
                        break;
                    case 4:
                        toStoreShellActivity();
                        break;
                    case 5:
                        Snackbar.make(view, "商品已经添加到您的收藏中", Snackbar.LENGTH_LONG)
                                .setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ToastUtils.show(MainActivity.this, "撤销成功", 1000);
                                    }
                                }).show();
                        break;
                    case 6:
                        ToastUtils.showShort(MainActivity.this, position);
                        break;
                }
            }
        });
    }

    private void toStoreShellActivity() {
        Intent intent = new Intent(MainActivity.this, StoreShellActivity.class);
        startActivity(intent);
    }

    private void toMusicLocationActivity() {
        Intent intent = new Intent(MainActivity.this, MusicLocationActivity.class);
        startActivity(intent);
    }

    private void toBlueToothActivity() {
//        Intent serverIntent = new Intent(TestActivity.this, BedBlueToothActivity.class);
//        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    private void switchFragment(Class<?> clazz) {
        Fragment fragment = FragmentManagerWrapper.getInstance().createFragment(clazz);
        //判断是不是加上来的，是的话隐藏之前的，展示创建的
        if (fragment.isAdded()) {
            mFragmentManager.beginTransaction().hide(mFragment).show(fragment).commitAllowingStateLoss();
        } else {
            //全部提交
            mFragmentManager.beginTransaction().hide(mFragment).add(R.id.fl_main_content, fragment).commitAllowingStateLoss();
        }
        mFragment = fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
            Uri uri = data.getData();

            String[] pojo = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(uri, pojo, null, null, null);
            if (cursor != null) {
                int index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(index);
                if (path != null) {
                    picPath = path;
                }
            }
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
//                icon.setImageBitmap(photo);
            }
            if (picPath == null) {

                Toast.makeText(MainActivity.this, "未修改图片！", Toast.LENGTH_SHORT)
                        .show();
            } else {
                final File file = new File(picPath);
                Log.i("picPath", "******" + picPath);
                if (file != null) {
//                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(file);
//                    mCimgPortrait.setImageBitmap(bitmap);
                    mCimgPortrait.setImageURI(Uri.fromFile(file));
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }
}

