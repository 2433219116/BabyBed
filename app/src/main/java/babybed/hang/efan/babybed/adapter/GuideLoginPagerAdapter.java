package babybed.hang.efan.babybed.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Efan on 2017/12/15.
 */

public class GuideLoginPagerAdapter extends PagerAdapter {
    List<View> mImgViewList;
    private Context mContext;

    public GuideLoginPagerAdapter(Context context, List<View> List) {
        super();
        mImgViewList = List;
        mContext = context;
    }

    /**
     * 实例化条目
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，
     * 我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
     *
     * @param container
     * @param position  索引值
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImgViewList != null) {
            if (mImgViewList.size() > 0) {
                container.addView(mImgViewList.get(position));
                return mImgViewList.get(position);
            }
        }
        return null;
    }


    /**
     * 销毁条目
     * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mImgViewList != null) {
            if (mImgViewList.size() > 0) {
                container.removeView(mImgViewList.get(position));
            }
        }
    }

    /**
     * 获取条目数量
     *
     * @return
     */
    @Override
    public int getCount() {
        if (mImgViewList != null) {
            return mImgViewList.size();
        }
        return 0;
    }

    /**
     * 用于判断某个view是否与key对象关联。
     * <p>
     * 功能：该函数用来判断instantiateItem(ViewGroup, int)函数所返回来的Key与一个页面视图是否是代表的同一个视图(即它俩是否是对应的，对应的表示同一个View)
     *
     * @param view   container.addView对应的key
     * @param object 就是你初始化的时候添加进去的container.addView这个时候添加进去的
     * @return 如果对应的是同一个View，返回True，否则返回False。
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }
}
