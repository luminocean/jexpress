package core.server;

/**
 * Http报文头数据结构
 * @author luminocean
 *
 */
public class Header {
	public String method; // http方法
	public String path; // 请求路径,包含GET查询字符串
	public String version; // http版本
	
	public Header(String str) {
		String[] lines = str.split("\n");
		
		// 第一行
		String methodLine = lines[0];
		String[] parts = methodLine.split(" ");
		method = parts[0].replace(" ", "");
		path = parts[1].replace(" ", "");
		version = parts[2].replace(" ", "");
	}
}
