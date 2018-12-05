package com.hur.lottery.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.R;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.enums.SettingType;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/27 14:04
 *   desc    : 更改数值的Dialog
 *   version : 1.0
 * </pre>
 */
public class SettingDialog extends DialogFragment {
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
    /**
     * 输入的数值
     */
    private SettingType type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 根布局
        View mContentView = inflater.inflate(R.layout.layout_dialog, container, false);
        // 输入框
        mEditText = (TextInputEditText) mContentView.findViewById(R.id.mEditText);
        TextInputLayout mTextInputLayout = (TextInputLayout) mContentView.findViewById(R.id.mTextInputLayout);
        mTextInputLayout.setHint("请输入需要修改的数值");
        // 确认按钮
        mContentView.findViewById(R.id.mOkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(mEditText.getText().toString());
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
        if (type == SettingType.TYPE_LIMIT) {
            String limit = SPUtils.getInstance().getInt(Constant.LIMIT_COUNT, 13) + "";
            mEditText.setText(limit);
        } else if (type == SettingType.TYPE_BIG_LIMIT) {
            String bigLimit = SPUtils.getInstance().getInt(Constant.BIG_LIMIT_COUNT, 23) + "";
            mEditText.setText(bigLimit);
        }
        return mContentView;
    }

    /**
     * 检验数字是否合法
     *
     * @param number 数字
     */
    private void check(String number) {
        // 极限通知
        if (type == SettingType.TYPE_LIMIT) {
            try {
                int value = Integer.valueOf(number);
                if (value > 18 || value < 6) {
                    ToastUtils.showShort("请输入6-18之间的数值");
                } else {
                    // 回调
                    mCallBack.onDismiss(value);
                    // 清空编辑框并消失对话框
                    mEditText.setText("");
                    dismiss();
                }
            } catch (Throwable throwable) {
                // 清空编辑框
                mEditText.setText("");
                ToastUtils.showShort("请输入合法的数值");
            }
        }
        // 超大极限
        else if (type == SettingType.TYPE_BIG_LIMIT) {
            try {
                int value = Integer.valueOf(number);
                if (value > 25 || value < 20) {
                    ToastUtils.showShort("请输入20-25之间的数值");
                } else {
                    // 回调
                    mCallBack.onDismiss(value);
                    // 清空编辑框并消失对话框
                    mEditText.setText("");
                    dismiss();
                }
            } catch (Throwable throwable) {
                // 清空编辑框
                mEditText.setText("");
                ToastUtils.showShort("请输入合法的数值");
            }
        }
    }

    /**
     * 初始化对话框数据
     *
     * @param type     类型
     * @param callback 回调
     */
    public void init(SettingType type, OnDismissListener callback) {
        this.type = type;
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
        void onDismiss(int value);
    }
}
