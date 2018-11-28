package babybed.hang.efan.babybed.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import babybed.hang.efan.babybed.R;
import kale.ui.view.dialog.BaseCustomDialog;
import kale.ui.view.dialog.BaseEasyDialog;

/**
 * Created by Efan on 2018/3/21.
 */

public class ArchivesSexDialog extends BaseCustomDialog {
    private RadioGroup  mRgSex;
    private RadioButton mRbBoy, mRbGirl;
    public String mSex;
    private TextView mTxtBoy,mTxtGirl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_archives_sex;
    }

    @Override
    protected void bindViews(View view) {
        mRgSex = view.findViewById(R.id.rg_archives_sex);
        mRbBoy = view.findViewById(R.id.rb_archives_boy);
        mRbGirl = view.findViewById(R.id.rb_archives_girl);
        mTxtBoy=view.findViewById(R.id.txt_archives_boy);
        mTxtGirl=view.findViewById(R.id.txt_archives_girl);
        mRgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_archives_girl:
                        mSex = "女孩";
                        setTextColor(mTxtBoy,mTxtGirl);
                        break;
                    case R.id.rb_archives_boy:
                        mSex = "男孩";
                        setTextColor(mTxtGirl,mTxtBoy);
                        break;
                }
            }
        });
    }

    private void setTextColor(TextView black,TextView main) {
        black.setTextColor(getResources().getColor(R.color.colorBlack));
        main.setTextColor(getResources().getColor(R.color.colorViolet));
    }

    @Override
    protected void setViews() {
        // 设置view
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_circle_white); // 资源文件
        if (mRgSex != null) {
            mRgSex.setVisibility(View.VISIBLE);
            if (!isRestored()) {
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁相关的view
        mRgSex = null;
    }

    public String getSex() {
        return mSex;
    }

    public static class Builder extends BaseEasyDialog.Builder<Builder> {
        private Bundle bundle = new Bundle(); // 通过bundle来支持参数

        public Builder(@NonNull Context context) {
            super(context);
        }


        protected ArchivesSexDialog createDialog() {
            ArchivesSexDialog dialog = new ArchivesSexDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

    }

}
