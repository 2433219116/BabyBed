package babybed.hang.efan.babybed.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import babybed.hang.efan.babybed.R;
import kale.ui.view.dialog.BaseCustomDialog;
import kale.ui.view.dialog.BaseEasyDialog;

/**
 * Created by Efan on 2018/3/21.
 */

public class ModelDialog extends BaseCustomDialog {
    private TextView mTxtMessage;
    private TextView mTxtTittle;
    private final String KEY_INPUT_TITTLE  = "1";
    private final String KEY_INPUT_MESSAGE = "2";
    private String mMessage, mTittle;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 拿到参数
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMessage = arguments.getString(KEY_INPUT_MESSAGE);
            mTittle = arguments.getString(KEY_INPUT_TITTLE);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_model;
    }

    @Override
    protected void bindViews(View view) {
        mTxtMessage = view.findViewById(R.id.txt_dialog_model_message);
        mTxtTittle = view.findViewById(R.id.txt_dialog_model_title);
    }

    @Override
    protected void setViews() {
        // 设置view
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_circle_white); // 资源文件
        if (mTxtMessage != null || mTxtTittle != null) {
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtTittle.setVisibility(View.VISIBLE);
            if (!isRestored()) {
                mTxtMessage.setText(mMessage);
                mTxtTittle.setText(mTittle);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁相关的view
//        mInputTextEt = null;
    }


    public class ModelBuilder extends BaseEasyDialog.Builder<Builder> {
        private Bundle bundle = new Bundle(); // 通过bundle来支持参数

        public ModelBuilder(@NonNull Context context) {
            super(context);
        }

        public ModelBuilder setInputText(String tittle, String message) {
            bundle.putString(KEY_INPUT_TITTLE, tittle);
            bundle.putString(KEY_INPUT_MESSAGE, message);
            return this;
        }

        protected ModelDialog createDialog() {
            ModelDialog dialog = new ModelDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

    }

}
