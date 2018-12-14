package com.smart.notebook.ui.home.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.smart.notebook.R;
import com.smart.notebook.ui.home.contract.SplashContract;

public class SplashPresenter implements SplashContract.Presenter {

    private Context mContext = null;
    private SplashContract.View mSplashView = null;

    public SplashPresenter(Context context, SplashContract.View splashView) {
        if (null == splashView) {
            throw new IllegalArgumentException("Constructor's parameters must not be Null");
        }

        mContext = context;
        mSplashView = splashView;
    }

    @Override
    public void initialized() {
        mSplashView.initViews(getVersionName(mContext), getCopyright(mContext),
                getBackgroundImageResID());

        Animation animation = getBackgroundImageAnimation(mContext);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSplashView.toHomePage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSplashView.animateBackgroundImage(animation);
    }

    @Override
    public Animation getBackgroundImageAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.splash);
    }

    @Override
    public int getBackgroundImageResID() {
        return R.drawable.morning;
    }

    @Override
    public String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.format(context.getResources().getString(R.string.splash_version), versionName);
    }

    @Override
    public String getCopyright(Context context) {
        return context.getResources().getString(R.string.splash_copyright);
    }
}
