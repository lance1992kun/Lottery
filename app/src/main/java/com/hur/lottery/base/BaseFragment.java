package com.hur.lottery.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hur.lottery.entity.Constant;
import com.hur.lottery.entity.DataBean;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:30
 *   desc    : Fragment基类
 *   version : 1.0
 * </pre>
 */
public abstract class BaseFragment extends Fragment
        implements IBaseView, BaseActivity.IRefreshListener {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    /**
     * 当前 Activity 渲染的视图 View
     */
    protected View contentView;
    /**
     * 连接的Activity
     */
    protected BaseActivity mActivity;
    /**
     * 上次点击时间
     */
    private long lastClick = 0;
    /**
     * 联网转圈圈
     */
    private ProgressDialog mLoadingDialog;
    /**
     * Rx任务管理器
     */
    private CompositeDisposable compositeDisposable;

    /**
     * 将任务添加到管理器中
     *
     * @param disposable 任务
     */
    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 取消任务
     */
    protected void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        contentView = inflater.inflate(bindLayout(), null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mActivity.setRefreshListener(this);
        initView(savedInstanceState, contentView);
        doBusiness();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onDestroyView() {
        if (contentView != null) {
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (!isFastClick()) onWidgetClick(view);
    }

    /**
     * 判断是否快速点击
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= Constant.FAST_CLICK) {
            lastClick = now;
            return false;
        }
        return true;
    }

    /**
     * 获取最大历史遗漏
     *
     * @param listBeen 数据列表
     * @return 最大历史遗漏
     */
    protected String getMaxString(List<DataBean.MaxListBean> listBeen) {
        String result = "";
        for (int i = 0; i < listBeen.size(); i++) {
            DataBean.MaxListBean maxListBean = listBeen.get(i);
            result += maxListBean.getKey() + "=" + maxListBean.getVal();
            if (i < listBeen.size() - 1) {
                result += ",";
            }
        }
        return result;
    }

    /**
     * 获取历史遗漏
     *
     * @param hisListBeen 历史数据列表
     * @return 历史遗漏
     */
    protected String getHisString(List<DataBean.HisListBean> hisListBeen) {
        String result = "";
        for (int i = 0; i < hisListBeen.size(); i++) {
            DataBean.HisListBean maxListBean = hisListBeen.get(i);
            result += maxListBean.getKey() + "=" + maxListBean.getVal();
            if (i < hisListBeen.size() - 1) {
                result += ",";
            }
        }
        return result;
    }

    /**
     * 显示联网对话框
     */
    protected void showLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mLoadingDialog = new ProgressDialog(mActivity);
        mLoadingDialog.setMessage("正在请求…");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * 消失联网对话框
     */
    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
