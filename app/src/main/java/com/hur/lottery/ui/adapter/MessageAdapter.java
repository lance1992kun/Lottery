package com.hur.lottery.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hur.lottery.R;
import com.hur.lottery.entity.HeaderBean;
import com.hur.lottery.entity.SubBean;

import java.util.List;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 16:28
 *   desc    : 消息列表适配器
 *   version : 1.0
 * </pre>
 */

public class MessageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * 第一层级层级
     */
    public static final int TYPE_LEVEL_HEADER = 0;
    /**
     * 第二层级项目
     */
    public static final int TYPE_LEVEL_SUB = 1;
    /**
     * 当前展开的下标
     */
    private HeaderBean currentHeaderBean = null;

    public MessageAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_HEADER, R.layout.layout_item_header);
        addItemType(TYPE_LEVEL_SUB, R.layout.layout_item_sub);
        // 重置标识
        resetFlag();
    }

    /**
     * 重置开关标识
     */
    private void resetFlag() {
        if (mData != null && mData.size() > 0) {
            currentHeaderBean = (HeaderBean) mData.get(0);
        }
    }

    @Override
    public void setNewData(@Nullable List<MultiItemEntity> data) {
        super.setNewData(data);
        // 重置标识
        resetFlag();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            // 第一层级
            case TYPE_LEVEL_HEADER:
                final HeaderBean headerBean = (HeaderBean) item;
                // 打开的项目箭头变化
                if (headerBean.isExpanded()) {
                    helper.getView(R.id.mRightArrow).setBackgroundResource(R.drawable.icon_arrow_down);
                    helper.getView(R.id.mHeaderLayout).setBackgroundColor(
                            mContext.getResources().getColor(R.color.main_color_divider));
                } else {
                    helper.getView(R.id.mRightArrow).setBackgroundResource(R.drawable.icon_arrow_right);
                    helper.getView(R.id.mHeaderLayout).setBackgroundColor(
                            mContext.getResources().getColor(android.R.color.white));
                }
                // 打开关闭监听
                helper.getView(R.id.mHeaderLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取当前下标
                        int position = helper.getAdapterPosition();
                        // 根据状态开关
                        if (headerBean.isExpanded()) {
                            // 关闭Header
                            collapse(position, false);
                        } else {
                            // 展开Header
                            expand(position, false);
                            // 记录当前展开的Header
                            currentHeaderBean = headerBean;
                            // 关闭除当前Header以外的所有Header
                            collapseAll();
                        }
                    }
                });
                // 左边文字
                helper.setText(R.id.mLeftText, headerBean.getLeftText());
                // 右侧文字
                helper.setText(R.id.mRightText, headerBean.getRightText());
                break;
            // 第二层级
            case TYPE_LEVEL_SUB:
                final SubBean subBean = (SubBean) item;
                // 左边文字
                helper.setText(R.id.mLeftText, subBean.getLeftText());
                // 右侧文字
                helper.setText(R.id.mRightText, subBean.getRightText());
                break;
        }
    }

    /**
     * 关闭除了当前点击以外的项目
     */
    private void collapseAll() {
        for (int i = mData.size() - 1; i >= getHeaderLayoutCount(); i--) {
            if (i != mData.indexOf(currentHeaderBean)) {
                collapse(i, false);
            }
        }
        // 滑动到该位置
        // showSection();
    }

    /**
     * 显示当前的Header
     */
    private void showSection() {
        int index = 0;
        if (currentHeaderBean != null) {
            index = mData.indexOf(currentHeaderBean);
        }
        LinearLayoutManager manager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
        manager.scrollToPositionWithOffset(index, 0);
        manager.setStackFromEnd(true);
    }

}
