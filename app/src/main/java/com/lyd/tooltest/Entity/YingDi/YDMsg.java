package com.lyd.tooltest.Entity.YingDi;

public class YDMsg<T> {
    private T data;

    private boolean success;

    private int total; //这个total，并不是所有的msg会带着的，现阶段是只有卡组列表查询的时候会带着，假如请求其他的，如卡牌数据或啥的就不需要

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "YDMsg{" +
                "data=" + data +
                ", success=" + success +
                ", total=" + total +
                '}';
    }
}
