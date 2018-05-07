package com.myzyb.appNYB.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;


public class ImageUtils {
	
//	第一：我们先看下质量压缩方法：
	public final static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 50;
		while ( baos.toByteArray().length / 1024>512) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
//	第二：图片按比例大小压缩方法（根据路径获取图片并压缩）：
	public final static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	
//	第三：图片按比例大小压缩方法（根据Bitmap图片压缩）：
	public final static Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		if( baos.toByteArray().length / 1024>512) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为1920f
		float ww = 480f;//这里设置宽度为1080f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
	/**
	 * 将图片文件转换成bitmap
	 * @param imgPath 图片文件路径
	 * @param width 宽度
	 * @param height 高度
	 * @param isThumbnail 是否根据高宽生成缩略图
	 * @return
	 */
	public static Bitmap imgPathToBitmap(String imgPath, int width, int height, boolean isThumbnail) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = getBitmapOpt();
		if ((height == 0 && width == 0) || isThumbnail) {
			options.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeFile(imgPath, options);
			} catch (OutOfMemoryError e) {
			}
		}
		// 需要缩放比例
		else {
			options.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeFile(imgPath, options);
			} catch (OutOfMemoryError e) {
			}
			// 计算缩放比
			int h = options.outHeight;
			int w = options.outWidth;
			int be = 1;
			if (width > 0 && w > width) {
				be = w / width;
			}
			if (height > 0 && h > height) {
				int be2 = h / height;
				if (be2 > be)
					be = be2;
			}
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			try {

				bitmap = BitmapFactory.decodeFile(imgPath, options);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
			}
		}
		int degree = getBitmapDegree(imgPath);
		// 如果有角度上的旋转,则纠正图片;
		if (degree != 0) {
			bitmap = correctImage(degree, bitmap);
//			bitmap = PicUtils.rotateBitmapByDegree(bitmap, degree);
		}
		// 生成固定尺寸的缩略图
		if (isThumbnail) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}
	// 获取优化内存的图片模式
		public static BitmapFactory.Options getBitmapOpt() {
			// 配置bitmap，防止内存溢出
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			return opt;
		}

	/**
		 * 根据角度值,重新构造一个正向的图片;
		 * 
		 * @param angle
		 * @param bitmap
		 * @return
		 */
		public static Bitmap correctImage(int angle, Bitmap bitmap) {
			Matrix mx = new Matrix();
			mx.postRotate(angle);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, mx, true);
			return resizedBitmap;
		}
		/**
		 * 读取图片的旋转的角度
		 * @param path
		 *  图片绝对路径
		 * @return 图片的旋转角度
		 */
		 
		public final static int getBitmapDegree(String path) {
			int degree = 0;
		    try {
		        // 从指定路径下读取图片，并获取其EXIF信息
		        ExifInterface exifInterface = new ExifInterface(path);
		        // 获取图片的旋转信息
		        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
		                ExifInterface.ORIENTATION_NORMAL);
		        switch (orientation) {
		        case ExifInterface.ORIENTATION_ROTATE_90:
		            degree = 90;
		            break;
		        case ExifInterface.ORIENTATION_ROTATE_180:
		            degree = 180;
		            break;
		        case ExifInterface.ORIENTATION_ROTATE_270:
		            degree = 270;
		            break;
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return degree;
		 
		}
	 


		/**
		 * 将图片按照某个角度进行旋转
		 * @param bm
		 * 需要旋转的图片
		 * @param degree
		 * 旋转角度
		 * @return 旋转后的图片
		 */
	 
		public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
			    Bitmap returnBm = null;
			    // 根据旋转角度，生成旋转矩阵
			    Matrix matrix = new Matrix();
			    matrix.postRotate(degree);
			    try {
			        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
			    } catch (OutOfMemoryError e) {
			    	
			    }
			    if (returnBm == null) {
			        returnBm = bm;
			    }
			    if (bm != returnBm) {
			        bm.recycle();
			    }
			    return returnBm;
			} 
		
		public static Bitmap setImage(Context context,Uri mImageCaptureUri) {  
			    // 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值  
			    // 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看  
			Bitmap bitmap = null;
			   ContentResolver cr = context.getContentResolver();  
			    Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找  
			    if (cursor != null) {  
			        cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了  
			        String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路  
			        String orientation = cursor.getString(cursor  
			                .getColumnIndex("orientation"));// 获取旋转的角度  
			        cursor.close();  
			        if (filePath != null) {  
			            Bitmap bm = BitmapFactory.decodeFile(filePath);//根据Path读取资源图片  
			            int angle = 0;  
			            if (orientation != null && !"".equals(orientation)) {  
			                angle = Integer.parseInt(orientation);  
			            }  
			            if (angle != 0) {  
			                // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等  
			                Matrix m = new Matrix();  
			                int width = bm.getWidth();  
			                int height = bm.getHeight();  
			                m.setRotate(angle); // 旋转angle度  
			                bitmap = Bitmap.createBitmap(bm, 0, 0, width, height,  
			                        m, true);// 从新生成图片  
			                  
			            }  
//					photo.setImageBitmap(bitmap);  
			        }  
			    }
				return bitmap;  
			}  
		
		/**
		 * 根据图片的Uri路径获得图片保存路径
		 * @param
		 * @return
		 */
		public static final String getPathByUri(Context context, Uri uri) {
	        if (uri == null)
	                return null;

	        if ("file".equals(uri.getScheme())) {
	                return uri.getPath();
	        }

	        try {
	                String[] projection = { MediaStore.Images.Media.DATA };
	                Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, projection, null, null);
	                if (cursor == null)
	                        return null;
	                cursor.moveToNext();
	                if (cursor.getCount() == 0) {
	                        cursor.close();
	                        return null;
	                } else {
	                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
	                        cursor.close();
	                        return imagePath;
	                }
	        } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	        } catch (RuntimeException e) {
	                e.printStackTrace();
	        }

	        return null;
	}
		/**
		 * 根据图片的url路径获得Bitmap对象    容易内存溢出
		 * @param url
		 * @return
		 */
		public static final Bitmap returnBitmap(String url) {
		    URL fileUrl = null;
		    Bitmap bitmap = null;
		 
		    try {
		        fileUrl = new URL(url);
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		    }
		 
		    try {
		        HttpURLConnection conn = (HttpURLConnection) fileUrl
		                .openConnection();
		        conn.setDoInput(true);
		        conn.connect();
		        InputStream is = conn.getInputStream();
		        bitmap = BitmapFactory.decodeStream(is);
		        is.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return bitmap;
		 
		}
}