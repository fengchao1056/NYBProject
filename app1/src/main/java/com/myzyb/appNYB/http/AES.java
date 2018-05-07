package com.myzyb.appNYB.http;

import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;

/**
 * Created by tanfangliang on 17/11/7.
 */

public class AES {

    //解密
    public static JSONObject desEncrypt(String data) throws Exception {
        try
        {
            String key = SharedPreferenceUtil.getString(CommApplication.getContext(),
                    Constant.KEY, "");
            String iv = SharedPreferenceUtil.getString(CommApplication.getContext(),
                    Constant.IV, "");
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return new JSONObject(originalString);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
