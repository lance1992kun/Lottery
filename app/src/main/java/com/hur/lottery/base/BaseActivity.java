package com.hur.lottery.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.ui.activity.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.hur.lottery.entity.Constant.FAST_CLICK;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:30
 *   desc    : 所有Activity的基类
 *   version : 1.0
 * </pre>
 */
public abstract class BaseActivity extends AppCompatActivity
        implements IBaseView {

    /**
     * 当前 Activity 渲染的视图 View
     */
    protected View contentView;
    /**
     * 刷新接口
     */
    protected IRefreshListener mRefresh;
    /**
     * Activity对象
     */
    protected BaseActivity mActivity;
    /**
     * 用来保存View的稀疏数组
     */
    private SparseArray<View> mViews = null;
    /**
     * 上次点击时间
     */
    private long lastClick = 0;
    /**
     * 联网转圈圈
     */
    private ProgressDialog mLoadingDialog;
    /**
     * 本地广播
     */
    private LocalBroadcastManager mLocalBroadcastManager;
    /**
     * 强制下线广播
     */
    private OfflineReceiver mReceiver;
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

    /**
     * 设置刷新接口
     *
     * @param mRefresh 接口
     */
    public void setRefreshListener(IRefreshListener mRefresh) {
        this.mRefresh = mRefresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Activity对象
        mActivity = this;
        // 调用友盟统计数据(推送)
        PushAgent.getInstance(mActivity).onAppStart();
        // 初始化View数组
        mViews = new SparseArray<>();
        // 初始化本地广播对象
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        // 获取保存过得数据
        Bundle bundle = getIntent().getExtras();
        initData(bundle);
        // 设置Layout
        setBaseView(bindLayout());
        // 将保存过得数据放入InitView周期中
        initView(savedInstanceState, contentView);
        // 逻辑处理
        doBusiness();
    }

    /**
     * Sets base view.
     *
     * @param layoutId the layout id
     */
    protected void setBaseView(@LayoutRes int layoutId) {
        setContentView(contentView = LayoutInflater.from(this).inflate(layoutId, null));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 初始化友盟统计
        MobclickAgent.onPause(this);
        // 解除广播注册
        if (mReceiver != null) {
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化友盟统计
        MobclickAgent.onResume(this);
        // 初始化下线广播接受者
        mReceiver = new OfflineReceiver();
        // 注册广播
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constant.OFF_LINE_BROADCAST);
        mLocalBroadcastManager.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onClick(final View view) {
        if (!isFastClick()) {
            onWidgetClick(view);
        }
    }

    /**
     * 判断是否快速点击
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= FAST_CLICK) {
            lastClick = now;
            return false;
        }
        return true;
    }

    /**
     * 发送离线广播
     */
    public void doOffline() {
        // 发送离线广播
        Intent mIntent = new Intent(Constant.OFF_LINE_BROADCAST);
        mLocalBroadcastManager.sendBroadcast(mIntent);
    }

    /**
     * 通过ID找到View(代替FindViewByID)
     *
     * @param viewID ID
     * @return View
     */
    @SuppressWarnings("unchecked")
    public <E extends View> E F(int viewID) {
        E view = (E) mViews.get(viewID);
        if (view == null) {
            view = (E) findViewById(viewID);
            mViews.put(viewID, view);
        }
        return view;
    }

    /**
     * 设置点击事件
     *
     * @param mViews 需要设置点击事件的View
     */
    @SafeVarargs
    public final <V extends View> void setOnClick(V... mViews) {
        if (mViews == null) {
            return;
        }
        for (View mView : mViews) {
            mView.setOnClickListener(this);
        }
    }

    /**
     * 根据文字显示对话框
     *
     * @param msg 需要显示的话
     */
    protected void showLoading(String msg) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mLoadingDialog = new ProgressDialog(mActivity);
        mLoadingDialog.setMessage(msg);
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

    /**
     * 数据刷新接口
     */
    public interface IRefreshListener {
        /**
         * 需求刷新
         */
        void onDataRefresh();
    }

    /**
     * 下线广播
     */
    private class OfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 强制下线
            ToastUtils.showShort("登录信息失效，请从新登录！");
            // 终结所有Activity
            ActivityUtils.finishAllActivities();
            // 跳转到登录界面
            ActivityUtils.startActivity(LoginActivity.class);
        }
    }
}
