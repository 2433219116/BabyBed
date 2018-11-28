package babybed.hang.efan.babybed.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

import babybed.hang.efan.babybed.R;
import babybed.hang.efan.babybed.activity.BedBlueToothActivity;
import babybed.hang.efan.babybed.activity.MusicLocationActivity;
import babybed.hang.efan.babybed.base.BaseFragment;
import babybed.hang.efan.babybed.receiver.AlarmVibratorReceiver;
import babybed.hang.efan.babybed.service.BluetoothChatService;
import babybed.hang.efan.babybed.utils.SoundPoolUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Efan on 2018/3/13.
 */

public class BedFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AlarmVibratorReceiver.BRInteraction {
    private boolean isConnection = false;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwitchButton mSwitchButtonShaky, mSwitchButtonMusic, mSwitchButtonWet, mSwitchButtonTemperature, mSwitchButtonHeart, mSwitchButtonDangerous;

    private static MediaPlayer mMediaPlayer;
    private static Vibrator mVibrator;

    private TextView mTxtWet, mTxtTemperature, mTxtHeart;

    private ImageView mImgLeft, mImgRight;
    private CircleImageView mCimgMusic;
    private boolean isBegin = true;
    private int cover[] = {R.mipmap.img_music_one, R.mipmap.img_music_two, R.mipmap.img_music_three, R.mipmap.img_music_four, R.mipmap.img_music_five, R.mipmap.img_music_six, R.mipmap.img_music_two, R.mipmap.img_music_two, R.mipmap.img_music_seven};
    int covers = 1;

    private NotificationManager manager;
    private RemoteViews remoteViews;

    AlarmVibratorReceiver alarmVibratorReceiver = new AlarmVibratorReceiver();

    // 调试
    private static final String TAG = "BluetoothChat";
    private static boolean D = true;
    private static final String info = "junge";
    // 类型的消息发送从bluetoothchatservice处理程序
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String BluetoothData = "fullscreen";
    public String filename = ""; // 用来保存存储的文件名
    private String newCode = "";
    private String newCode2 = "";
    private String fmsg = ""; // 保存用数据缓存
    // 键名字从收到的bluetoothchatservice处理程序
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    // 独特的是这个应用程序

    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Intent需要 编码
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // 布局控件
    private TextView mTitle;
    private EditText mInputEditText;
    private EditText mOutEditText;
    private EditText mOutEditText2;
    private Button mSendButton;
    private Button breakButton;

    // 名字的连接装置
    private String mConnectedDeviceName = null;
    // 传出消息的字符串缓冲区
    private StringBuffer mOutStringBuffer;
    // 当地的蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    // 成员对象的聊天服务
    private BluetoothChatService mChatService = null;
    // 设置标识符，选择用户接受的数据格式
    private boolean dialogs;

    //第一次输入加入-->变量
    private int sum = 1;
    private int UTF = 1;

    // 名社民党记录当创建服务器套接字
    String mmsg = "";
    String mmsg2 = "";

    private int[] heart = {70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80};

