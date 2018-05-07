package com.myzyb.appNYB.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by cuiz on 2016/1/28.
 */
public class FontsUtil {
    public FontsUtil() {
    }

    public static void FontsUtil(TextView text, Context context) {

        Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/Bodoni 72 Smallcaps Book.ttf");
        text.setTypeface(fontFace);

    }
}
