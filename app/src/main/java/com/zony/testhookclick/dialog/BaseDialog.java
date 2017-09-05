package com.zony.testhookclick.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.MotionEvent;

import com.zony.testhookclick.utils.HookViewClickUtil;

public class BaseDialog extends Dialog {
    private static final String TAG = "zony";


    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "dispatchTouchEvent ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent ACTION_DOWN");
                HookViewClickUtil.hookAllViews(getWindow().getDecorView());
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}