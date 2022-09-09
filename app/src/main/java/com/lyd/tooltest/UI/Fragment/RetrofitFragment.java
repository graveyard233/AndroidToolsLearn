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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "RetrofitFragment";

    Button btn_pure;

    @Override
    protected void initViews() {
        btn_pure = find(R.id.ret_btn_pure);

        btn_pure.setOnClickListener(this);
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
            default:break;
        }
    }

    private void tryRetrofit() {
        IWanAndroid service = NetWorkManager.getInstances().initRetrofit().create(IWanAndroid.class);
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
                }
            }

            @Override
            public void onFailure(Call<WanMsg<List<WanHomeBanner>>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }
}
