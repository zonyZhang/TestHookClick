package com.zony.testhookclick.utils;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 劫持view点击事件(反射+静态代理)
 *
 * @param
 * @author zony
 * @time 17-8-30 上午11:41
 */
public class HookViewClick {
    private static final String TAG = "HookViewClick";

    private int mPrivateTagKey = System.identityHashCode(TAG);

    private Method hookMethod;

    private Field hookField;

    private static HookViewClick mHookInstance;

    public HookViewClick() {
        initHook();
    }

    public static HookViewClick getHookInstance() {
        if (mHookInstance == null) {
            mHookInstance = new HookViewClick();
        }
        return mHookInstance;
    }

    private void initHook() {
        if (hookMethod == null) {
            try {
                Class viewClazz = Class.forName("android.view.View");
                if (viewClazz != null) {
                    hookMethod = viewClazz.getDeclaredMethod("getListenerInfo");// 获取getListenerInfo事件监听器实例
                    if (hookMethod != null) {
                        hookMethod.setAccessible(true);// 改变访问对象的可见性，常常用来访问private属性的对象。
                    }
                }
                Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");// 通过类名获取Class对象
                if (listenerInfoClazz != null) {
                    hookField = listenerInfoClazz.getDeclaredField("mOnClickListener");
                    if (hookField != null) {
                        hookField.setAccessible(true);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "HookViewClick initHook HOOK EXCEPTION: " + e.toString());
            }
        }
    }

    /**
     * hook all view
     *
     * @param
     * @author zony
     * @time 17-8-30 下午1:38
     */
    public void hookAllViews(View view) {
        if (view == null) {
            Log.w(TAG, "HookViewClickUtil hookAllViews view is null !");
            return;
        }

        if (view instanceof ViewGroup) {
            hookViews(view, 0);
        } else if (view instanceof View) {
            hookViews(view, 1);
        } else {
            Log.w(TAG, "HookViewClickUtil getAllChildViews view is invalid !");
        }
    }

    private void hookViews(View view, int recycledContainerDeep) {
        if (view.getVisibility() == View.VISIBLE) {
            boolean forceHook = recycledContainerDeep == 1;
            if (view instanceof ViewGroup) {
                boolean existAncestorRecycle = recycledContainerDeep > 0;
                ViewGroup p = (ViewGroup) view;
                if (!(p instanceof AbsListView || p instanceof RecyclerView)
                        || existAncestorRecycle) {
                    hookClickListener(view, recycledContainerDeep, forceHook);
                    if (existAncestorRecycle) {
                        recycledContainerDeep++;
                    }
                } else {
                    recycledContainerDeep = 1;
                }
                for (int i = 0; i < p.getChildCount(); i++) {
                    View child = p.getChildAt(i);
                    hookViews(child, recycledContainerDeep);
                }
            } else {
                hookClickListener(view, recycledContainerDeep, forceHook);
            }
        }
    }

    private void hookClickListener(View view, int recycledContainerDeep, boolean forceHook) {
        boolean needHook = forceHook;
        if (!needHook) {
            needHook = view.isClickable();
            if (needHook && recycledContainerDeep == 0) {
                needHook = view.getTag(mPrivateTagKey) == null;
            }
        }
        if (needHook) {
            try {
                Object listenerInfoObj = hookMethod.invoke(view);// 通过对象和参数列表执行方法
                View.OnClickListener baseClickListener = listenerInfoObj == null ? null
                        : (View.OnClickListener) hookField.get(listenerInfoObj);// 获取已设置过的监听器
                if ((baseClickListener != null
                        && !(baseClickListener instanceof OnClickListenerProxy))) {

                    // 自定义代理事件监听器
                    View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(
                            baseClickListener);
                    // 设置生效
                    hookField.set(listenerInfoObj, onClickListenerProxy);
                    view.setTag(mPrivateTagKey, recycledContainerDeep);
                }
            } catch (Exception e) {
                Log.w(TAG, "HookViewClick hookClickListener HOOK EXCEPTION: " + e.toString());
            }
        }
    }

    /**
     * 自定义的代理事件监听器
     *
     * @param
     * @author zony
     * @time 17-8-30 下午12:25
     */
    private class OnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener object;

        private int MIN_CLICK_DELAY_TIME = 500;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            // 点击时间控制
            long currentTime = SystemClock.uptimeMillis();
            Log.i(TAG, "HookViewClickUtil OnClickListenerProxy currentTime - lastClickTime:"
                    + currentTime + " - " + lastClickTime + "=" + (currentTime - lastClickTime));
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                if (object != null) {
                    Log.i(TAG, "HookViewClickUtil OnClickListenerProxy view :" + v.toString());
                    object.onClick(v);
                }
            }
        }
    }
}
