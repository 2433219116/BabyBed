package babybed.hang.efan.babybed.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import java.util.Set;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.base.BaseActivity;
import babybed.hang.efan.babybed.receiver.BlueToothReceiver;
import babybed.hang.efan.babybed.utils.ToastUtils;

/**
 * Created by Efan on 2018/3/19.
 */


/**
 * 这项活动似乎是一个对话框。它列出的任何配对的设备和装置
 * 在现场发现后的发现。当一个设备是由用户选择，
 * 地址的设备发送给家长活动的
 * 结果意图。
 */
public class BedBlueToothActivity extends BaseActivity implements View.OnClickListener {
    // Debugging
    private static final String  TAG = "DeviceListActivity";
    private static final boolean D   = true;


    // 返回别的意图
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // 适配器

    //    private RecyclerView               mRecyclerView;
    //    private ListView                   pairedListView;
    //    private BedBlueToothRelistAdapter  mAdapter;
    //    private List<BedBlueToothRelistBO> mList;
    //    private BedBlueToothRelistBO       mRelistBO;

    public static SwitchButton mSwitchButton;
    private       LinearLayout mLlSearch;
    private       ImageView    mImgSearch;

    private BluetoothAdapter mBtAdapter = null;

    private       ListView             pairedListView;
    public        ArrayAdapter<String> mPairedDevicesArrayAdapter;
    public static ArrayAdapter<String> mNewDevicesArrayAdapter;

    private BlueToothReceiver mBlueToothReceiver;

//    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_bed_bluetooth;
    }

    @Override
    protected void setToolBar() {
        setSupportToolbar();
        initToolbarContent();
        setToolbarTitle("蓝牙设置");
        setToolbarLeftIcon(R.mipmap.ic_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
//        mRecyclerView = findViewById(R.id.relist_bed_bluetooth);
        mSwitchButton = findViewById(R.id.switch_button_bluetooth);
        mLlSearch = findViewById(R.id.ll_bluetooth_search);
        mImgSearch = findViewById(R.id.img_bluetooth_search);
        // 结果取消如果用户备份
        setResult(Activity.RESULT_CANCELED);

        // 初始化数组适配器。一个已配对装置和一个新发现的设备
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_bed_bluetooth);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_bed_bluetooth);

        //寻找和建立配对设备列表
        pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // 寻找和建立为新发现的设备列表
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // 获取本地蓝牙适配器
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        setSwitchButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册时发送广播给设备
        mBlueToothReceiver = new BlueToothReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mBlueToothReceiver, filter);

        // 广播时发现已完成注册
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mBlueToothReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 确保我们没有发现了
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
            mImgSearch.clearAnimation();
        }

        // 注销广播听众
        this.unregisterReceiver(mBlueToothReceiver);
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void initListener() {
        mLlSearch.setOnClickListener(this);
        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                openOrCloseBlueTooth(isChecked);
            }
        });
    }

    @Override
    protected void initEvent() {
        setBondedDevices();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bluetooth_search:
                setDiscovery();
                setImageAnimation();
                break;
        }
    }

    /**
     * 判断当前蓝牙状态，改变switchbutton状态
     */
    private void setSwitchButtonState() {
        if (mBtAdapter.getState() == BluetoothAdapter.STATE_OFF) {
            mSwitchButton.setChecked(false);
        } else if (mBtAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mSwitchButton.setChecked(true);
        }
    }

    /**
     * 打开关闭蓝牙
     *
     * @param isChecked
     */
    private void openOrCloseBlueTooth(boolean isChecked) {
        if (mBtAdapter == null) {
            ToastUtils.showShort(BedBlueToothActivity.this, "当前设备不存在蓝牙模块");
        } else {
            if (mBtAdapter.isEnabled()) {
                if (isChecked == false) {
                    mBtAdapter.disable(); // 关闭蓝牙
                    pairedListView.setVisibility(View.GONE);
                }
            } else {
                if (isChecked == true) {
                    // 询问用户，如果用户同意就打开蓝牙
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // 300就表示300秒
                    startActivity(intent);
                    setBondedDevices();
                    pairedListView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 得到手机已经配对的设备
     */
    private void setBondedDevices() {
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            if (pairedListView.getChildCount() < 1) {
                String noDevices = getResources().getText(R.string.none_paired)
                        .toString();
                mPairedDevicesArrayAdapter.add(noDevices);
            }
        }
    }

    /**
     * 发现与蓝牙装置
     */

    private void setDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");
        // 显示扫描的称号

        // 打开新设备的字幕
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // 如果我们已经发现，阻止它
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
            mImgSearch.clearAnimation();
        }

        // 要求从bluetoothadapter发现
        mBtAdapter.startDiscovery();
    }

    private void setImageAnimation() {
        //动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_circle_anim);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        mImgSearch.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取最新的list数据
//                setRefreshData();
                //通知界面显示，
//                adapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                mImgSearch.clearAnimation();
            }
        }, 14000);
    }

    /**
     * item点击事件
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // 因为它是浪费的，取消发现我们的连接
            mBtAdapter.cancelDiscovery();
            mImgSearch.clearAnimation();
            // 获得设备地址，这是近17字的
            //视图
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //创建结果意图和包括地址
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            //结果，完成这项活动
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}