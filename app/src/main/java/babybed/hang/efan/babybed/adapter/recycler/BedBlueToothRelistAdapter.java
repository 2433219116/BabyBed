package babybed.hang.efan.babybed.adapter.recycler;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import babybed.hang.efan.babybed.R;

/**
 * Created by Efan on 2018/3/17.
 */

public class BedBlueToothRelistAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    public BedBlueToothRelistAdapter(int layoutResId, @Nullable List<BluetoothDevice> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice device) {
        helper.setText(R.id.txt_bed_bluetooth_name, device.getName());
        helper.setText(R.id.txt_bed_bluetooth_address, "设备地址：" + device.getAddress());
        helper.setText(R.id.txt_bed_bluetooth_status, "匹配状态：" + getStatus(device.getBondState()));
    }

    private String getStatus(int status) {
        switch (status) {
            case BluetoothDevice.BOND_NONE:
                return "未匹配";
            case BluetoothDevice.BOND_BONDING:
                return "匹配中";
            case BluetoothDevice.BOND_BONDED:
                return "已匹配";
            default:
                return "未匹配";
        }
    }


}
