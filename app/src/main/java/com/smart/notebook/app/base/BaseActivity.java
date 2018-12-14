package com.smart.notebook.app.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.library.base.BaseAppCompatActivity;
import com.library.utils.ToolbarUtil;
import com.smart.notebook.BaseApplication;
import com.smart.notebook.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends BaseAppCompatActivity {
    protected final static int COMM_TOOLBAR = 0;
    protected final static int MAIN_TOOLBAR = 1;
    protected final static int SINGLE_TOOLBAR = 2;

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSystemBarTintDrawable()) {
            setSystemBarTintDrawable(getResources().getDrawable(R.drawable.sr_primary));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    protected void setTitle(String title) {
        setTitle(title, COMM_TOOLBAR);
    }

    protected void setTitle(String title, int type) {
        if (mToolbar != null) {
            switch (type) {
                case COMM_TOOLBAR: {
                    ToolbarUtil.setToolbar(mToolbar, title, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }
                break;
                case MAIN_TOOLBAR: {
                    ToolbarUtil.setToolbar(mToolbar, "", title, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }
                break;
                case SINGLE_TOOLBAR: {
                    ToolbarUtil.setToolbar(mToolbar, title, null);
                }
                break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }

    protected boolean getSystemBarTintDrawable() {
        return true;
    }
}