package com.smart.notebook.ui.home.contract;


import android.content.Context;
import android.view.animation.Animation;


public interface SplashContract {
    interface View {
        void animateBackgroundImage(Animation animation);

        void initViews(String versionName, String copyright, int backgroundResId);

        void toHomePage();
    }

    interface Presenter {
        void initialized();

        int getBackgroundImageResID();

        String getCopyright(Context context);

        String getVersionName(Context context);

        Animation getBackgroundImageAnimation(Context context);
    }
}