    // 处理程序，获取信息的bluetoothchatservice回来
    private final Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
//                            mInputEditText.setText("");
                            isConnection = true;
                            setFailOrSuccess(isConnection);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            isConnection = false;
                            setFailOrSuccess(isConnection);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // 构建一个字符串缓冲区
                    String writeMessage = new String(writeBuf);
                    sum = 1;
                    UTF = 1;
                    mmsg += writeMessage;

//                    mInputEditText.getText().append("\n<--" + writeMessage + "\n");
                    fmsg += "\n<--" + writeMessage + "\n";

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    String[] str = readMessage.split("/");
                    if (str.length > 2) { //确保接收的数据正确
                        String a = str[1].toString();
                        String b = str[2].toString();
                        if (a.length() == 6 && b.length() != 1 && !a.substring(6).equals("%")) {
                            //  Toast.makeText(getContext(), a + b, Toast.LENGTH_SHORT).show();
                            mTxtTemperature.setText(a);
                            mTxtWet.setText(b);
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存该连接装置的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getActivity(),
                            "已连接 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getActivity(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 99:
                    Random ra = new Random();
                    int a = (ra.nextInt(10) + 1);
                    mTxtHeart.setText(heart[a] + "次/分");
                    break;

            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bed;
    }

    @Override
    protected void initView() {
        mSwitchButtonShaky = bindViewId(R.id.switch_bed_shaky);
        mSwitchButtonMusic = bindViewId(R.id.switch_bed_music);
        mSwitchButtonWet = bindViewId(R.id.switch_bed_wet);
        mSwitchButtonTemperature = bindViewId(R.id.switch_bed_temperature);
        mSwitchButtonHeart = bindViewId(R.id.switch_bed_heart);
        mSwitchButtonDangerous = bindViewId(R.id.switch_bed_dangerous);


        mTxtWet = bindViewId(R.id.txt_bed_wet);
        mTxtTemperature = bindViewId(R.id.txt_bed_temperature);
        mTxtHeart = bindViewId(R.id.txt_bed_heart);

        mImgLeft = bindViewId(R.id.img_bed_music_left);
        mImgRight = bindViewId(R.id.img_bed_music_right);
        mCimgMusic = bindViewId(R.id.cimg_bed_music);

        D = false;
        swipeRefreshLayout = bindViewId(R.id.swipe_refresh_bed);
        mTitle = bindViewId(R.id.mTitle);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mInputEditText = bindViewId(R.id.editText1);
//        mInputEditText.setGravity(Gravity.TOP);
//        mInputEditText.setSelection(mInputEditText.getText().length(),
//                mInputEditText.getText().length());
//        mInputEditText.clearFocus();
        breakButton = bindViewId(R.id.txt_break);
        breakButton.setOnClickListener(this);

        // 得到当地的蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 初始化Socket
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_LONG)
                    .show();
            setFailOrSuccess(isConnection);
            return;
        }
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        mImgLeft.setOnClickListener(this);
        mImgRight.setOnClickListener(this);
        mCimgMusic.setOnClickListener(this);


