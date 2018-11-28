package babybed.hang.efan.babybed.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * fragment管理工厂
 * 在我们创建多个Fragment的时候，或许我们应该用工厂模式来降低程序的耦合性。
 * Created by hejunlin on 17/3/21.
 */

public class FragmentManagerWrapper {

    //使用单例模式需要用多线程的volatile
    private volatile static FragmentManagerWrapper mInstance = null;

    /**
     * 使用单例模式
     * 双重锁 防止没有new到的对象
     *
     * @return
     */
    public static FragmentManagerWrapper getInstance() {
        if (mInstance == null) {
            //同步
            synchronized (FragmentManagerWrapper.class) {
                if (mInstance == null) {
                    mInstance = new FragmentManagerWrapper();
                }
            }
        }
        return mInstance;
    }

    //存储fragment classname  fragment
    private HashMap<String, Fragment> mHashMap = new HashMap<>();


    public Fragment createFragment(Class<?> clazz) {
        return createFragment(clazz, true);
    }

    /**
     * 创建fragment
     *
     * @param clazz    class名字
     * @param isobtain 是否包含的boolean值
     * @return
     */
    public Fragment createFragment(Class<?> clazz, boolean isobtain) {
        Fragment fragment = null;
        String className = clazz.getName();
        //判断mHashMap的key值中是否有类的名字
        if (mHashMap.containsKey(className)) {
            fragment = mHashMap.get(className);
        } else {
            try {
                //newInstance()是实现IOC、反射、面对接口编程 和 依赖倒置 等技术方法的必然选择，new 只能实现具体类的实例化，不适合于接口编程。
                //newInstance是Class的一个方法，在这个过程中，是先取了这个类的不带参数的构造方法，然后调用构造方法的newInstance来创建对象。
                fragment = (Fragment) Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        if (isobtain) {
            mHashMap.put(className, fragment);
        }
        return fragment;
    }

}
