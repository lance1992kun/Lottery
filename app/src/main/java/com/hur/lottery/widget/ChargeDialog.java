package com.hur.lottery.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.R;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/28 10:02
 *   desc    : 充值Dialog
 *   version : 1.0
 * </pre>
 */
public class ChargeDialog extends DialogFragment {
    /**
     * 数值修改框
     */
    private TextInputEditText mEditText;
    /**
     * 上次点击的时间
     */
    private long lastClick = 0;
    /**
     * 回调
     */
    private OnDismissListener mCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 根布局
        View mContentView = inflater.inflate(R.layout.layout_dialog, container, false);
        // 输入框
        mEditText = (TextInputEditText) mContentView.findViewById(R.id.mEditText);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        // 确认按钮
        mContentView.findViewById(R.id.mOkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mEditText.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    ToastUtils.showShort("请输入充值码");
                } else {
                    mEditText.setText("");
                    mCallBack.onDismiss(value);
                    dismiss();
                }
            }
        });
        // 取消按钮
        mContentView.findViewById(R.id.mCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空编辑框并消失对话框
                mEditText.setText("");
                dismiss();
            }
        });
        // 返回键不消失
        setCancelable(false);
        return mContentView;
    }

    /**
     * 初始化对话框数据
     *
     * @param callback 回调
     */
    public void init(OnDismissListener callback) {
        mCallBack = callback;
    }

    @Override
    public void show(FragmentManager manager, String title) {
        if (!isFastClick() && !this.isAdded()) {
            super.show(manager, SettingDialog.class.getSimpleName());
        }
    }

    /**
     * 判断是否快速点击
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getDialog() == null) {
            return;
        }
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        super.onActivityCreated(savedInstanceState);
        if (window != null) {
            // 背景色透明
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }
    }

    /**
     * 消失监听
     */
    public interface OnDismissListener {
        /**
         * 消失
         *
         * @param value 输入的数值
         */
        void onDismiss(String value);
    }
}
