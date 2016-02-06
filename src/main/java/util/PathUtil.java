package util;

import java.util.regex.Pattern;

public class PathUtil {
	/**
	 * 标准化目录路径
	 * 即前面没有/，后面必须有/
	 * @param path
	 * @return
	 */
	public static String normalizeDirPath(String path){
		if(path.startsWith("/")) path = path.substring(1);
		if(!path.endsWith("/")) path = path + "/";
		return path;
	}
	
	/**
	 * 标准化文件路径
	 * 即前面没有/，后面必须也没有/
	 * @param path
	 * @return
	 */
	public static String normalizeFilePath(String path){
		if(path.startsWith("/")) path = path.substring(1);
		if(path.endsWith("/")) path = path.substring(0, path.length()-1);
		return path;
	}

	/**
	 * 请求被Node捕获后剩下的路径
	 * @param requestPath 请求全路径
	 * @param watchPath 监控路径
	 * @return
	 */
	public static String pathAfterCaptured(String requestPath, String watchPath) {
		assert requestPath.startsWith(watchPath);
		return requestPath.substring(watchPath.length());
	}
}
