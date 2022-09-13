package com.lyd.tooltest.InterFace;

import com.lyd.tooltest.Entity.YingDi.Cards.HSCard;
import com.lyd.tooltest.Entity.YingDi.Cards.YDCardData;
import com.lyd.tooltest.Entity.YingDi.Decks.HSDeck;
import com.lyd.tooltest.Entity.YingDi.Decks.YDDeskData;
import com.lyd.tooltest.Entity.YingDi.YDMsg;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IYingDi {
    String API2_URL = "https://api2.iyingdi.com";
    String BASE_URL = "https://www.iyingdi.com";

    // TODO: 2022/9/9 准备做营地搜索卡牌
    @FormUrlEncoded
    @POST("/hearthstone/card/search/vertical")
    Call<YDMsg<YDCardData<List<HSCard>>>> getHearthStoneCard(@HeaderMap Map<String,String> headerMap, @FieldMap Map<String,String> formMap);


    @Headers({
            "Accept: */*",
            "Accept-Language: zh-CN,zh;q=0.9",
            "Connection: keep-alive",
            "Origin: https://iyingdi.com",
            "Referer: https://iyingdi.com/",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36",
    })
    @GET("/hearthstone/deck/search_open")
    Observable <YDMsg<List<YDDeskData<HSDeck>>>> getHearthStoneDeckList(@QueryMap Map<String,String> queryMap);
}