        mSwitchButtonShaky.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    onClickSendMessage("A", 1);
                } else if (isChecked == false) {
                    onClickSendMessage("B", 1);
                }
            }
        });
        mSwitchButtonMusic.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    onClickSendMessage("7E04440101EF", 2);
                    mImgLeft.setVisibility(View.VISIBLE);
                    mCimgMusic.setImageResource(cover[0]);
                    mImgRight.setVisibility(View.VISIBLE);
                    setImageAnimation();
                } else if (isChecked == false) {
                    onClickSendMessage("7E0202EF", 2);
                    mImgLeft.setVisibility(View.GONE);
                    mCimgMusic.setImageResource(R.mipmap.ic_bed_music);
                    mImgRight.setVisibility(View.GONE);
                    mCimgMusic.clearAnimation();
                    mCimgMusic.setBorderWidth(0);
                }
            }
        });
        mSwitchButtonWet.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {

                } else if (isChecked == false) {
                    mTxtHeart.setText("--");
                }
            }
        });
        mSwitchButtonTemperature.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {

                } else if (isChecked == false) {
                    mTxtHeart.setText("--");
                }
            }
        });
        mSwitchButtonHeart.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 99;
                            mHandler.sendMessage(message);
                        }
                    }, 3000);
                } else if (isChecked == false) {
                    mTxtHeart.setText("--");
                }
            }
        });
        mSwitchButtonDangerous.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                            int ringerMode = audioManager.getRingerMode();
                            SoundPoolUtils.init(getActivity());
                            handleRingMode(ringerMode);
                        }
                    }, 5000);

                } else if
                        (isChecked == false) {
                    stopAlarm();
                    stopVibrate();
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        setRefresh();
    }

    /**
     * 判断是否启用
     */
    @Override
    public void onStart() {
        super.onStart();
        // 如果是没有，要求它启用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // 否则，启用聊天会话
        } else {
            if (mChatService == null)
                setupChat();
        }
    }


    @Override
    public synchronized void onResume() {
        super.onResume();
        //注册广播接收者
        ringerModeListening();
        if (D)
            Log.e(TAG, "+ ON RESUME +");

        // 执行此检查onresume()涵盖的案件中，英国电信
        // 不可在onstart()，所以我们停下来让它…
        // onresume()将被调用时，action_request_enable活动返回。
        if (mChatService != null) {
            // 只有国家是state_none，我们知道，我们还没有开始
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 启动蓝牙聊天服务
                mChatService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D)

            Log.e(TAG, "- ON PAUSE -");
        getActivity().unregisterReceiver(alarmVibratorReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 蓝牙聊天服务站
        if (mChatService != null)
            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_break:
                onConnectButtonClicked();
                break;
            case R.id.cimg_bed_music:
                toMusicLocation();
                break;
            case R.id.img_bed_music_left:
                if (isBegin) {
                    mCimgMusic.clearAnimation();
                    sendMessage("7E0202EF", 2);
                    isBegin = false;
                } else {
                    setImageAnimation();
                    sendMessage("7E0201EF", 2);
                    isBegin = true;
                }
                break;
            case R.id.img_bed_music_right:
                if (covers < 9) {
                    mCimgMusic.setImageResource(cover[covers]);
                    covers = covers + 1;
                    sendMessage("7E0203EF", 2);
                } else {
                    covers = 1;
                }
                break;
        }
    }

    private void setImageAnimation() {

        //动画
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.cimg_circle_anim);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        mCimgMusic.startAnimation(animation);
        mCimgMusic.setBorderWidth(3);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取最新的list数据
//                setRefreshData();
                //通知界面显示，
//                adapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                mCimgMusic.clearAnimation();
            }
        }, 200000);
    }

    /**
     * 下拉滑动刷新的接口实现
     */
    @Override
    public void onRefresh() {
        //因为本例中没有从网络获取数据，因此这里使用Handler演示4秒延迟来从服务器获取数据的延迟现象，以便于大家
        // 能够看到listView正在刷新的状态。大家在现实使用时只需要使用run（）{}方法中的代码就行了。
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 99;
                mHandler.sendMessage(message);
                //获取最新的list数据
                // setRefreshData();
                //通知界面显示，
                // adapter.notifyDataSetChanged();
                // 通知listview刷新数据完毕,让listview停止刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    /**
     * BroadcastReceiver接口 传递情景状态的改变
     *
     * @param content
     */

    @Override
    public void setAudioManagerState(AudioManager content) {
        handleRingMode(content.getRingerMode());
    }

    /**
     * 监听情景切换（注册情景模式变化广播接收者）
     */
    private void ringerModeListening() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        getActivity().registerReceiver(alarmVibratorReceiver, intentFilter);
    }

    /**
     * 获取系统默认铃声的Uri
     */

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_RINGTONE);
    }

    /**
     * 播放系统声音
     */
    public void startAlarm() {
        //有的手机会创建失败，从而导致mMediaPlayer为空。
        mMediaPlayer = MediaPlayer.create(getActivity(), getSystemDefultRingtoneUri());
        if (mMediaPlayer == null) {//有的手机铃声会创建失败，如果创建失败，播放我们自己的铃声
            SoundPoolUtils.playCallWaitingAudio();//自己定义的铃音播放工具类。具体实现见下方
        } else {
            mMediaPlayer.setLooping(true);// 设置循环
            try {
                mMediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }
    }

    /**
     * 停止播放来电声音
     */
    private void stopAlarm() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
        SoundPoolUtils.stopCallWaitingAudio();
    }

    /**
     * 开启震动
     */
    private void startVibrate() {
        if (mVibrator == null) {
            //获取震动服务
            mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        //震动模式隔1秒震动1.4秒
        long[] pattern = {1000, 1400};
        //震动重复，从数组的0开始（-1表示不重复）
        mVibrator.vibrate(pattern, 0);
    }

    /**
     * 停止震动
     */
    private void stopVibrate() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }
    }

    /**
     * 处理情景模式
     *
     * @param ringerMode
     */
    private void handleRingMode(int ringerMode) {
        stopAlarm();//关闭铃声
        stopVibrate();//关闭震动
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL://标准模式（铃声和震动）
                startAlarm();
                startVibrate();
                break;
            case AudioManager.RINGER_MODE_SILENT://静音模式
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式【开启震动不要忘记系统震动权限】
                startVibrate();
                break;
            default:
                break;
        }

    }

    /**
     * 跳转MusicLocation
     */
    private void toMusicLocation() {
        Intent intent = new Intent(getActivity(), MusicLocationActivity.class);
        startActivity(intent);
    }

    /**
     * 设置通知
     */
    @SuppressLint("NewApi")
