package com.lyd.tooltest.InterFace;

import com.lyd.tooltest.Entity.WanAndroid.WanHomeBanner;
import com.lyd.tooltest.Entity.WanAndroid.WanMsg;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IWanAndroid {
    String BASE_URL = "https://www.wanandroid.com";

    @GET("banner/json")
    Call<WanMsg<List<WanHomeBanner>>> homeBanner();
}
