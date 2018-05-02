package com.hur.lottery.entity;

import java.util.List;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/03 09:57
 *   desc    : 实时消息、历史消息、超大极限数据实体类
 *   version : 1.0
 * </pre>
 */

public class DataBean {
    /**
     * ssc : TJ
     * play : R2_HZ
     * position : 12
     * regex : 23789
     * id : 20180327026
     * count : 16
     * avg1 : 17.29
     * avg2 : 20.54
     * hisList : [{"key":16,"val":1}]
     * maxList : [{"key":20,"val":13},{"key":21,"val":4},{"key":22,"val":10},{"key":23,"val":3},{"key":24,"val":2},{"key":28,"val":1},{"key":30,"val":1}]
     * time : 2018-03-27 13:21:32
     */

    private String ssc;
    private String play;
    private String position;
    private String regex;
    private String id;
    private int count;
    private String avg1;
    private String avg2;
    private String time;
    private List<HisListBean> hisList;
    private List<MaxListBean> maxList;

    public String getSsc() {
        return ssc;
    }

    public void setSsc(String ssc) {
        this.ssc = ssc;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAvg1() {
        return avg1;
    }

    public void setAvg1(String avg1) {
        this.avg1 = avg1;
    }

    public String getAvg2() {
        return avg2;
    }

    public void setAvg2(String avg2) {
        this.avg2 = avg2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<HisListBean> getHisList() {
        return hisList;
    }

    public void setHisList(List<HisListBean> hisList) {
        this.hisList = hisList;
    }

    public List<MaxListBean> getMaxList() {
        return maxList;
    }

    public void setMaxList(List<MaxListBean> maxList) {
        this.maxList = maxList;
    }

    public static class HisListBean {
        /**
         * key : 16
         * val : 1
         */

        private int key;
        private int val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }
    }

    public static class MaxListBean {
        /**
         * key : 20
         * val : 13
         */

        private int key;
        private int val;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }
    }
}
