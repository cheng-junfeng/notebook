package com.smart.notebook.ui.home.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.smart.notebook.R;
import com.smart.notebook.app.adapter.BaseDelegateAdapter;
import com.smart.notebook.app.adapter.BaseViewHolder;
import com.smart.notebook.ui.home.contract.HomeFragmentContract;
import com.smart.notebook.ui.other.webview.WebViewX5Activity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentPresenter implements HomeFragmentContract.Presenter {

    private Context mContext;

    public HomeFragmentPresenter(Context activity) {
        this.mContext = activity;
    }

    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public BaseDelegateAdapter initMenu1() {
        //menu
        // 在构造函数设置每行的网格个数
        final TypedArray proPic = mContext.getResources().obtainTypedArray(R.array.find_gv_image1);
        final String[] proName = mContext.getResources().getStringArray(R.array.find_gv_title1);
        final List<Integer> images = new ArrayList<>();
        for (int a = 0; a < proName.length; a++) {
            images.add(proPic.getResourceId(a, R.drawable.light_checked));
        }
        proPic.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 16);
        gridLayoutHelper.setVGap(16);   // 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);    // 控制子元素之间的水平间距
        gridLayoutHelper.setBgColor(Color.WHITE);
        return new BaseDelegateAdapter(mContext, gridLayoutHelper, R.layout.item_home, 1, 2) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.hm_content, proName[position]);
                holder.setImageResource(R.id.hm_image, images.get(position));
                holder.getView(R.id.hm_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        switch (position) {
                            case 0:
                                intent = new Intent(mContext, WebViewX5Activity.class);
                                mContext.startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        };
    }
}
