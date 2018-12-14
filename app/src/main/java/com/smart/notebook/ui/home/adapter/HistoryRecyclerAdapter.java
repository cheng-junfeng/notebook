package com.smart.notebook.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.library.listener.OnClickLongListener;
import com.smart.notebook.R;
import com.smart.notebook.db.entity.DataEntity;

import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<DataEntity> datas;
    private LayoutInflater inflater;
    private OnClickLongListener mListener;

    public HistoryRecyclerAdapter(Context mainActivity, List<DataEntity> data) {
        this.context = mainActivity;
        this.datas = data;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<DataEntity> data) {
        this.datas = data;
    }

    public void setOnItemListener(OnClickLongListener mOnItemClickListener) {
        this.mListener = mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history, parent, false);
        ViewHoders viewHoders = new ViewHoders(view);
        return viewHoders;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        DataEntity bean = datas.get(position);
        ((ViewHoders) holder).li_count.setText((position+1)+"");
        ((ViewHoders) holder).time_tv.setText(empty(bean.getData_lasttime()));
        ((ViewHoders) holder).content_tv.setText(empty(bean.getData_name()));
        ((ViewHoders) holder).descrp_tv.setText(empty(bean.getData_url()));
        byte[] icon = bean.getIconUrl();
        if(icon != null && icon.length > 0) {
            Glide.with(context).load(bean.getIconUrl()).into(((ViewHoders) holder).li_image);
        }else{
            Glide.with(context).load(R.mipmap.default_error).into(((ViewHoders) holder).li_image);
        }

        if (mListener != null) {
            //直接给某个空间添加监听
            ((ViewHoders) holder).li_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(position);
                    }
                }
            });
            ((ViewHoders) holder).li_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener != null) {
                        mListener.onItemLongClick(position);
                    }
                    return true;
                }
            });
        }
    }

    private String empty(String oldStr){
        if(TextUtils.isEmpty(oldStr)){
            return "无";
        }else{
            return oldStr;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public DataEntity getItem(int position) {
        return datas.get(position);
    }

    class ViewHoders extends RecyclerView.ViewHolder {
        private TextView li_count, time_tv, content_tv, descrp_tv;
        private LinearLayout li_layout;
        private ImageView li_image;

        public ViewHoders(View itemView) {
            super(itemView);
            li_count = itemView.findViewById(R.id.li_count);
            time_tv = itemView.findViewById(R.id.time_view);
            content_tv = itemView.findViewById(R.id.content_view);
            descrp_tv = itemView.findViewById(R.id.descrp_view);
            li_layout = itemView.findViewById(R.id.li_layout);
            li_image = itemView.findViewById(R.id.li_image);
        }
    }
}
