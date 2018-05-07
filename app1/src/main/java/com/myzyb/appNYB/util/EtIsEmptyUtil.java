package com.myzyb.appNYB.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by cuiz on 2015/12/12.
 */
public class EtIsEmptyUtil {
    public static String text;
    private static boolean isEmp = false;
    public EtIsEmptyUtil() {

    }

    public static boolean etIsEmpty(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();

                if (!TextUtils.isEmpty(text)) {
                    isEmp = true;
                    LogUtil.e("isEmp", isEmp + "");
                    LogUtil.e("isEmp", text);


                } else {
                    isEmp = false;
                    LogUtil.e("isEmp", isEmp + "");
                    LogUtil.e("isEmp", "kongde");
                }

            }

        });
        return isEmp;


    }


}
