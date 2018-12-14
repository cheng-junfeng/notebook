package com.hint.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hint.R;

public class ProgressUtils {
    private static HintLoadingDialog loadingDialog = null;

    public static void showLoading(Context context) {
        loadingDialog = new HintLoadingDialog(context, 0, "");
        if (null != loadingDialog && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public static void showLoading(Context context, String msg) {
        loadingDialog = new HintLoadingDialog(context, 1, msg);
        if (null != loadingDialog && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public static void dismissLoading() {
        if (null != loadingDialog) {
            if (loadingDialog.isShowing()) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }

    private static Dialog progressdialog = null;

    public static void showProgressDialog(Context context) {
        dismissProgressDialog();
        if (null == progressdialog) {
            progressdialog = new Dialog(context, R.style.Loading);
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
            progressdialog.setContentView(view);
        }
        if (null != progressdialog && !progressdialog.isShowing()) {
            progressdialog.show();
            Window window = progressdialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showProgressDialog(Context context, String msg) {
        dismissProgressDialog();
        if (null == progressdialog) {
            progressdialog = new Dialog(context, R.style.Loading);
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_msg, null);
            TextView tipsTv = view.findViewById(R.id.tv_tips);
            progressdialog.setContentView(view);
            tipsTv.setText(msg);
        }
        if (null != progressdialog && !progressdialog.isShowing()) {
            progressdialog.show();
            Window window = progressdialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void dismissProgressDialog() {
        if (null != progressdialog) {
            if (progressdialog.isShowing()) {
                progressdialog.cancel();
                progressdialog.dismiss();
            }
            progressdialog = null;
        }
    }
}
