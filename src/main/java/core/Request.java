package core;

import core.server.Header;

public class Request {
	public String path;
	public Method method;
	public String pathBeyondCaptured; // 请求以某一路径捕获后剩下的部分

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