//    private void setNotification() {
//
//        NotificationCompat.Builder builder = new Builder(this);
//
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        // 点击跳转到主界面
//        PendingIntent intent_go = PendingIntent.getActivity(this, 5, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.notice, intent_go);
//
//        // 4个参数context, requestCode, intent, flags
//        PendingIntent intent_close = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_close, intent_close);
//
//        // 设置上一曲
//        Intent prv = new Intent();
//        prv.setAction(SyncStateContract.Constants.ACTION_PRV);
//        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, prv,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_prev, intent_prev);
//
//        // 设置播放
//        if (Myapp.isPlay) {
//            Intent playorpause = new Intent();
//            playorpause.setAction(SyncStateContract.Constants.ACTION_PAUSE);
//            PendingIntent intent_play = PendingIntent.getBroadcast(getActivity(), 2,
//                    playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.widget_play, intent_play);
//        }
//        if (!Myapp.isPlay) {
//            Intent playorpause = new Intent();
//            playorpause.setAction(SyncStateContract.Constants.ACTION_PLAY);
//            PendingIntent intent_play = PendingIntent.getBroadcast(getActivity(), 6,
//                    playorpause, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.widget_play, intent_play);
//        }
//
//        // 下一曲
//        Intent next = new Intent();
//        next.setAction(SyncStateContract.Constants.ACTION_NEXT);
//        PendingIntent intent_next = PendingIntent.getBroadcast(getActivity(), 3, next,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_next, intent_next);
//
//        // 设置收藏
//        PendingIntent intent_fav = PendingIntent.getBroadcast(getActivity(), 4, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_fav, intent_fav);
//
//        builder.setSmallIcon(R.drawable.notification_bar_icon); // 设置顶部图标
//
//        Notification notify = builder.build();
//        notify.contentView = remoteViews; // 设置下拉图标
//        notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
//        notify.flags = Notification.FLAG_ONGOING_EVENT;
//        notify.icon = R.drawable.notification_bar_icon;
//
//        manager.notify(100, notify);
//    }

