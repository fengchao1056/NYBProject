package com.myzyb2.appNYB2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myzyb2.appNYB2.R;

/**
 * layout转BitmapDrawable
 * Created by cuiz on 2016/4/22.
 */
public class ViewToBitmap {

    public static Bitmap ViewToBitmap(Context context, int layout, String s1 , String s2,int status) {
        LayoutInflater factory = LayoutInflater.from(context);
        View textEntryView = factory.inflate(layout, null); ////把视图转换成Bitmap 再转换成Drawable
        TextView name = (TextView) textEntryView.findViewById(R.id.marker_text);
        TextView weight = (TextView) textEntryView.findViewById(R.id.marker_weight);
        LinearLayout ll_bg= (LinearLayout) textEntryView.findViewById(R.id.ll_bg);
        if(0==status){
            ll_bg.setBackgroundResource(R.drawable.tubaio_green);
        }else{
            ll_bg.setBackgroundResource(R.drawable.tubaio_red);
        }
       // weight.setGravity(Gravity.CENTER);
        name.setText(s1);
       weight.setText(s2);

        textEntryView.setDrawingCacheEnabled(true);
        textEntryView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        textEntryView.layout(0, 0, textEntryView.getMeasuredWidth(),
                textEntryView.getMeasuredHeight());
        textEntryView.buildDrawingCache();
        Bitmap newbmp = textEntryView.getDrawingCache();
        return newbmp;
    }
}
