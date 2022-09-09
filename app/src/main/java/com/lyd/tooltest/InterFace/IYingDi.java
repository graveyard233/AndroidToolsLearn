package com.lyd.tooltest.InterFace;

import com.lyd.tooltest.Entity.YingDi.HSCard;
import com.lyd.tooltest.Entity.YingDi.YDMsg;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IYingDi {
    String API2_URL = "https://api2.iyingdi.com";
    String BASE_URL = "https://www.iyingdi.com";

    // TODO: 2022/9/9 准备做营地搜索卡牌
    @POST("/hearthstone/card/search/vertical")
    Call<YDMsg<List<HSCard>>> getHearthStoneCard(@HeaderMap Map<String,String> headerMap, @FieldMap Map<String,Object> formMap);
}
