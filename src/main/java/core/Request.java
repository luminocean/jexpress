package core;

import java.util.Map;

import core.server.Header;

public class Request {
	public String path; // 客户端请求的路径
	public Method method;
	public String pathBeyondCaptured; // 请求以某一路径捕获后剩下的部分
	public String matchedPath; // 匹配成功的部分
	public Map<String, String> params; // url里面匹配取出的参数

	public Request(Header header) {
		this.path = header.path;
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
