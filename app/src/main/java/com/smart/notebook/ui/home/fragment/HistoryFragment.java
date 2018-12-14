package com.smart.notebook.ui.home.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.hint.listener.OnChooseListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.library.event.RxBusHelper;
import com.library.listener.OnClickLongListener;
import com.library.utils.TLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smart.notebook.R;
import com.smart.notebook.app.base.BaseFragment;
import com.smart.notebook.db.entity.DataEntity;
import com.smart.notebook.db.helper.DataHelper;
import com.smart.notebook.event.bean.MsgEvent;
import com.smart.notebook.event.config.MsgType;
import com.smart.notebook.ui.home.adapter.HistoryRecyclerAdapter;
import com.smart.notebook.ui.qrcode.ResultActivity;
import com.smart.notebook.ui.qrcode.decode.DecodeThread;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HistoryFragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.data_main)
    RecyclerView recyclerView;
    @BindView(R.id.view_empty)
    FrameLayout viewEmpty;

    private DataHelper mHelper;
    private HistoryRecyclerAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_history;
    }

    @Override
    protected void init() {
        mHelper = DataHelper.getInstance();
        RxBusHelper.doOnMainThread(this, MsgEvent.class, new RxBusHelper.OnEventListener<MsgEvent>() {
            @Override
            public void onEvent(MsgEvent msgEvent) {
                if (msgEvent.getType() == MsgType.UPDATE) {
                    TLog.d(TAG, "doOnMainThread:onEvent");
                    initData();
                }
            }
        });

        initData();
    }

    private void initData() {
        List<DataEntity> allEntity = mHelper.queryList();
        if (allEntity == null || allEntity.size() == 0) {
            viewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }else{
            viewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new HistoryRecyclerAdapter(mContext, allEntity);
            mAdapter.setOnItemListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    final DataEntity entity = mAdapter.getItem(position);

                    Bundle bundle = new Bundle();
                    bundle.putByteArray(DecodeThread.BARCODE_BITMAP, entity.iconUrl);
                    bundle.putString(ResultActivity.BUNDLE_KEY_TIME, entity.data_lasttime);
                    bundle.putString(ResultActivity.BUNDLE_KEY_SCAN_RESULT, entity.data_name);
                    bundle.putString(ResultActivity.BUNDLE_KEY_DESCRIP, entity.data_url);
                    readyGo(ResultActivity.class, bundle);
                }

                @Override
                public void onItemLongClick(final int position) {
                    List<String> allStr = new ArrayList<>();
                    allStr.add("删除");
                    allStr.add("复制到剪切板");
                    DialogUtils.showChooseDialog(mContext, allStr, new OnChooseListener() {
                        @Override
                        public void onPositive(int pos) {
                            final DataEntity entity = mAdapter.getItem(position);
                            switch (pos){
                                case 0:
                                    mHelper.delete(entity);
                                    RxBusHelper.post(new MsgEvent.Builder(MsgType.UPDATE).build());
                                    break;
                                case 1:
                                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setText(new Gson().toJson(entity));
                                    ToastUtils.showToast(mContext, "已复制内容到剪切板");
                                    break;
                            }
                        }
                    });
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(divider);
            recyclerView.setAdapter(mAdapter);

            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    initData();
                    refreshLayout.finishRefresh(2000);
                }
            });
            refreshLayout.setEnableLoadMore(false);
        } else {
            mAdapter.setData(allEntity);
            mAdapter.notifyDataSetChanged();
        }
    }
}
