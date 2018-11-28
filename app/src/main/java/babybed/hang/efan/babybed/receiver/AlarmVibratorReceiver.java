package babybed.hang.efan.babybed.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by Efan on 2018/4/8.
 */

public class AlarmVibratorReceiver extends BroadcastReceiver { // 情景模式切换广播接受者
    private BRInteraction brInteraction;
    private AudioManager  am;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是情景模式切换的广播
        if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent
                .getAction())) {
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }


    public interface BRInteraction {
        void setAudioManagerState(AudioManager am);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }

}

