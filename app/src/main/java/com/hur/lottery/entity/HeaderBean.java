package com.hur.lottery.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hur.lottery.ui.adapter.MessageAdapter;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/29 11:29
 *   desc    : 列表头部实体类
 *   version : 1.0
 * </pre>
 */

public class HeaderBean extends AbstractExpandableItem<SubBean> implements MultiItemEntity {

    /**
     * 头部左侧文字
     */
    private String leftText;
    /**
     * 头部右侧文字
     */
    private String rightText;

    /**
     * 构造函数
     *
     * @param leftText  头部左侧文字
     * @param rightText 头部右侧文字
     */
    public HeaderBean(String leftText, String rightText) {
        this.leftText = leftText;
        this.rightText = rightText;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return MessageAdapter.TYPE_LEVEL_HEADER;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getRightText() {
        return rightText;
    }
}
