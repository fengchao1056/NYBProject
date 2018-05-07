package com.myzyb2.appNYB2.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.util.LogUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 个人中心——头像上传
 */

public class UploadPhotoActivity extends BaseActivity implements View.OnClickListener {

    private static final String PHOTOPATH = "/photo/";
    private Context context;
    final String start = Environment.getExternalStorageState();
    @Bind(R.id.tv_photograph)
    TextView tvPhotograph;
    @Bind(R.id.tv_localupload)
    TextView tvLocalupload;
    @Bind(R.id.bt_cancel)
    Button btCancel;
    @Bind(R.id.ll_photo)
    LinearLayout llPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_uploadphoto);
        ButterKnife.bind(this);
        context = this;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_cancel:
                finish();
                break;
            case R.id.tv_photograph:

                if (start.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory() + PHOTOPATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String tempphotoname = System.currentTimeMillis() + ".jpg";
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(Environment.getExternalStorageDirectory() + PHOTOPATH).append(tempphotoname);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(buffer.toString())));
                    startActivityForResult(intent, 1);
                    Intent intentResult = new Intent();
                    LogUtil.e("purl", buffer.toString());
                    intentResult.putExtra("imgpath", buffer.toString());
                    //setResult(RegisterMesActivity.RESULT_OK, intentResult );
                    break;
                }
            case R.id.tv_localupload:
                if (start.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                    getImage.setType("image/jpeg");
                    startActivityForResult(getImage, 0);

                }
                break;
        }
    }


}
