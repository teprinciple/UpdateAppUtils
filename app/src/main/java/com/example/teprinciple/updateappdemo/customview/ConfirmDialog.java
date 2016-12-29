package com.example.teprinciple.updateappdemo.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.teprinciple.updateappdemo.R;
import com.example.teprinciple.updateappdemo.customview.feature.Callback;


/**
 * Created by Teprinciple on 2016/10/13.
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {

    Callback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    public ConfirmDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        sureBtn = (TextView)mView.findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView)mView.findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);
        sureBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        super.setContentView(mView);
    }


    public ConfirmDialog setContent(String s){
        content.setText(s);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_confirm_cancle:
                this.cancel();
                break;

            case R.id.dialog_confirm_sure:
                callback.callback();
                this.cancel();
                break;
        }
    }

}
