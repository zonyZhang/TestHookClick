# TestHookClick
hook android view clicklistener

劫持Android中所有view的点击事件

用途：

        1、控制点击时间（快速点击的时间控制）

        2、所有的点击事件进行计数

实现原理：反射+静态代理

hook 时机： Activity基类或者Dialog基类中 dispatchTouchEvent --> ACTION_DOWN

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "dispatchTouchEvent ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent ACTION_DOWN");
                HookViewClickUtil.hookAllView(this);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
