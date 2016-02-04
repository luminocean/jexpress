package core;

import core.server.Header;

public class Request {
	public String path;
	public String method;
	public String pathBeyondCaptured; // 请求以某一路径捕获后剩下的部分

	public Request(Header header) {
		this.path = header.path;
		this.method = header.method;
	}
}