//    public Handler handler2 = new Handler() {
//
//        public void handleMessage(android.os.Message msg) {
//
//            Mp3Info info = (Mp3Info) msg.obj;
//            Bitmap bitmap = MediaUtil.getArtwork(getApplicationContext(),
//                    info.getId(), info.getAlbumId(), true, false);
//            btm_album.setImageBitmap(bitmap);
//            btm_artist.setText(info.getArtist());
//            btm_title.setText(info.getTitle());
//
//            // 播放歌曲
//            btm_state
//                    .setImageResource(R.drawable.player_btn_radio_pause_normal);
//
//            // 设置通知栏的图片文字
//            remoteViews = new RemoteViews(getPackageName(),
//                    R.layout.notification_custom_music);
//            remoteViews.setImageViewBitmap(R.id.widget_album, bitmap);
//            remoteViews.setTextViewText(R.id.widget_title, info.getTitle());
//            remoteViews.setTextViewText(R.id.widget_artist, info.getArtist());
//            if (Myapp.isPlay) {
//                remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_btn_pause_normal);
//            }else {
//                remoteViews.setImageViewResource(R.id.widget_play, R.drawable.widget_btn_play_normal);
//            }
//
//            setNotification();
//        }
//    };
    /**
     * 设置下拉刷新颜色
     */
    private void setRefresh() {
        //设置圆圈进度条的背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                getResources().getColor(R.color.colorWhite));

        //设置进度条变化的颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorViolet,
                R.color.colorRed,
                R.color.colorYellow);
    }

    /**
     * 连接按钮点击方法
     */
    public void onConnectButtonClicked() {

        if (breakButton.getText().equals("蓝牙连接")) {
            Intent serverIntent = new Intent(getActivity(), BedBlueToothActivity.class); // 跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // 设置返回宏定义
            breakButton.setText(R.string.duankai);

        } else {
            // 关闭连接socket
            try {
                // 关闭蓝牙
                breakButton.setText(R.string.button_break);
                mChatService.stop();
            } catch (Exception e) {
            }
        }
        return;
    }

    /**
     * 判断连接状态 改变图标样式
     *
     * @param isConnection
     */
    private void setFailOrSuccess(boolean isConnection) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ImageView imageView = activity.findViewById(R.id.img_toolbar_right);
        if (isConnection == true) {
            imageView.setImageResource(R.mipmap.ic_bed_conection_success);
        } else if (isConnection == false) {
            imageView.setImageResource(R.mipmap.ic_bed_conection_fail);
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // 初始化bluetoothchatservice执行蓝牙连接
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // 缓冲区初始化传出消息
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * 发送消息
     *
     * @param message
     * @param category
     */
    private void onClickSendMessage(String message, int category) {
        if (category == 2) {
            sendMessage(message, category);
        } else if (category == 1)

        {
            sendMessage(message, category);
        }

    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 发送一个消息
     *
     * @param message 一个文本字符串发送.
     */
    public void sendMessage(String message, int c) {
        // 检查我们实际上在任何连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            setFailOrSuccess(isConnection);
            return;
        }
        // 检查实际上有东西寄到
        if (message.length() > 0) {
            if (c == 2) {
                mChatService.write(hexStringToByte(message));
            } else if (c == 1) {
                // 得到消息字节和告诉bluetoothchatservice写
                byte[] send = message.getBytes();
                mChatService.write(send);
                // 重置字符串缓冲区零和清晰的文本编辑字段
                //mOutEditText.setText(mOutStringBuffer);
                //mOutEditText2.setText(mOutStringBuffer);
            }
        } else if (message.length() <= 0) {
            Toast.makeText(getActivity(), "连接已断开", Toast.LENGTH_LONG).show();
            // 用户未启用蓝牙或发生错误
            mChatService = new BluetoothChatService(getActivity(), mHandler);
            Intent serverIntent = new Intent(getActivity(),
                    BedBlueToothActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    }

    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 将字符编码转换成UTF-8码
     */
    public String toUTF_8(String str) throws UnsupportedEncodingException {
        return this.changeCharset(str, "UTF_8");
    }

    /**
     * 回传的蓝牙设备连接状态
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // 当devicelistactivity返回连接装置
                if (resultCode == Activity.RESULT_OK) {
                    // 获得设备地址
                    String address = data.getExtras().getString(
                            BedBlueToothActivity.EXTRA_DEVICE_ADDRESS);
                    // 把蓝牙设备对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 试图连接到装置
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 当请求启用蓝牙返回
                if (resultCode == Activity.RESULT_OK) {
                    // 蓝牙已启用，所以建立一个聊天会话
                    setupChat();
                } else {
                    // 用户未启用蓝牙或发生错误
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
//                    finish();
                }
        }
    }

}
