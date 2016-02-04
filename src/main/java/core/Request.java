package core;

import core.server.Header;

public class Request {
	public String path;
	public String method;

	public Request(Header header) {
		this.path = header.path;
		this.method = header.method;
	}
}
