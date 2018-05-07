package com.myzyb2.appNYB2.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.myzyb2.appNYB2.common.CommApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 程序常用类，基于整个程序中使用，这里是对程序中，重复代码复用抽取
 *
 * @ClassName: CommonUtil
 * @Description: TODO
 * @date modify by 2015-7-6 上午9:21:28
 */

public class CommonUtil {
    private static String NOT_LOGIN = "notlogin";


    /**
     * 在主线程执行Runnable
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        CommApplication.getMainHandler().post(r);
    }


    public static void StartToast(Context context, String string) {
        Toast toast = null;
        if (toast == null) {
            toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        } else {
            toast.setText(string);
        }
        View view = toast.getView();
        toast.setView(view);
        toast.show();
    }

    /**
     * 将自己从父view移除
     *
     * @param child
     */
    public static void removeSelfFromParent(View child) {
        if (child != null) {
            ViewParent parent = child.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(child);//移除子view
            }
        }
    }

    /**
     * 关闭软键盘
     */
    public static void colseKeyset(Activity activity, Context context) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * 判定网络状态
     *
     * @param context
     * @return
     */

    public static boolean hasNetwork(Context context) {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();

            return flag;
        }
        /*if (!flag) {
            setNetwork();
		} else {
			isNetworkAvailable();
		}
*/
        return flag;
    }
    /*public static boolean hasNetwork(Context context) {
        // 检查网络状态
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			return false;
		}
		return true;
	}*/

    /**
     * 检查是否需要登陆
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public static boolean invilidateLogin(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        String responseCode = jsonObject.getString("response");
        if (NOT_LOGIN.equals(responseCode)) {
            return true;
        }
        return false;
    }


    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static float getscreenWidthScale(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int widthPixels = displayMetrics.widthPixels;

        float h = (float) widthPixels / 720;
        LogUtil.e("h", h + "");
        return h;
    }

    public static double getscreenHeightScale(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int heightPixels = displayMetrics.heightPixels;

        LogUtil.e("widthPixels", heightPixels + "");
        float w = (float) heightPixels / 1280;
        LogUtil.e("w", w + "");
        return w;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int picScreenHeight = 0;
        picScreenHeight = (screenWidth * picHeight) / picWidth;
        return picScreenHeight;
    }

    /**
     * 从assets文件夹中获取图片
     *
     * @param ct
     * @param fileName
     * @return
     */
    public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
        Bitmap image = null;
        AssetManager am = ct.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    /**
     * 异步任务执行框架
     *
     * @param task
     * @param params
     */
    public static <Params, Progress, Result> void executeAsyncTask(AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    /**
     * 计算上传时间
     *
     * @param created
     * @return
     */
    public static String getUploadtime(long created) {
        StringBuffer when = new StringBuffer();
        int difference_seconds;
        int difference_minutes;
        int difference_hours;
        int difference_days;
        int difference_months;
        long curTime = System.currentTimeMillis();
        difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
        if (difference_months > 0) {
            when.append(difference_months + "月");
        }

        difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
        if (difference_days > 0) {
            when.append(difference_days + "天");
        }

        difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
        if (difference_hours > 0) {
            when.append(difference_hours + "小时");
        }

        difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
        if (difference_minutes > 0) {
            when.append(difference_minutes + "分钟");
        }

        difference_seconds = (int) ((curTime % 60) - (created % 60));
        if (difference_seconds > 0) {
            when.append(difference_seconds + "秒");
        }

        return when.append("前").toString();
    }

    public static boolean hasToken(Context ct) {
        String token = SharedPreferenceUtil.getString(ct, "token", "");
        if (TextUtils.isEmpty(token)) {
            return false;
        } else {
            return true;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getId()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 格式化时间
     *
     * @return
     */
    public static String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public static String formatDateToDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }


    public static Date[] formatDateOneDay(String date, boolean b) throws ParseException {
        String dayTime;
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat secondFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (b) {
            dayTime = date;
        } else {
            dayTime = dayFormat.format(new Date());
        }
        String startTime = new StringBuffer().append(dayTime).append(" 00:00:00").toString();
        String endTime = new StringBuffer().append(dayTime).append(" 23:59:59").toString();
        Date startDate = secondFormat.parse(startTime);
        Date endDate = secondFormat.parse(endTime);
        Date[] dates = {startDate, endDate};
        return dates;
    }

    public static Date[] formatDate(String date, boolean b) throws ParseException {
        String dayTime;
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat secondFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (b) {
            dayTime = date;
        } else {
            dayTime = dayFormat.format(new Date());
        }
        String startTime = new StringBuffer().append(dayTime).append(" 00:00:00").toString();
        String endTime = new StringBuffer().append(dayTime).append(" 23:59:59").toString();
        Date startDate = secondFormat.parse(startTime);
        Date endDate = secondFormat.parse(endTime);
        Date[] dates = {startDate, endDate};
        return dates;
    }

    /**
     * @return 返回版本名称 如果返回null,获取版本名称异常
     */
    public static String getVersionName(Context context) {
        //获取版本号,PackageMananger---->PackageInfo------->versionName,versionName
        //1,创建PackageMananger对象
        PackageManager pm = context.getPackageManager();
        //2,获取PackageInfo对象(包名,0获取PackageInfo对象)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    };

}
