package com.lyd.tooltest.Entity.YingDi;

public class YDMsg<T> {
    private YDData<T> data;

    private boolean success;

    public YDData<T> getData() {
        return data;
    }

    public void setData(YDData<T> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
