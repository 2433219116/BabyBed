package babybed.hang.efan.babybed.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import babybed.hang.efan.babybed.R;

/**
 * Created by Efan on 2018/3/17.
 */

public class StoreInfoLooperViewpagerAdapter extends PagerAdapter {
    private int[] simg = {R.mipmap.img_store_information, R.mipmap.img_store_information, R.mipmap.img_store_information};
    private Context mContext;

    public StoreInfoLooperViewpagerAdapter(Activity activity) {
        mContext = activity;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_looper, container,false);
        ImageView imageView = view.findViewById(R.id.img_item_looper);
        imageView.setImageResource(simg[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return simg.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
