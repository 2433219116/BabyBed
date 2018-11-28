package babybed.hang.efan.babybed.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import babybed.hang.efan.babybed.R;
import kale.ui.view.dialog.BaseCustomDialog;
import kale.ui.view.dialog.BaseEasyDialog;

/**
 * Created by Efan on 2018/3/20.
 */

public class ArchivesNameDialog extends BaseCustomDialog {
    private EditText mEdtName;
    private String   mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_archives_name;
    }

    @Override
    protected void bindViews(View view) {
        mEdtName = view.findViewById(R.id.edt_dialog_archives_name);
    }

    @Override
    protected void setViews() {
        // 设置view
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_circle_white); // 资源文件
        if (mEdtName != null) {
            mEdtName.setVisibility(View.VISIBLE);
            if (!isRestored()) {
                mEdtName.setText("");
                mEdtName.setHint("请重新输入");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁相关的view
        mEdtName = null;
    }

    public String getName() {
        mName = mEdtName.getText().toString().trim();
        return mName;
    }

    public static class Builder extends BaseEasyDialog.Builder<Builder> {
        private Bundle bundle = new Bundle(); // 通过bundle来支持参数

        public Builder(@NonNull Context context) {
            super(context);
        }

        protected ArchivesNameDialog createDialog() {
            ArchivesNameDialog dialog = new ArchivesNameDialog();
            dialog.setArguments(bundle);
            return dialog;
        }
    }
}


