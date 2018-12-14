package com.smart.notebook.ui.other.photo;

import android.os.Bundle;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.smart.notebook.R;
import com.smart.notebook.app.base.BaseSwipeBackActivity;
import com.smart.notebook.ui.qrcode.decode.DecodeThread;

import butterknife.BindView;


public class PhotoBrowserActivity extends BaseSwipeBackActivity {

    @BindView(R.id.li_image)
    PhotoView liImage;

    byte[] compressedBitmap;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_photo_browser;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        if (extras != null) {
            compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
        }
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {
        if(compressedBitmap != null && compressedBitmap.length > 0){
            liImage.enable();
            liImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(mContext).load(compressedBitmap).into(liImage);
        }
    }
}
