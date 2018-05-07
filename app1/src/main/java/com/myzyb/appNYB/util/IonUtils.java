package com.myzyb.appNYB.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Ion.Config;
import com.myzyb.appNYB.R;


public class IonUtils {
    private static Ion ion;
    private static IonUtils ionUtils;
    public static final String POST = "POST";
    public static final String GET = "GET";

    public static IonUtils getInstence(Context context) {
        if (ionUtils == null) {
            ionUtils = new IonUtils();
            ion = Ion.getDefault(context);
            Config config = ion.configure();
            ion.getPendingRequestCount(1);
            ion.getServer();
            ion.getServer();
        }
        return ionUtils;
    }

    public static void loadImage(Context context, String url,
                                 ImageView imageView) {
        try {
            Ion.with(context).load(url).withBitmap()
                    .placeholder(R.drawable.baitu)
                    .error(R.drawable.baitu).intoImageView(imageView);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void loadImageView(Context context, String url,
                                     ImageView imageview, final int imageId) {
        try {
            final ImageView iv_temp = imageview;
            if (!TextUtils.isEmpty(url)) {
                Ion.with(context).load(url).asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {

                            @Override
                            public void onCompleted(Exception arg0, Bitmap arg1) {
                                if (arg1 != null) {
                                    iv_temp.setImageBitmap(arg1);
                                } else {
                                    iv_temp.setImageResource(imageId);
                                }

                            }
                        });
            } else {
                iv_temp.setImageResource(imageId);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
