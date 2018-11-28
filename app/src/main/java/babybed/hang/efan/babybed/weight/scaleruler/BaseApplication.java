package babybed.hang.efan.babybed.weight.scaleruler;

import android.app.Application;
import android.content.Context;

import babybed.hang.efan.babybed.weight.scaleruler.utils.DrawUtil;


/**
 * application
 */
public class BaseApplication extends Application {
    protected static BaseApplication sInstance;

    public BaseApplication() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DrawUtil.resetDensity(this);
    }


    public static Context getAppContext() {
        return sInstance;
    }


}
