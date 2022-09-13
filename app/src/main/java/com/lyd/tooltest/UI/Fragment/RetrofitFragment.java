package com.lyd.tooltest.UI.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.tooltest.Base.BaseFragment;
import com.lyd.tooltest.Entity.WanAndroid.WanHomeBanner;
import com.lyd.tooltest.Entity.WanAndroid.WanMsg;
import com.lyd.tooltest.Entity.YingDi.Cards.HSCard;
import com.lyd.tooltest.Entity.YingDi.Cards.YDCardData;
import com.lyd.tooltest.Entity.YingDi.Decks.HSDeck;
import com.lyd.tooltest.Entity.YingDi.Decks.YDDeskData;
import com.lyd.tooltest.Entity.YingDi.YDMsg;
import com.lyd.tooltest.InterFace.IWanAndroid;
import com.lyd.tooltest.InterFace.IYingDi;
import com.lyd.tooltest.NetWork.NetWorkManager;
import com.lyd.tooltest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "RetrofitFragment";

    Button btn_pure;
    Button btn_rxjava;

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        btn_pure = find(R.id.ret_btn_pure);
        btn_rxjava = find(R.id.ret_btn_rxJava);

        btn_pure.setOnClickListener(this);
        btn_rxjava.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_retrofit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ret_btn_pure:
//                tryRetrofit();
                tryRetrofit_getCards();
                break;
            case R.id.ret_btn_rxJava:
//                tryRetrofitWithRxJava();
                tryRetrofitWithRxJava_getHSDeckList();
                break;
            default:break;
        }
    }

    private void tryRetrofit() {
        IWanAndroid service = NetWorkManager.getInstances().initRetrofit(IWanAndroid.BASE_URL).create(IWanAndroid.class);
        service.homeBanner().enqueue(new Callback<WanMsg<List<WanHomeBanner>>>() {
            @Override
            public void onResponse(Call<WanMsg<List<WanHomeBanner>>> call, Response<WanMsg<List<WanHomeBanner>>> response) {

                if (response.body() != null){
                    try {
//                        Log.d(TAG, response.body().toString());
                        for (WanHomeBanner w:
                             response.body().getData()) {
                            Log.i(TAG, "onResponse: " + w.toString());
                        }
                    } catch (Exception e){
                        Log.e(TAG, "onResponse error: " + e.getMessage());
                    }
                    finally {
                        NetWorkManager.setNull();
                    }
                }
            }

            @Override
            public void onFailure(Call<WanMsg<List<WanHomeBanner>>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    private void tryRetrofitWithRxJava(){
        NetWorkManager.getInstances().initRetrofitWithRxJava(IWanAndroid.BASE_URL)
                .create(IWanAndroid.class)
                .homeBannerWithRxJava()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WanMsg<List<WanHomeBanner>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull WanMsg<List<WanHomeBanner>> listWanMsg) {
                        if (listWanMsg != null && listWanMsg.getData() != null){
                            for (WanHomeBanner b :
                                    listWanMsg.getData()) {
                                Log.i(TAG, "onNext: " + b.toString());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        NetWorkManager.setNull();
                    }
                });

    }

    private void tryRetrofit_getCards(){
        HashMap<String,String> headerMap = new HashMap<>();
        headerMap.put("Accept","*/*");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Origin", "https://iyingdi.com");
        headerMap.put("Referer", "https://iyingdi.com/");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");


        HashMap<String,String> formMap = new HashMap<>();
        formMap.put("ignoreHero","1");
        formMap.put("statistic","total");
        formMap.put("order", "-series,+mana");
        formMap.put("token","");
        formMap.put("page","0");
        formMap.put("size","10");
        formMap.put("clazz","法术");

         /*
         * form 还有其他的检索规则 表单如下
         * "series","48,49" 这个是系列的检索，48和49是系列的标记，这里选择了两个系列
         * "manaNormal","5" 这个是费用消耗
         * "faction","Hunter" 职业英雄检索,这里选择了猎人
         * "name_rule","战吼" 规则检索
         * "name","法术卷积者" 卡名检索
         * "wild","1" 赛制检索：狂野
         * "standard","1" 赛制检索：标准
         * "rarity","稀有" 稀有度检索
         * "manaMore","9" 费用为9+时用的检索
         * "clazz","法术" 卡牌类别
         * */

        IYingDi service = NetWorkManager.getInstances().initRetrofit(IYingDi.API2_URL).create(IYingDi.class);
        service.getHearthStoneCard(headerMap,formMap).enqueue(new Callback<YDMsg<YDCardData<List<HSCard>>>>() {
            @Override
            public void onResponse(Call<YDMsg<YDCardData<List<HSCard>>>> call, Response<YDMsg<YDCardData<List<HSCard>>>> response) {
                if (response.isSuccessful()){
                    if (response.body().isSuccess()){
                        for (HSCard card :
                                response.body().getData().getCards()) {
                            Log.i(TAG, "cards: " + card.toString());
                        }
                    }
                }

                NetWorkManager.setNull();
            }

            @Override
            public void onFailure(Call<YDMsg<YDCardData<List<HSCard>>>> call, Throwable t) {
                NetWorkManager.setNull();
            }
        });
    }

    private void tryRetrofitWithRxJava_getHSDeckList(){
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put("token","");
        queryMap.put("page","0");
        queryMap.put("size","7");
        queryMap.put("total","1");
        queryMap.put("format","狂野");
        queryMap.put("name","开门");

        /*
        * "order","hot/time" 按热度和最新时间来检索
        * "faction","Mage" 按职业检索
        * "name","开门" 按字段检索
        * */
        
        NetWorkManager.getInstances().initRetrofitWithRxJava(IYingDi.API2_URL)
                .create(IYingDi.class)
                .getHearthStoneDeckList(queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YDMsg<List<YDDeskData<HSDeck>>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        
                    }

                    @Override
                    public void onNext(@NonNull YDMsg<List<YDDeskData<HSDeck>>> listYDMsg) {
                        if (listYDMsg != null && listYDMsg.isSuccess()){
                            for (YDDeskData<HSDeck> deckItem :
                                    listYDMsg.getData()) {
                                Log.i(TAG, "onNext: " + deckItem.toString());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        NetWorkManager.setNull();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(String s){
        ToastUtils.make().setDurationIsLong(true).show(TAG + ": " + s);
        Log.i(TAG, "onReceiveMsg: " + s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
