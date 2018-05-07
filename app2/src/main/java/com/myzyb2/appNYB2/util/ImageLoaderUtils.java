package com.myzyb2.appNYB2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


import com.myzyb2.appNYB2.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class ImageLoaderUtils {

	private static ImageLoaderUtils imageLoaderUtils;
	private static DisplayImageOptions options;
	private static ImageLoader imageLoader;
	public static ImageLoaderUtils newInstance(){
		if(imageLoaderUtils==null){
			imageLoaderUtils = new ImageLoaderUtils();
			imageLoader = ImageLoader.getInstance();
			
			config();
		}
		return imageLoaderUtils; 
	}
	/**
	 * 
	 */
	public static void config(){
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.cell_hold)
		.showImageForEmptyUri(R.drawable.cell_hold)
		.showImageOnFail(R.drawable.cell_hold)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

//		File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.initbuild(context)
//		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
//		        .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个 
//		        .taskExecutor(...)
//		        .taskExecutorForCachedImages(...)
//		        .threadPoolSize(3) // default  线程池内加载的数量
//		        .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
//		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//		        .denyCacheImageMultipleSizesInMemory()
//		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
//		        .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
//		        .memoryCacheSizePercentage(13) // default
//		        .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径  
//		        .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
//		        .diskCacheFileCount(100)  // 可以缓存的文件数量 
//		        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
//		        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) 
//		        .imageDownloader(new BaseImageDownloader(context)) // default
////		        .imageDecoder(new BaseImageDecoder()) // default
//		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//		        .writeDebugLogs() // 打印debug log
//		        .build(); //开始构建
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		imageLoader.init(config);
	}
	
	public  void loadImage(String iamgeUrl,ImageView imageView){
		imageLoader.displayImage(iamgeUrl, imageView, options);
	}
	public  void loadImage(String iamgeUrl,ImageView imageView,int imageId){
		config(imageId);
		imageLoader.displayImage(iamgeUrl, imageView, options);
	}
	
	public static void config(int imageId ){
		options = new DisplayImageOptions.Builder()
		.showStubImage(imageId)
		.showImageForEmptyUri(imageId)
		.showImageOnFail(imageId)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
}
