package com.lyd.tooltest.UI.Fragment;

import android.view.View;
import android.widget.Button;

import com.lyd.tooltest.Base.BaseFragment;
import com.lyd.tooltest.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "EventBusFragment";

    Button btn_send;



    @Override
    protected void initViews() {

        btn_send = find(R.id.eve_btn_send);

        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eve_btn_send:
                EventBus.getDefault().post(TAG + " say hello !");
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_eventbus;
    }
}
