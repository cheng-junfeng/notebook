package com.smart.notebook.ui.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.notebook.R;
import com.smart.notebook.app.base.BaseActivity;
import com.smart.notebook.ui.home.contract.SplashContract;
import com.smart.notebook.ui.home.presenter.SplashPresenter;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @BindView(R.id.splash_image)
    ImageView mSplashImage;
    @BindView(R.id.splash_version_name)
    TextView mVersionName;
    @BindView(R.id.splash_copyright)
    TextView mCopyright;

    private SplashContract.Presenter mSplashPresenter = null;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected boolean getSystemBarTintDrawable() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.NULL;
    }

    @Override
    protected void init() {
        mSplashPresenter = new SplashPresenter(this, this);
        mSplashPresenter.initialized();
    }

    @Override
    public void animateBackgroundImage(Animation animation) {
        mSplashImage.startAnimation(animation);
    }

    @Override
    public void initViews(String versionName, String copyright, int backgroundResId) {
        mCopyright.setText(copyright);
        mVersionName.setText(versionName);
        mSplashImage.setImageResource(backgroundResId);
    }

    @Override
    public void toHomePage() {
        readyGoThenKill(MainActivity.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
