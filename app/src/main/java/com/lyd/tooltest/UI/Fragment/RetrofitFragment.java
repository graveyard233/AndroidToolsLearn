package com.lyd.tooltest.UI.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lyd.tooltest.Base.BaseFragment;
import com.lyd.tooltest.Entity.WanAndroid.WanHomeBanner;
import com.lyd.tooltest.Entity.WanAndroid.WanMsg;
import com.lyd.tooltest.InterFace.IWanAndroid;
import com.lyd.tooltest.NetWork.NetWorkManager;
import com.lyd.tooltest.R;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
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
                tryRetrofit();
                break;
            case R.id.ret_btn_rxJava:
                tryRetrofitWithRxJava();
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
}
