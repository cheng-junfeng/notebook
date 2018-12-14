package com.smart.notebook.ui.home.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.google.gson.Gson;
import com.smart.notebook.R;
import com.smart.notebook.app.adapter.BaseDelegateAdapter;
import com.smart.notebook.app.base.BaseFragment;
import com.smart.notebook.db.entity.DataEntity;
import com.smart.notebook.db.helper.DataHelper;
import com.smart.notebook.ui.home.contract.HomeFragmentContract;
import com.smart.notebook.ui.home.presenter.HomeFragmentPresenter;
import com.smart.notebook.ui.qrcode.CaptureActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HomeFragmentContract.Presenter presenter;
    private List<DelegateAdapter.Adapter> mAdapters;

    DelegateAdapter delegateAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home;
    }

    protected void init() {
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        presenter = new HomeFragmentPresenter(this.getActivity());
        delegateAdapter = presenter.initRecyclerView(recyclerView);
        BaseDelegateAdapter bannerAdapter = presenter.initMenu1();
        mAdapters.add(bannerAdapter);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
    }

    @OnClick({R.id.scan_input, R.id.handle_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_input:
                readyGo(CaptureActivity.class);
                break;
            case R.id.handle_input:
                DataHelper mHelper = DataHelper.getInstance();
                List<DataEntity> allEntity = mHelper.queryList();
                StringBuffer content = new StringBuffer();
                if (allEntity != null && allEntity.size() > 0) {
                    String json = new Gson().toJson(allEntity);
                    content.append(json);
                } else {
                    content.append("null");
                }

                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, content.toString());
                startActivity(Intent.createChooser(textIntent, "分享"));
                break;
        }
    }
}
