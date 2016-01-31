package core;

public class HttpHeader {
	public String method; // http方法
	public String path; // 请求路径
	public String version; // http版本
	
	public HttpHeader(String str) {
		String[] lines = str.split("\n");
		// 第一行
		String methodLine = lines[0];
		String[] parts = methodLine.split(" ");
		method = parts[0];
		path = parts[1];
		version = parts[2];
	}
}
