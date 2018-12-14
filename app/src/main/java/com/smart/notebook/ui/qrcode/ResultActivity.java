package com.smart.notebook.ui.qrcode;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.custom.view.MultiEditInputView;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.library.event.RxBusHelper;
import com.smart.notebook.R;
import com.smart.notebook.app.base.BaseSwipeBackActivity;
import com.smart.notebook.db.entity.DataEntity;
import com.smart.notebook.db.helper.DataHelper;
import com.smart.notebook.event.bean.MsgEvent;
import com.smart.notebook.event.config.MsgType;
import com.smart.notebook.ui.other.photo.PhotoBrowserActivity;
import com.smart.notebook.ui.qrcode.decode.DecodeThread;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class ResultActivity extends BaseSwipeBackActivity {

    public static final String BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME";
    public static final String BUNDLE_KEY_SCAN_RESULT = "BUNDLE_KEY_SCAN_RESULT";
    public static final String BUNDLE_KEY_DESCRIP = "BUNDLE_KEY_DESCRIP";

    @BindView(R.id.result_image)
    ImageView resultImage;
    @BindView(R.id.result_time)
    TextView resultTime;
    @BindView(R.id.result_content)
    TextView resultContent;
    @BindView(R.id.save_descrip)
    MultiEditInputView saveDescrip;

    private Bitmap mBitmap;
    private String mResultStr;
    private String currentTime;
    private String currentDescip;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_result;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        if (extras != null) {
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                mBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            currentTime = extras.getString(BUNDLE_KEY_TIME, new Date().toLocaleString());
            mResultStr = extras.getString(BUNDLE_KEY_SCAN_RESULT, "");
            currentDescip = extras.getString(BUNDLE_KEY_DESCRIP, "");
        }
    }

    @Override
    protected void init() {
        setTitle("扫码详情");

        if (null != mBitmap) {
            resultImage.setImageBitmap(mBitmap);
        }
        resultTime.setText(currentTime);
        saveDescrip.setContentText(currentDescip);

        resultContent.setText(mResultStr);
        resultContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(mResultStr);
                ToastUtils.showToast(getApplicationContext(), "已复制到剪切板");
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @OnClick({R.id.result_image, R.id.save_button})
    public void onViewClicked(View view) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        switch (view.getId()) {
            case R.id.result_image:
                Bundle bundle = new Bundle();
                bundle.putByteArray(DecodeThread.BARCODE_BITMAP, baos.toByteArray());
                readyGo(PhotoBrowserActivity.class, bundle);
                break;
            case R.id.save_button:
                DataHelper mHelper = DataHelper.getInstance();
                String descrip = saveDescrip.getContentText();

                DataEntity tempEntity = new DataEntity();
                tempEntity.data_lasttime = currentTime;
                tempEntity.data_name = mResultStr;
                tempEntity.data_url = descrip;
                tempEntity.iconUrl = baos.toByteArray();
                boolean result = mHelper.insert(tempEntity);
                if (result) {
                    DialogUtils.showConfirmAlertDialog(mContext, "保存成功", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RxBusHelper.post(new MsgEvent.Builder(MsgType.UPDATE).build());
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showToast(mContext, "保存失败，请退出重试");
                }
                break;
        }
    }
}
