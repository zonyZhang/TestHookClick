package com.zony.testhookclick.utils;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 劫持view点击事件(反射+静态代理)
 *
 * @param
 * @author zony
 * @time 17-8-30 上午11:41
 */
public class HookViewClickUtil {
    private static final String TAG = "HookViewClickUtil";

    private static int mPrivateTagKey = System.identityHashCode(TAG);

    /**
     * hook all view
     *
     * @param
     * @author zony
     * @time 17-8-30 下午1:38
     */
    public static void hookAllViews(View view) {
        if (view == null) {
            Log.w(TAG, "HookViewClickUtil hookAllViews view is null !");
            return;
        }

        if (view instanceof ViewGroup) {
            List<View> allChildrens = getAllChildViews(view);
            for (View childView : allChildrens) {
                hookViews(childView, 0);
            }
        } else if (view instanceof View) {
            hookViews(view, 0);
        } else {
            Log.w(TAG, "HookViewClickUtil getAllChildViews view is invalid !");
        }
    }

    /**
     * 获取所有子VIEW或view对象
     *
     * @param
     * @author zony
     * @time 17-8-30 上午11:35
     */
    private static List<View> getAllChildViews(View view) {
        List<View> allChildrens = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View viewChild = viewGroup.getChildAt(i);
                allChildrens.add(viewChild);
                allChildrens.addAll(getAllChildViews(viewChild));
            }
        }
        return allChildrens;
    }

    /**
     * 自定义的代理事件监听器
     *
     * @param
     * @author zony
     * @time 17-8-30 下午12:25
     */
    private static class OnClickListenerProxy implements View.OnClickListener {

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

    /**
     * install proxy click listener in a recursive function
     *
     * @param view                  root view .
     * @param recycledContainerDeep view hierarchy level
     */
    private static void hookViews(View view, int recycledContainerDeep) {
        if (view.getVisibility() == View.VISIBLE) {
            boolean forceHook = recycledContainerDeep == 1;
            if (view instanceof ViewGroup) {
                boolean existAncestorRecycle = recycledContainerDeep > 0;
                hookClickListener(view, recycledContainerDeep, forceHook);
                if (existAncestorRecycle) {
                    recycledContainerDeep++;
                }
            } else {
                hookClickListener(view, recycledContainerDeep, forceHook);
            }
        }
    }

    /**
     * hook original click listener of a single view.
     *
     * @param forceHook force hook View if it is direct child of some AbsListView 。
     */
    private static void hookClickListener(View view, int recycledContainerDeep, boolean forceHook) {
        boolean needHook = forceHook;
        if (!needHook) {
            needHook = view.isClickable();
            if (needHook && recycledContainerDeep == 0) {
                needHook = view.getTag(mPrivateTagKey) == null;
            }
        }
        if (needHook) {
            try {
                // 获取getListenerInfo事件监听器实例
                Method listenerInfoMethod = Class.forName("android.view.View")
                        .getDeclaredMethod("getListenerInfo");
                listenerInfoMethod.setAccessible(true);// 改变访问对象的可见性，常常用来访问private属性的对象。
                Object listenerInfoObj = listenerInfoMethod.invoke(view);// 通过对象和参数列表执行方法
                Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");// 通过类名获取Class对象

                Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
                onClickListenerField.setAccessible(true);
                View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField
                        .get(listenerInfoObj);

                // 自定义代理事件监听器
                View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(
                        mOnClickListener);

                // 设置生效
                onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
                view.setTag(mPrivateTagKey, recycledContainerDeep);
            } catch (Exception e) {
                Log.w(TAG, "HOOK EXCEPTION: " + e.toString());
            }
        }
    }
}
