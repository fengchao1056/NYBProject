package com.myzyb2.appNYB2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
	/**
	 * @param is	接收输入流
	 * @return		返回此流转换成的字符串
	 * @throws IOException	抛出的异常类型
	 */
	public static String stream2String(InputStream is) throws IOException {
		//读取到的数据存储到一个缓存空间然后一次性写出作为一个字符串返回
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int temp = -1;
		
		while((temp = is.read(buffer))!=-1){
			//还有数据
			bos.write(buffer, 0, temp);
		}
		String result = bos.toString();
		bos.close();
		return result;
	}
}
