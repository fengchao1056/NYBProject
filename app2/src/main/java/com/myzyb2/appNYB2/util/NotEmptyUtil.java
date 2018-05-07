package com.myzyb2.appNYB2.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by cuiz on 2015/12/31.
 */
public class NotEmptyUtil {

    public static boolean NotEmpty(EditText editText) {
        final boolean[] flag = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    flag[0] = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return flag[0];

    }

}
