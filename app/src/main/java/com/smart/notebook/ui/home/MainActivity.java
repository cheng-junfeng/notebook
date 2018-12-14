package com.smart.notebook.ui.home;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.hint.utils.ToastUtils;
import com.smart.notebook.R;
import com.smart.notebook.app.base.BaseActivity;
import com.smart.notebook.ui.home.adapter.ViewPagerAdapter;
import com.smart.notebook.ui.home.fragment.AboutUsFragment;
import com.smart.notebook.ui.home.fragment.HistoryFragment;
import com.smart.notebook.ui.home.fragment.HomeFragment;
import com.smart.notebook.ui.qrcode.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static long DOUBLE_CLICK_TIME = 0L;
    public static final String TAG = "DesignActivity";

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_msg)
    RadioButton rbMsg;
    @BindView(R.id.rb_my)
    RadioButton rbMy;

    private int normalColor;
    private int selectColor;

    int pre_index = -1;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_control;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.NULL;
    }

    @Override
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);// 去掉默认的返回箭头
        normalColor = getResources().getColor(R.color.tab_host_text_normal);
        selectColor = getResources().getColor(R.color.tab_host_text_select);

        initViewPager();
        initRadio();
        setTabSelect(0);
    }

    HomeFragment homeFragment;
    HistoryFragment historyFragment;
    AboutUsFragment meFragment;
    Fragment[] mFragments;

    // 初始化数据
    private void initViewPager() {
        homeFragment = new HomeFragment();
        historyFragment = new HistoryFragment();
        meFragment = new AboutUsFragment();
        mFragments = new Fragment[]{homeFragment, historyFragment, meFragment};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpMain.setAdapter(adapter);
        vpMain.addOnPageChangeListener(this);
        vpMain.setOffscreenPageLimit(3);
    }

    RadioButton[] radioButtons;

    private void initRadio() {
        radioButtons = new RadioButton[3];
        radioButtons[0] = rbHome;
        radioButtons[1] = rbMsg;
        radioButtons[2] = rbMy;
        radioButtons[0].setChecked(true);
        for (int i = 0; i < radioButtons.length; i++) {
            Drawable[] drawables = radioButtons[i].getCompoundDrawables();
            Rect r = new Rect(0, 0, drawables[1].getMinimumWidth() * 2 / 5, drawables[1].getMinimumHeight() * 2 / 5);
            drawables[1].setBounds(r);
            radioButtons[i].setCompoundDrawables(null, drawables[1], null, null);
        }
    }

    @OnClick({R.id.rb_home, R.id.rb_msg, R.id.rb_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_home:
                setTabSelect(0);
                break;
            case R.id.rb_msg:
                setTabSelect(1);
                break;
            case R.id.rb_my:
                setTabSelect(2);
                break;
        }
    }

    private void setTabSelect(int position) {
        if (position == pre_index) {
            return;
        }
        switch (position) {
            case 0:
                setTitle("首页", MAIN_TOOLBAR);
                break;
            case 1:
                setTitle("历史", MAIN_TOOLBAR);
                break;
            case 2:
                setTitle("我的", MAIN_TOOLBAR);
                break;
        }
        if (pre_index >= 0) {
            radioButtons[pre_index].setChecked(false);
            radioButtons[pre_index].setTextColor(normalColor);
        }

        vpMain.setCurrentItem(position);
        radioButtons[position].setChecked(true);
        radioButtons[position].setTextColor(selectColor);
        pre_index = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setTabSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                readyGo(CaptureActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                ToastUtils.showToast(mContext, getString(R.string.double_click_exit));
                DOUBLE_CLICK_TIME = System.currentTimeMillis();
            } else {
                getBaseApplication().exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
