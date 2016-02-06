package core;

import java.util.HashMap;
import java.util.Map;

import core.server.Header;

public class Request {
	public String path; // 客户端请求的路径，不含GET查询字符串
	public Method method;
	public String pathBeyondCaptured; // 请求以某一路径捕获后剩下的部分
	public String matchedPath; // 匹配成功的部分
	public Map<String, String> params = new HashMap<>(); // url里面匹配取出的参数

	public Request(Header header) {
		// 解析请求路径与URL参数
		String requestPath = header.path;
		int splitIndex = requestPath.indexOf('?');
		// 有请求参数，解析参数键值对
		if(splitIndex >= 1 && splitIndex < requestPath.length()-1){
			// 实际的请求路径
			this.path = requestPath.substring(0, splitIndex);
			
			String paramStr = requestPath.substring(splitIndex+1);
			String[] pairs = paramStr.split("&");
			for(String pair: pairs){
				String[] splits = pair.split("=");
				params.put(splits[0], splits[1]);
			}
		}
		// 没有请求参数，直接设置path即可
		else{
			this.path = header.path;
		}
		
		switch(header.method){
			case "GET": method = Method.GET; break;
			case "POST": method = Method.POST; break;
			case "PUT": method = Method.PUT; break;
			case "DELETE": method = Method.DELETE; break;
			case "ALL": method = Method.ALL; break;
			default: method = Method.NONE;
		}
	}
}
