package com.hur.lottery.base;

import android.os.Bundle;
import android.view.View;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:30
 *   desc    : 基类接口
 *   version : 1.0
 * </pre>
 */
interface IBaseView extends View.OnClickListener {

    /**
     * 初始化数据
     *
     * @param bundle 传递过来的 bundle
     */
    void initData(final Bundle bundle);

    /**
     * 绑定布局
     *
     * @return 布局 Id
     */
    int bindLayout();

    /**
     * 初始化 view
     */
    void initView(final Bundle savedInstanceState, final View view);

    /**
     * 业务操作
     */
    void doBusiness();

    /**
     * 视图点击事件
     *
     * @param view 视图
     */
    void onWidgetClick(final View view);

}
