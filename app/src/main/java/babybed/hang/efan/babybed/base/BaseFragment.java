package babybed.hang.efan.babybed.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public abstract class BaseFragment extends Fragment {

    private View rootView;

    /**
     * 初始化布局
     */
    protected abstract int getLayoutRes();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initEvent();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        rootView = view;
        initView();
        initData();
        initListener();
        initEvent();
        return view;
    }

    protected <T extends View> T bindViewId(int resId) {
        return (T) rootView.findViewById(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
