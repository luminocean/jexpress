package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mime类型查询工具
 * @author luminocean
 *
 */
public class MimeUtil {
	private static Map<String, String> mimeMap = new HashMap<>();
	private static Pattern postfixPattern = Pattern.compile("([^/.]*)$");
	
	static{
		mimeMap.put("html", "text/html");
		mimeMap.put("xml", "text/xml");
		mimeMap.put("txt", "text/plain");
		mimeMap.put("pdf", "application/pdf");
		mimeMap.put("png", "image/png");
		mimeMap.put("gif", "image/gif");
		mimeMap.put("jpg", "image/jpeg");
		mimeMap.put("jpeg", "image/jpeg");
		mimeMap.put("mp4", "video/mp4");
		mimeMap.put("mp3", "audio/mpeg");
	}
	
	/**
	 * 根据传入的文件路径（其实只是后缀）判断文件mime类型
	 * @param path
	 * @return 文件路径对应的mime字符串，如果找不到默认返回text/plain
	 */
	public static String mime(String path){
		Matcher matcher = postfixPattern.matcher(path);
		if(matcher.find()){
			String postfix = matcher.group(1);
			String mime = mimeMap.get(postfix);
			
			if(mime != null) return mime;
		}
		
		return "text/plain"; // 默认类型为纯文本
	}
}
