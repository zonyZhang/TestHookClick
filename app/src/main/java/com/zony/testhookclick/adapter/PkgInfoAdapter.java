package com.zony.testhookclick.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zony.testhookclick.R;
import com.zony.testhookclick.bean.AppInfo;

import java.util.List;

/**
 * Created by zony on 17-5-18.
 */

public class PkgInfoAdapter extends RecyclerView.Adapter {
    private List<AppInfo> list;

    private Context mContext;

    public PkgInfoAdapter(List<AppInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_appinfo, null);
        AppInfoViewHolder appInfoViewHolder = new AppInfoViewHolder(view);
        return appInfoViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppInfo mAppInfo = list.get(position);
        AppInfoViewHolder appInfoViewHolder = (AppInfoViewHolder) holder;
        appInfoViewHolder.appIcon.setImageDrawable(mAppInfo.getAppIcon());
        appInfoViewHolder.tvAppLabel.setText(mAppInfo.getAppLabel());
        appInfoViewHolder.tvAppName.setText(mAppInfo.getAppName());
        appInfoViewHolder.tvAppPath.setText(mAppInfo.getAppPath());
        appInfoViewHolder.tvAppSize.setText(mAppInfo.getAppSize() + "");
        appInfoViewHolder.tvAppStyle.setText(mAppInfo.getAppStyle());
        appInfoViewHolder.tvAppIntent.setText(mAppInfo.getIntent().toString());
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    class AppInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView appIcon;
        private TextView tvAppLabel, tvAppName, tvAppPath, tvAppSize, tvAppStyle, tvAppIntent;

        public AppInfoViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            tvAppLabel = (TextView) itemView.findViewById(R.id.tv_label_value);
            tvAppName = (TextView) itemView.findViewById(R.id.tv_app_name_value);
            tvAppPath = (TextView) itemView.findViewById(R.id.tv_app_path_value);
            tvAppSize = (TextView) itemView.findViewById(R.id.tv_app_size_value);
            tvAppStyle = (TextView) itemView.findViewById(R.id.tv_app_type_value);
            tvAppIntent = (TextView) itemView.findViewById(R.id.tv_app_intent_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(mContext, "click position is " + position, Toast.LENGTH_SHORT).show();
        }
    }

}
