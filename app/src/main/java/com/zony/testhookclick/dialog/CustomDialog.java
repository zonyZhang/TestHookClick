package com.zony.testhookclick.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zony.testhookclick.R;

public abstract class CustomDialog extends BaseDialog implements android.view.View.OnClickListener {
    private TextView tvClick;
    private Button btnClick;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
        this.setTitle("Test Dialog Hook Click");
        tvClick = (TextView) findViewById(R.id.text_click);
        btnClick = (Button) findViewById(R.id.btn_click);
        tvClick.setOnClickListener(this);
        btnClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "" + v.getId(), Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.text_click:
                textClick();
                break;
            case R.id.btn_click:
                btnClick();
                break;
        }
    }

    public abstract void textClick();

    public abstract void btnClick();
}