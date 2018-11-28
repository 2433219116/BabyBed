package babybed.hang.efan.babybed.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import babybed.hang.efan.babybed.activity.BedBlueToothActivity;

/**
 * 该broadcastreceiver监听设备和变化的标题时，发现完成
 * Created by Efan on 2018/3/24.
 */

public class BlueToothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ArrayAdapter<String> mNewDevicesArrayAdapter = null;
        String action = intent.getAction();

        // 当发现设备
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //把蓝牙设备对象的意图
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 如果它已经配对，跳过它
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                BedBlueToothActivity.mNewDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
            //当发现后，改变活动名称
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            if (BedBlueToothActivity.mNewDevicesArrayAdapter.getCount() == 0) {
                BedBlueToothActivity.mNewDevicesArrayAdapter.add("没有建立连接");
            }
        }
    }
}
