package com.hur.lottery.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseFragment;
import com.hur.lottery.entity.BaseResponse;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.entity.DataBean;
import com.hur.lottery.entity.HeaderBean;
import com.hur.lottery.entity.SubBean;
import com.hur.lottery.net.HttpUrl;
import com.hur.lottery.net.NetCallBack;
import com.hur.lottery.ui.adapter.MessageAdapter;
import com.hur.lottery.utils.RequestHelper;
import com.hur.lottery.widget.HorizontalDivider;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/29 14:35
 *   desc    : 超大极限界面
 *   version : 1.0
 * </pre>
 */

public class LimitFragment extends BaseFragment {
    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    /**
     * 列表
     */
    private RecyclerView mListView = null;
    /**
     * 空数据
     */
    private View mEmptyView = null;
    /**
     * 列表适配器
     */
    private MessageAdapter mAdapter = null;
    /**
     * 列表数据
     */
    private List<DataBean> data = null;
    private List<MultiItemEntity> mResource = null;

    /**
     * 获取实时消息界面
     *
     * @return 实时消息界面
     */
    public static LimitFragment newInstance() {
        Bundle args = new Bundle();
        LimitFragment fragment = new LimitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            // 显示就获取数据
            getData();
        }
    }

    /**
     * 联网获取实时信息
     */
    private void getData() {
        // 置位状态
        mSwipeRefreshLayout.setRefreshing(true);
        // 清空数据
        data.clear();
        mResource.clear();
        // 进行网络请求
        OkGo.<BaseResponse<String>>post(HttpUrl.GET_ORDER_LIMIT)
                .tag(this)
                .headers("token", SPUtils.getInstance().getString(Constant.USER_TOKEN))
                .upJson(RequestHelper.getLimitBody(
                        SPUtils.getInstance().getInt(Constant.BIG_LIMIT_COUNT, 23)))
                .execute(new NetCallBack<BaseResponse<String>>() {
                    @Override
                    public void onStart(Request<BaseResponse<String>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                        // 停止刷新
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        // 显示空布局
                        setData(null);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }

                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        // 停止刷新
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        // 将联网获得的数据添加到列表中
                        if (response.body().getCode() == 1) {
                            // 设置数据
                            setData(response.body().getData());
                        } else {
                            ToastUtils.showShort(response.body().getMsg());
                        }
                    }
                });
    }

    /**
     * 设置数据
     */
    private void setData(String dataString) {
        if (!TextUtils.isEmpty(dataString)) {
            // Json的解析类对象
            JsonParser parser = new JsonParser();
            // 将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(dataString).getAsJsonArray();
            Gson gson = new Gson();
            // 加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                DataBean bean = gson.fromJson(user, DataBean.class);
                data.add(bean);
            }
        }
        for (int i = 0; i < data.size(); i++) {
            // 根据获取的联网数据进行初始化布局
            DataBean dataBean = data.get(i);
            String leftText = (i + 1) + "、" + dataBean.getSsc() + "【" + dataBean.getPlay() + "】";
            HeaderBean headerBean = new HeaderBean(leftText, dataBean.getId());
            SubBean subBean0 = new SubBean("位置", dataBean.getPosition());
            SubBean subBean1 = new SubBean("投注方案", dataBean.getRegex());
            SubBean subBean2 = new SubBean("遗漏", dataBean.getCount() + "");

            headerBean.addSubItem(subBean0);
            headerBean.addSubItem(subBean1);
            headerBean.addSubItem(subBean2);
            // 加入到数据源
            mResource.add(headerBean);
        }
        // 加入数据
        if (mResource.size() > 0) {
            mAdapter.setNewData(mResource);
            mAdapter.expand(0);
            // 滑动到第一个
            mListView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void initData(Bundle bundle) {
        // 初始化数据列表
        data = new ArrayList<>();
        mResource = new ArrayList<>();
        // 初始化数据列表适配器
        mAdapter = new MessageAdapter(mResource);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_im;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        // 下拉刷新控件
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        // 列表
        mListView = (RecyclerView) view.findViewById(R.id.mListView);
        // 空数据布局
        mEmptyView = mActivity.getLayoutInflater()
                .inflate(R.layout.layout_empty_view, (ViewGroup) mListView.getParent(), false);
        mEmptyView.setOnClickListener(this);
        // 设置空布局
        mAdapter.setEmptyView(mEmptyView);
        // 设置适配器
        mListView.setAdapter(mAdapter);
        HorizontalDivider mDivider = new HorizontalDivider
                .Builder(mActivity)
                .color(Color.GRAY)
                .build();
        mListView.addItemDecoration(mDivider);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onWidgetClick(View view) {
        if (view == mEmptyView) {
            // 联网获取实时信息
            getData();
        }
    }

    @Override
    public void onDataRefresh() {
        // 联网获取实时信息
        getData();
    }
}
