package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystem {
	private static Logger logger = LoggerFactory.getLogger(FileSystem.class);

	/**
	 * 以文本的方式读取文件
	 * @param filePath
	 * @return
	 */
	public static String readTextFile(String filePath) {
		// 读取文本的二进制数据
		byte[] data = readFile(filePath);
		try {
			// 转成utf-8文本后返回
			return new String(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return "";
		}
	}
	
	/**
	 * 以二进制的方式读取文件
	 * @param filePath
	 * @return
	 */
	public static byte[] readFile(String filePath){
		ByteBuffer dest = new ByteBuffer(1024); // 初始大小
		try {
			byte[] buf = new byte[1024];
			InputStream is = ClassLoader.getSystemResourceAsStream(filePath);
			int count = is.read(buf);
			while( count > 0){
				dest.writeIn(buf, count);
				count = is.read(buf);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			return new byte[0];
		}
		
		return dest.data;
	}
}

/**
 * 字节缓冲区
 * @author luminocean
 *
 */
class ByteBuffer{
	public byte[] data;
	public int index = 0; // 下一个要写入的字节索引位置
	
	public ByteBuffer(int size){
		data = new byte[size];
	}
	
	/**
	 * 数据从数据源写入缓冲区
	 * @param buf 数据源
	 * @param count 要写入的数据个数
	 */
	public void writeIn(byte[] buf, int count) {
		// 现有空间不够写入新的字节，扩容
		while(free() < count){
			expend();
		}
		
		// 新数据写入
		for(int i=0; i<count; i++){
			data[index++] = buf[i];
		}
	}
	
	// 数据区扩容
	private void expend(){
		// 新数据区两倍扩容
		byte[] newData = new byte[data.length * 2];
		// 旧数组数据拷贝到新数组里面去
		for(int i=0; i<data.length; i++){
			newData[i] = data[i];
		}
		data = newData;
	}

	// 还能写入多少字节
	private int free(){
		return data.length - index;
	}
}
