package com.zony.testhookclick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.zony.testhookclick.adapter.PkgInfoAdapter;
import com.zony.testhookclick.dialog.CustomDialog;
import com.zony.testhookclick.utils.CommonUtil;

public class MainActivity extends BaseActivity {

    private static final String TAG = "zony";

    private RecyclerView recyclerView;

    private PkgInfoAdapter pkgInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pkgInfoAdapter = new PkgInfoAdapter(CommonUtil.getAllApk(this));
        recyclerView.setAdapter(pkgInfoAdapter);
        recyclerView.addItemDecoration(new MyDecoration(this));
    }



    public void showDialog(View view) {
        Log.i(TAG, "showDialog");
        new CustomDialog(this) {
            @Override
            public void textClick() {

            }

            @Override
            public void btnClick() {

            }
        }.show();
    }

    class MyDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        private Drawable mDivider;

        public MyDecoration(Context ctx) {
            final TypedArray a = ctx.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                //以下计算主要用来确定绘制的位置
                int top = child.getBottom() + layoutParams.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth());
        }

    }
}
